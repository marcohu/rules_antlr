package org.antlr.bazel;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;


/**
 * Defines a temporary test project.
 *
 * @author  Marco Hunsicker
 */
class TestProject implements Closeable
{
    private final String name;
    private final Path outputDirectory;
    private final Path root;

    /**
     * Creates a new TestProject object.
     *
     * @param   project  the project name.
     * @param   copy     if {@code true} the project files are copied into the test
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
            Disk.copy(Projects.path(project), root);
        }
        else
        {
            Path target = Projects.path(project).resolve("src");
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
            Disk.delete(root);
        }
    }


    String[] antlr2()
    {
        return Dependencies.antlr2();
    }


    String[] antlr3()
    {
        return Dependencies.antlr3();
    }


    String[] antlr4()
    {
        return Dependencies.antlr4();
    }


    String[] args(String... args)
    {
        List<String> a = new ArrayList<>();

        // we always want the output in the output directory
        a.addAll(Arrays.asList("-o", outputDirectory.toString()));
        a.addAll(Arrays.asList(args));

        return a.toArray(new String[0]);
    }


    String[] grammars(String... exclusions) throws IOException
    {
        List<String> result = new ArrayList<>();

        Files.walkFileTree(root,
            EnumSet.of(FileVisitOption.FOLLOW_LINKS),
            Integer.MAX_VALUE,
            new SimpleFileVisitor<Path>()
            {
                PathMatcher grammar = root.getFileSystem()
                        .getPathMatcher("glob:**/*.g{3,4,}");
                Set<String> excludes = new HashSet<>(Arrays.asList(exclusions));

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
                {
                    if (grammar.matches(file)
                        && !excludes.contains(file.getFileName().toString()))
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
                    throw new AssertionError(
                        String.format("Path does not exist: %s. Archive contains: %s",
                            path,
                            contents(fs.getPath("/"))));
                }
            }
        }
    }


    private String contents(Path root) throws IOException
    {
        List<String> paths = new ArrayList<>();

        Files.walkFileTree(root,
            EnumSet.of(FileVisitOption.FOLLOW_LINKS),
            Integer.MAX_VALUE,
            new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
                {
                    paths.add(root.relativize(file).toString());

                    return FileVisitResult.CONTINUE;
                }
            });

        Collections.sort(paths);

        return paths.stream().collect(Collectors.joining("\n"));
    }
}
