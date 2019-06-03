package org.antlr.bazel;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Fetches dependencies.
 *
 * @author  Marco Hunsicker
 */
class Dependencies
{
    private static final Map<Version, String[]> DEPENDENCIES = download();

    /**
     * Returns the ANTLR 2 dependencies.
     *
     * @return  the ANTLR 2 dependencies.
     */
    public static String[] antlr2()
    {
        return DEPENDENCIES.get(Version.V2);
    }


    /**
     * Returns the ANTLR 3 dependencies.
     *
     * @return  the ANTLR 3 dependencies.
     */
    public static String[] antlr3()
    {
        return DEPENDENCIES.get(Version.V3);
    }


    /**
     * Returns the ANTLR 4 dependencies.
     *
     * @return  the ANTLR 4 dependencies.
     */
    public static String[] antlr4()
    {
        return DEPENDENCIES.get(Version.V4);
    }


    private static Map<Version, String[]> download()
    {
        try
        {
            TestWorkspace workspace = new TestWorkspace();
            Path output = workspace.path("output_base");

            Map<Version, String[]> deps = loadDependencies(output);

            // if short-circuiting did not work, fetch the dependencies
            if (deps.isEmpty())
            {
                fetchDependencies(workspace.root, output);
                deps = loadDependencies(output);
            }

            assertFalse(deps.isEmpty());

            return deps;
        }
        catch (Exception ex)
        {
            throw new IllegalStateException("Failed to load dependencies", ex);
        }
    }


    private static void fetchDependencies(Path workspace, Path base) throws Exception
    {
        Process p = new ProcessBuilder().command("bazel", "fetch", "//...")
            .directory(Paths.get(".").toRealPath().toFile())
            .inheritIO()
            .directory(workspace.toFile())
            .start();
        p.waitFor();

        if (p.exitValue() != 0)
        {
            // cleanup if something went wrong to make short-circuiting reliable
            Path external = base.resolve("external");

            if (Files.exists(base))
            {
                Disk.delete(external);
            }
        }

        assertEquals(0, p.exitValue());
    }


    private static Map<Version, String[]> loadDependencies(final Path base)
        throws IOException
    {
        Collection<String> deps2 = new ArrayList<>();
        Collection<String> deps3 = new ArrayList<>();
        Collection<String> deps4 = new ArrayList<>();

        Path external = base.resolve("external");

        if (Files.exists(external))
        {
            Files.walkFileTree(external, new SimpleFileVisitor<Path>()
                {
                    PathMatcher archives = base.getFileSystem()
                        .getPathMatcher("glob:**/*.jar");
                    PathMatcher antlr4 = base.getFileSystem()
                        .getPathMatcher(
                            "regex:.*(antlr._tool|antlr._runtime|stringtemplate4|javax_json)/jar/downloaded.jar");
                    PathMatcher antlr3 = base.getFileSystem()
                        .getPathMatcher("regex:.*/(antlr3_tool|antlr3_runtime|stringtemplate4)/jar/downloaded.jar");
                    PathMatcher antlr2 = base.getFileSystem()
                        .getPathMatcher("glob:**/antlr2/jar/downloaded.jar");

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException
                    {
                        if (archives.matches(file)
                            && Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS))
                        {
                            if (antlr4.matches(file))
                            {
                                deps4.add(file.toString());
                            }

                            if (antlr3.matches(file))
                            {
                                deps3.add(file.toString());
                            }

                            if (antlr2.matches(file))
                            {
                                deps2.add(file.toString());
                            }
                        }

                        return super.visitFile(file, attrs);
                    }
                });

            Map<Version, String[]> result = new HashMap<>();
            result.put(Version.V2, deps2.toArray(new String[deps2.size()]));
            result.put(Version.V3, deps3.toArray(new String[deps3.size()]));
            result.put(Version.V4, deps4.toArray(new String[deps4.size()]));

            return result;
        }

        return Collections.emptyMap();
    }
}
