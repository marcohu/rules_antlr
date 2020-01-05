package org.antlr.bazel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;


/**
 * Dependency loading tests.
 *
 * @author  Marco Hunsicker
 */
public class RepositoriesTest
{
    @Test
    public void missingVersion() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies()";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Calc/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute versionsAndLanguages: Missing ANTLR version"));
    }

    @Test
    public void languageAndMissingVersion() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies(\"Java\")";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Calc/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute versionsAndLanguages: Missing ANTLR version"));
    }

    @Test
    public void unsupportedVersion() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies(\"4.0\")";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Calc/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute versionsAndLanguages: Unsupported ANTLR version provided: \"4.0\"."));

        contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_optimized_dependencies\")\n"
            + "rules_antlr_optimized_dependencies(\"4.0\")";

        workspace.file("WORKSPACE", contents);
        c = new Command(workspace.root, "//antlr4/HelloWorld/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute version: Unsupported ANTLR version provided: \"4.0\"."));
    }

    @Test
    public void invalidVersion() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies(471)";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Calc/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute versionsAndLanguages: Integer version '471' no longer valid. Use semantic version \"4.7.1\" instead."));

        contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_optimized_dependencies\")\n"
            + "rules_antlr_optimized_dependencies(471)";

        workspace.file("WORKSPACE", contents);
        c = new Command(workspace.root, "//antlr4/HelloWorld/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute version: Integer version '471' no longer valid. Use semantic version \"4.7.1\" instead."));
    }

    @Test
    public void invalidLanguage() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies(4, \"Haskell\")";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Calc/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute versionsAndLanguages: Invalid language provided: \"Haskell\"."));
    }

    @Test
    public void severalVersions() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies(\"4.7.1\", \"4.7.2\")";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Calc/...").build();
        assertEquals(c.output(), 1, c.exitValue());
        assertTrue(c.output().contains("attribute versionsAndLanguages: You can only load one version from ANTLR 4. You specified both \"4.7.1\" and \"4.7.2\"."));
    }

    @Test
    public void missingLanguage() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies(\"2.7.7\")";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Cpp/...").build();
        assertEquals(c.output(), 1, c.exitValue());
    }

    @Test
    public void alwaysLoadJavaDependencies() throws Exception
    {
        String contents = "workspace(name=\"examples\")\n"
            + "local_repository(\n"
            + "    name = \"rules_antlr\",\n"
            + "    path = \"../../../rules_antlr\",\n"
            + ")\n"
            + "load(\"@rules_antlr//antlr:repositories.bzl\", \"rules_antlr_dependencies\")\n"
            + "rules_antlr_dependencies(\"2.7.7\", \"Cpp\")";

        TestWorkspace workspace = new TestWorkspace(true);
        workspace.file("WORKSPACE", contents);
        Command c = new Command(workspace.root, "//antlr2/Cpp/...").build();
        assertEquals(c.output(), 0, c.exitValue());
    }
}
