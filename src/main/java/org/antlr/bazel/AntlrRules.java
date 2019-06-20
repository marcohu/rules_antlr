package org.antlr.bazel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Runs ANTLR and creates a .srcjar with the generated files.
 *
 * @author  Marco Hunsicker
 */
public class AntlrRules
{
    private String[] args;
    private String[] classpath;
    private Charset encoding = Charset.defaultCharset();
    private final FileSystem fs;
    private List<String> grammars;
    private boolean keepImports;
    private Language language;
    private String layout;
    private Namespace namespace;
    private Path outputDirectory;
    private final Path sandbox;
    private Path srcjar;
    private Version version;
    private Output output;
    private boolean split = true;

    /**
     * Creates a new AntlrRules object.
     *
     * @param  root  the root directory.
     */
    private AntlrRules(Path root)
    {
        this.sandbox = root;
        this.fs = root.getFileSystem();
    }

    /**
     * Main program entry point.
     *
     * @param   args  the command-line arguments.
     *
     * @throws  Exception  if an error occurred.
     */
    public static void main(String[] args) throws Exception
    {
        // for simplicity we use environment variables for configuration and pass through
        // the command-line arguments to ANTLR
        Map<String, String> env = System.getenv();

        AntlrRules.create()
            .srcjar(env.get("SRC_JAR"))
            .version(env.get("ANTLR_VERSION"))
            .classpath(env.get("TOOL_CLASSPATH").split(","))
            .outputDirectory(env.get("OUTPUT_DIRECTORY"))
            .encoding(env.get("ENCODING"))
            .grammars(env.get("GRAMMARS").split(","))
            .namespace(env.get("PACKAGE_NAME"))
            .language(env.get("TARGET_LANGUAGE"))
            .layout(env.get("DIRECTORY_LAYOUT"))
            .args(args)
            .generate();
    }


    static AntlrRules create(Path root) throws IOException
    {
        return new AntlrRules(root.toRealPath());
    }


    AntlrRules args(String[] args)
    {
        this.args = args;

        keepImports = Arrays.asList(args).contains("-XsaveLexer");

        return this;
    }


    AntlrRules classpath(String[] classpath)
    {
        this.classpath = classpath;

        return this;
    }


    AntlrRules encoding(String encoding)
    {
        this.encoding = encoding.isEmpty() ? Charset.defaultCharset()
                                           : Charset.forName(encoding);

        return this;
    }


    void generate() throws Exception
    {
        Map<Namespace, Collection<Grammar>> namespaces = groupByNamespace(grammars);

        // use reflection so we are not tied to a specific ANTLR version
        try (URLClassLoader loader = classloader(classpath))
        {
            switch (version)
            {
                case V2 :
                {
                    List<String> arguments = new ArrayList<>(Arrays.asList(args));

                    // ANTLR 2 does only accept a single grammar per invocation
                    for (String grammar : grammars)
                    {
                        arguments.add(grammar);
                        supergrammars(arguments);

                        antlr2(loader, arguments.toArray(new String[arguments.size()]));

                        arguments.remove(arguments.size() - 1);
                    }

                    break;
                }

                case V3 :
                {
                    antlr3(loader, new Arguments(args).build(grammars));

                    break;
                }

                case V4 :
                {
                    Arguments arguments = new Arguments(args);

                    for (Map.Entry<Namespace, Collection<Grammar>> e
                        : namespaces.entrySet())
                    {
                        antlr4(loader,
                            arguments.log,
                            arguments.build(e.getKey(), e.getValue()));
                    }

                    break;
                }
            }
        }

        Map<String, Grammar> names = grammarNames(namespaces);

        switch (output)
        {
            case FOLDER:
            {
                Files.createDirectories(outputDirectory);
                Path other = Files.createDirectories(
                        outputDirectory
                            .getParent()
                            .resolve(
                                outputDirectory
                                    .getFileName()
                                    .toString()
                                    .replace(".cc", ".antlr")));
                Path headers = Files.createDirectories(
                        outputDirectory
                            .getParent()
                            .resolve(
                                outputDirectory
                                    .getFileName()
                                    .toString()
                                    .replace(".cc", ".inc")));
                Path includes = Files.createDirectories(
                        outputDirectory
                            .getParent()
                            .resolve(
                                outputDirectory
                                    .getFileName()
                                    .toString()
                                    .replace(".cc", ".inc")));
                Files.createDirectories(includes);

                List<String> files = new ArrayList<>();

                try (DirectoryStream<Path> entries = Files.newDirectoryStream(outputDirectory))
                {
                    PathMatcher expanded = outputDirectory.getFileSystem()
                        .getPathMatcher("glob:**/expanded*.g");
                    PathMatcher csources = outputDirectory.getFileSystem()
                        .getPathMatcher("glob:**.{c,cc,cpp,cxx,c++,C}");
                    PathMatcher cheaders = outputDirectory.getFileSystem()
                        .getPathMatcher("glob:**.{h,hh,hpp,hxx,inc,inl,H}");

                    for (Path entry : entries)
                    {
                        // for extended grammars ANTLR 2 creates a new grammar file that
                        // merges the two grammars and must be ignored
                        if (expanded.matches(entry))
                        {
                            Files.delete(entry);

                            continue;
                        }

                        String fileName = entry.getFileName().toString();

                        if (fileName.endsWith(".log"))
                        {
                            Files.move(entry, other.resolve(entry.getFileName()));

                            continue;
                        }

                        Grammar grammar = findGrammar(entry, names);

                        // indicates imported file that should not be kept
                        if (grammar == null)
                        {
                            Files.delete(entry);

                            continue;
                        }

                        if (language != null)
                        switch (language)
                        {
                            case C :
                            case CPP :
                            {
                                if (cheaders.matches(entry))
                                {
                                    if (split)
                                    {
                                        Path target = headers.resolve(
                                                grammar.getNamespacePath().toString())
                                                .resolve(entry.getFileName());
                                        Files.createDirectories(target.getParent());
                                        Files.move(entry, target);

                                        continue;
                                    }
                                }
                                else if (!csources.matches(entry))
                                {
                                    Files.move(entry, other.resolve(entry.getFileName()));

                                    continue;
                                }
                            }
                        }

                        // source files should be stored below their corresponding
                        // package/namespace
                        Path target = outputDirectory.resolve(
                                grammar.getNamespacePath().toString())
                                .resolve(entry.getFileName());

                        files.add(outputDirectory.relativize(target).toString());

                        if (!target.equals(entry))
                        {
                            Files.createDirectories(target.getParent());
                            Files.move(entry, target);
                        }
                    }
                }
                break;
            }

            case SRCJAR:
            {
                URI uri = URI.create("jar:file:" + srcjar.toUri().getPath());
                Map<String, String> env = new HashMap<>();
                env.put("create", "true");

                try (FileSystem archive = FileSystems.newFileSystem(uri, env))
                {
                    Path root = archive.getPath("/");

                    Files.walkFileTree(outputDirectory, new SimpleFileVisitor<Path>()
                        {
                            CopyOption[] options =
                                {
                                    StandardCopyOption.COPY_ATTRIBUTES,
                                    StandardCopyOption.REPLACE_EXISTING
                                };

                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
                                throws IOException
                            {
                                String filename = file.getFileName().toString();

                                if (filename.endsWith(".srcjar"))
                                {
                                    return CONTINUE;
                                }

                                if (filename.startsWith("expanded"))
                                {
                                    return CONTINUE;
                                }

                                Path target = root.resolve(
                                    outputDirectory.relativize(file).toString());

                                if (!filename.endsWith(".log"))
                                {
                                    Grammar grammar = findGrammar(file, names);

                                    // indicates imported file that does not belong in the .srcjar
                                    if (grammar == null)
                                    {
                                        return CONTINUE;
                                    }

                                    // source files should be stored below their corresponding
                                    // package/namespace
                                    target = root.resolve(grammar.getNamespacePath().toString())
                                        .resolve(target.getFileName());
                                }

                                Files.createDirectories(target.getParent());
                                Files.copy(file, target, options);

                                return CONTINUE;
                            }
                        });
                }
                break;
            }
        }
    }


    AntlrRules grammars(String... grammars)
    {
        this.grammars = new ArrayList<>(grammars.length);

        for (String grammar : grammars)
        {
            this.grammars.add(sandbox.resolve(grammar).toString());
        }

        return this;
    }


    AntlrRules language(String language)
    {
        this.language = language.isEmpty() ? null : Language.of(language);

        return this;
    }


    AntlrRules layout(String layout)
    {
        this.layout = layout.isEmpty() ? null : layout;

        return this;
    }


    AntlrRules namespace(String namespace)
    {
        this.namespace = namespace.isEmpty() ? null : Namespace.of(namespace);

        return this;
    }


    AntlrRules outputDirectory(String directory)
    {
        outputDirectory = sandbox.resolve(directory);

        return this;
    }


    AntlrRules srcjar(String srcjar)
    {
        this.srcjar = sandbox.resolve(srcjar);
        this.output = srcjar.isBlank() ? Output.FOLDER : Output.SRCJAR;

        return this;
    }


    AntlrRules version(String version)
    {
        this.version = Version.of(version);

        return this;
    }


    private static AntlrRules create() throws IOException
    {
        return new AntlrRules(Paths.get(".").toRealPath());
    }


    private void antlr2(URLClassLoader loader, String[] args) throws Exception
    {
        Class<?> $Tool = loader.loadClass("antlr.Tool");

        $Tool.getDeclaredMethod("doEverything", String[].class)
            .invoke($Tool.getDeclaredConstructor().newInstance(), new Object[] { args });
    }


    private void antlr3(URLClassLoader loader, String[] args) throws Exception
    {
        Class<?> $Tool = loader.loadClass("org.antlr.Tool");
        Class<?> $ErrorManager = loader.loadClass("org.antlr.tool.ErrorManager");

        Object tool = $Tool.getConstructor(String[].class)
            .newInstance(new Object[] { args });

        $Tool.getDeclaredMethod("process").invoke(tool);

        int errors = (int) $ErrorManager.getDeclaredMethod("getNumErrors").invoke(null);

        if (errors > 0)
        {
            throw new IllegalStateException(
                String.format("ANTLR terminated with %s error%s",
                    errors,
                    (errors == 1) ? "" : "s"));
        }
    }


    private void antlr4(URLClassLoader loader, boolean log, String[] args)
        throws Exception
    {
        Class<?> $Tool = loader.loadClass("org.antlr.v4.Tool");
        Class<?> $ErrorManager = loader.loadClass("org.antlr.v4.tool.ErrorManager");
        Object tool = $Tool.getConstructor(String[].class)
            .newInstance(new Object[] { args });
        Object errorManager = $Tool.getDeclaredField("errMgr").get(tool);
        $Tool.getDeclaredMethod("processGrammarsOnCommandLine").invoke(tool);

        if (log)
        {
            Class<?> $LogManager = loader.loadClass(
                "org.antlr.v4.runtime.misc.LogManager");
            Object logManager = $Tool.getDeclaredField("logMgr").get(tool);
            String filename = (String) $LogManager.getDeclaredMethod("save")
                .invoke(logManager);
            Path logFile = fs.getPath(filename).toRealPath();
            Files.copy(logFile, outputDirectory.resolve(logFile.getFileName()));
            Files.delete(logFile);
        }

        int errors = (int) $ErrorManager.getDeclaredMethod("getNumErrors")
            .invoke(errorManager);

        if (errors > 0)
        {
            throw new IllegalStateException(
                String.format("ANTLR terminated with %s error%s",
                    errors,
                    (errors == 1) ? "" : "s"));
        }
    }


    private URLClassLoader classloader(String[] classpath) throws IOException
    {
        PathMatcher matcher = sandbox.getFileSystem().getPathMatcher("glob:**/*.jar");

        Collection<URL> urls = new LinkedHashSet<>();

        for (String path : classpath)
        {
            Path lib = sandbox.resolve(path);

            if (matcher.matches(lib))
            {
                if (Files.notExists(lib))
                {
                    throw new FileNotFoundException(path);
                }

                urls.add(lib.toUri().toURL());
            }
        }

        return new ContextClassLoader(urls, null);
    }


    /**
     * Finds the grammar that corresponds to the given generated file.
     *
     * @param   file      the generated source file.
     * @param   grammars  the processed grammars.
     *
     * @return  the corresponding grammar.
     */
    private Grammar findGrammar(Path file, Map<String, Grammar> grammars)
    {
        for (Map.Entry<String, Grammar> e : grammars.entrySet())
        {
            String fileName = file.getFileName().toString();
            String grammarName = e.getKey();

            if (fileName.startsWith(e.getKey())
                // the Go target uses lower underscore, but not consistently
                || fileName.startsWith(CaseFormat.toLowerUnderscore(grammarName))
                || fileName.startsWith(grammarName.toLowerCase()))
            {
                return e.getValue();
            }

            // ANTLR 2 does not enforce casing for grammars
            if ((version == Version.V2)
                && fileName.toLowerCase().startsWith(e.getKey().toLowerCase()))
            {
                return e.getValue();
            }
        }

        throw new IllegalStateException(
            "Could not find matching grammar for " + file.getFileName());
    }


    /**
     * Creates mappings of the possible grammar output file names sorted from longest name
     * to shortest name to their corresponding grammars.
     *
     * @param   namespaces  the grammars grouped by namespaces.
     *
     * @return  the sorted map.
     */
    private Map<String, Grammar> grammarNames(
        Map<Namespace, Collection<Grammar>> namespaces)
    {
        Map<String, Grammar> result = new TreeMap<>(new LengthComparator());

        for (Collection<Grammar> grammars : namespaces.values())
        {
            for (Grammar grammar : grammars)
            {
                for (String name : grammar.names)
                {
                    result.put(name, grammar);
                }

                for (String name : grammar.imports)
                {
                    result.put(name, keepImports ? grammar : null);
                }
            }
        }

        return result;
    }


    private Map<Namespace, Collection<Grammar>> groupByNamespace(
        Collection<String> grammars) throws IOException
    {
        Map<Namespace, Collection<Grammar>> result = new LinkedHashMap<>();

        for (String path : grammars)
        {
            Grammar grammar = new Grammar(version,
                fs.getPath(path),
                language,
                namespace,
                encoding,
                layout);

            List<Grammar> files = (List<Grammar>) result.get(grammar.namespace);

            if (files == null)
            {
                files = new ArrayList<>();
                result.put(grammar.namespace, files);
            }

            files.add(grammar);

            // enforce order to avoid problems with imported grammars
            Collections.sort(files);
        }

        return result;
    }


    private void supergrammars(List<String> arguments) throws IOException
    {
        int glib = arguments.indexOf("-glib");

        // ANTLR 2 expects all files in the same directory as the specified grammar, but
        // that's not feasible with Bazel. We can workaround that by requiring the .srcjar
        // with the generated sources as an additional -glib entry and put the necessary
        // tokens file where ANTLR can find it
        if (glib > -1)
        {
            String argument = arguments.get(glib + 1);
            String[] libs = argument.split(";");

            boolean srcjarFound = false;

            for (int i = 0; i < libs.length; i++)
            {
                String lib = libs[i];

                if (lib.endsWith(".srcjar"))
                {
                    srcjarFound = true;

                    // by convention the .srcjar has to be provided immediately after the
                    // super grammar. This should be made more foolproof, but as probably
                    // nobody will ever use this feature we just go with it for now
                    String path = libs[i - 1];

                    argument = argument.replace(";" + lib, "");

                    Path target = sandbox.resolve(path).getParent();
                    Files.createDirectories(target);

                    Path srcjar = sandbox.resolve(lib);
                    URI uri = URI.create("jar:file:" + srcjar.toUri().getPath());

                    try (FileSystem fs = FileSystems.newFileSystem(uri,
                            new HashMap<String, String>()))
                    {
                        Files.walkFileTree(fs.getPath("/"),
                            new SimpleFileVisitor<Path>()
                            {
                                @Override
                                public FileVisitResult visitFile(Path file,
                                    BasicFileAttributes attr) throws IOException
                                {
                                    if (file.getFileName().toString().endsWith(".txt"))
                                    {
                                        Path copy = target.resolve(
                                            file.getFileName().toString());

                                        if (Files.notExists(copy))
                                        {
                                            Files.copy(file, copy);
                                        }
                                    }

                                    return CONTINUE;
                                }
                            });
                    }
                }
            }

            if (!srcjarFound)
            {
                throw new IllegalArgumentException(
                    String.format(
                        "You have to provide the .srcjar created for '%s' as well",
                        argument));
            }

            arguments.set(glib + 1, argument);
        }
    }

    private class Arguments
    {
        public boolean log;

        private List<String> arguments;
        private boolean packageAttribute;

        public Arguments(String[] arguments)
        {
            this.arguments = Arrays.asList(arguments);

            for (int i = 0, size = this.arguments.size(); i < size; i++)
            {
                switch (this.arguments.get(i))
                {
                    case "-lib" :
                    {
                        // ensure absolute path
                        this.arguments.set(i + 1,
                            sandbox.resolve(this.arguments.get(i + 1)).toString());

                        break;
                    }

                    case "-Xlog" :
                    {
                        log = true;

                        break;
                    }

                    case "-package" :
                    {
                        packageAttribute = true;

                        break;
                    }
                }
            }
        }

        public String[] build(Collection<String> grammars)
        {
            List<String> result = new ArrayList<>(arguments);
            result.addAll(grammars);

            return result.toArray(new String[result.size()]);
        }


        public String[] build(Namespace namespace, Collection<Grammar> grammars)
        {
            List<String> result = new ArrayList<>(arguments);

            List<Grammar> headers = new ArrayList<>(grammars.size());

            for (Grammar grammar : grammars)
            {
                if (grammar.namespace.isHeader())
                {
                    headers.add(grammar);
                }
            }

            if (!packageAttribute)
            {
                // we can only add the -package option if no grammar defines a namespace
                if (headers.isEmpty() && !namespace.isEmpty())
                {
                    result.add("-package");
                    result.add(namespace.id);
                }
            }

            result.addAll(paths(grammars));

            return result.toArray(new String[result.size()]);
        }


        private List<String> paths(Collection<Grammar> grammars)
        {
            List<String> result = new ArrayList<>(grammars.size());

            for (Grammar grammar : grammars)
            {
                result.add(grammar.path.toString());
            }

            return result;
        }
    }
}
