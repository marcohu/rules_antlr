package org.antlr.bazel;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;


/**
 * Defines a test project.
 *
 * @author  Marco Hunsicker
 */
class TestProject implements Closeable
{
    private static final CopyOption[] ATTRIBUTES =
        { StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };

    private final String name;
    private final Path outputDirectory;
    private final Path root;

    /**
     * Creates a new TestProject object.
     *
     * @param   project  the project name.
     * @param   copy     if [@code true] the project files are copied into the test
     *                   directory. Otherwise they are sym-linked.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    private TestProject(String project, boolean copy) throws IOException
    {
        this.root = Files.createTempDirectory("antlr-bazel-test-");
        this.name = root.getFileName().toString();

        if (copy)
        {
            copy(Paths.get(project).toRealPath(), root);
        }
        else
        {
            Path target = Paths.get(project).resolve("src").toRealPath();
            Path link = root.resolve("src");
            Files.createSymbolicLink(link, target);
        }

        outputDirectory = Files.createDirectories(root.resolve("target"));
        Files.createDirectories(root.resolve("lib"));
    }

    /**
     * Creates a new test project.
     *
     * @param   project  the project name.
     *
     * @return  the test project.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    public static TestProject create(String project) throws IOException
    {
        return create(project, false);
    }


    /**
     * Creates a new test project.
     *
     * @param   project  the project name.
     * @param   copy     if [@code true] the project files are copied into the test
     *                   directory. Otherwise they are sym-linked.
     *
     * @return  the test project.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    public static TestProject create(String project, boolean copy) throws IOException
    {
        System.setProperty("ANTLR_DO_NOT_EXIT", "true");

        return new TestProject(project, copy);
    }


    @Override
    public void close() throws IOException
    {
        if (Files.exists(root))
        {
            delete(root);
        }
    }


    String[] antlr2() throws IOException
    {
        return classpath("src/it/lib/antlr-2.7.7/antlr-2.7.7.jar");
    }


    String[] antlr4() throws IOException
    {
        return classpath("src/it/lib/antlr-4.7.1/antlr4-4.7.1.jar",
            "src/it/lib/antlr-4.7.1/antlr4-runtime-4.7.1.jar",
            "src/it/lib/antlr-3.5.2/antlr-runtime-3.5.2.jar",
            "src/it/lib/org.abego.treelayout.core-1.0.3.jar",
            "src/it/lib/javax.json-1.0.4.jar",
            "src/it/lib/ST4-4.0.8.jar");
    }

    String[] antlr3() throws IOException
    {
        return classpath(
            "src/it/lib/antlr-3.5.2/antlr-runtime-3.5.2.jar",
            "src/it/lib/antlr-3.5.2/antlr-3.5.2.jar",
            "src/it/lib/ST4-4.0.8.jar");
    }


    String[] args(String... args)
    {
        List<String> a = new ArrayList<>();

        // we always want the output in the output directory
        a.addAll(Arrays.asList("-o", outputDirectory.toString()));
        a.addAll(Arrays.asList(args));

        return a.toArray(new String[0]);
    }


    String[] grammars() throws IOException
    {
        final List<String> result = new ArrayList<>();

        Files.walkFileTree(root,
            EnumSet.of(FileVisitOption.FOLLOW_LINKS),
            Integer.MAX_VALUE,
            new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
                {
                    if (file.getFileSystem()
                        .getPathMatcher("glob:**/*.g{3,4,}")
                        .matches(file))
                    {
                        result.add(file.toAbsolutePath().toString());
                    }

                    return FileVisitResult.CONTINUE;
                }
            });

        return result.toArray(new String[0]);
    }


    Path outputDirectory()
    {
        return outputDirectory;
    }


    Path relative(Path link)
    {
        return root.relativize(link);
    }


    Path resolve(String string)
    {
        return root.resolve(string);
    }


    Path root()
    {
        return root;
    }


    Path srcjar()
    {
        return outputDirectory.resolve(name + ".srcjar");
    }


    /**
     * Verifies that the given paths exists in the .srcjar.
     *
     * @param   paths  the path to verify.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    void validate(String... paths) throws IOException
    {
        Path srcjar = srcjar();

        assertTrue(Files.exists(srcjar));

        URI uri = URI.create("jar:file:" + srcjar.toUri().getPath());

        try (FileSystem fs = FileSystems.newFileSystem(uri, new HashMap<String, String>()))
        {
            for (String path : paths)
            {
                if (Files.notExists(fs.getPath(path)))
                {
                    throw new AssertionError("Path does not exist: " + path);
                }
            }
        }
    }


    private static void copy(final Path path, final Path target, CopyOption... options)
        throws IOException
    {
        final CopyOption[] opt = (options.length == 0) ? ATTRIBUTES : options;

        if (Files.isDirectory(path))
        {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>()
                {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir,
                        BasicFileAttributes attrs) throws IOException
                    {
                        Files.createDirectories(
                            target.resolve(path.relativize(dir).toString()));

                        return FileVisitResult.CONTINUE;
                    }


                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException
                    {
                        Files.copy(file,
                            target.resolve(path.relativize(file).toString()),
                            opt);

                        return FileVisitResult.CONTINUE;
                    }
                });
        }
        else
        {
            Files.copy(path, target, opt);
        }
    }


    private static void delete(Path path) throws IOException
    {
        if (Files.isDirectory(path))
        {
            Files.walkFileTree(path, DeleteVisitor.INSTANCE);
        }
        else if (Files.exists(path))
        {
            Files.delete(path);
        }
    }


    private String[] classpath(String... libs) throws IOException
    {
        List<String> result = new ArrayList<>();

        for (String lib : libs)
        {
            Path target = Paths.get(lib).toRealPath();
            Path link = root.resolve("lib").resolve(target.getFileName());

            Files.createSymbolicLink(link, target);

            result.add(target.toString());
        }

        return result.toArray(new String[0]);
    }

    private static class DeleteVisitor extends SimpleFileVisitor<Path>
    {
        public static final DeleteVisitor INSTANCE = new DeleteVisitor();

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException cause)
            throws IOException
        {
            Files.delete(dir);

            return FileVisitResult.CONTINUE;
        }


        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException
        {
            Files.delete(file);

            return FileVisitResult.CONTINUE;
        }
    }
}
