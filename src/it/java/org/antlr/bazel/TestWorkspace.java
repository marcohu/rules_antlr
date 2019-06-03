package org.antlr.bazel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Our workspace for testing.
 *
 * @author  Marco Hunsicker
 */
class TestWorkspace
{
    /** The workspace root. */
    public final Path root;

    /**
     * Creates a new TestWorkspace object.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    public TestWorkspace() throws IOException
    {
        Path examples = Projects.path("examples");

        assertTrue(Files.exists(examples));

        root = examples;

        Path workspace = examples.resolve("WORKSPACE");

        if (Files.notExists(workspace))
        {
            // we can't use the workspace file when running under Bazel as the linked
            // folder structure is slightly different and the path to the local
            // repository would be wrong
            String contents = "workspace(name=\"examples\")\n" + "local_repository(\n"
                + "    name = \"rules_antlr\",\n"
                + "    path = \"../../../rules_antlr\",\n" + ")\n"
                + "load(\"@rules_antlr//antlr:deps.bzl\", \"antlr_dependencies\")\n"
                + "antlr_dependencies(2, 3, 4)";
            Files.write(workspace, contents.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Returns the absolute path of the given info key.
     *
     * @param   key  the info key.
     *
     * @return  the path.
     *
     * @throws  Exception  if an error occurred.
     */
    public Path path(String key) throws Exception
    {
        Process p = new ProcessBuilder().command("bazel", "info", key)
            .redirectErrorStream(true)
            .directory(root.toFile())
            .start();

        try (Stream<String> lines = new BufferedReader(
                new InputStreamReader(p.getInputStream())).lines())
        {
            String line = lines.reduce((first, second) -> second).orElse(null);

            Path path = Paths.get(line);

            assertTrue(Files.exists(path));

            return path;
        }
        finally
        {
            p.waitFor();
            assertEquals(0, p.exitValue());
        }
    }
}
