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
        this(false);
    }

    /**
     * Creates a new TestWorkspace object.
     *
     * @param  empty  {@code true} to indicate that no default WORKSPACE file should be
     *                created.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    public TestWorkspace(boolean empty) throws IOException
    {
        Path examples = Projects.path("examples");

        assertTrue(Files.exists(examples));

        root = examples;

        Path workspace = examples.resolve("WORKSPACE");

        if (!empty && Files.notExists(workspace))
        {
            // we can't use the workspace file when running under Bazel as the linked
            // folder structure is slightly different and the path to the local
            // repository would be wrong
            String contents = "workspace(name=\"examples\")\n"
                + "load(\"@bazel_tools//tools/build_defs/repo:http.bzl\", \"http_archive\")\n"
                + "local_repository(\n"
                + "    name = \"rules_antlr\",\n"
                + "    path = \"../../../rules_antlr\",\n" + ")\n"
                + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
                + "rules_antlr_dependencies(2, 3, 4, \"Cpp\", \"Go\", \"Python\", \"Python2\")\n"
                + "http_archive(\n"
                + "    name = \"io_bazel_rules_go\",\n"
                + "    urls = [\n"
                + "        \"https://storage.googleapis.com/bazel-mirror/github.com/bazelbuild/rules_go/releases/download/v0.20.3/rules_go-v0.20.3.tar.gz\",\n"
                + "        \"https://github.com/bazelbuild/rules_go/releases/download/v0.20.3/rules_go-v0.20.3.tar.gz\",\n"
                + "    ],\n"
                + "    sha256 = \"e88471aea3a3a4f19ec1310a55ba94772d087e9ce46e41ae38ecebe17935de7b\",\n"
                + ")\n"
                + "load(\"@io_bazel_rules_go//go:deps.bzl\", \"go_rules_dependencies\", \"go_register_toolchains\")\n"
                + "go_rules_dependencies()\n"
                + "go_register_toolchains()\n"
                + "http_archive(\n"
                + "    name = \"rules_python\",\n"
                + "    sha256 = \"aa96a691d3a8177f3215b14b0edc9641787abaaa30363a080165d06ab65e1161\",\n"
                + "    url = \"https://github.com/bazelbuild/rules_python/releases/download/0.0.1/rules_python-0.0.1.tar.gz\",\n"
                + ")\n"
                + "load(\"@rules_python//python:repositories.bzl\", \"py_repositories\")\n"
                + "py_repositories()\n";

            Files.write(workspace, contents.getBytes(StandardCharsets.UTF_8));
        }
    }

    public Path file(String path, String contents) throws IOException
    {
        Path file = root.resolve(path);
        Files.write(file, contents.getBytes(StandardCharsets.UTF_8));
        return file;
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
