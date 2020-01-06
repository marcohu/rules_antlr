package org.antlr.bazel;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;


/**
 * Bazel test support.
 *
 * @author  Marco Hunsicker
 */
class BazelTestSupport
{
    /**
     * Asserts that the given paths exists in the given .srcjar.
     *
     * @param   srcjar  the .srcjar.
     * @param   paths   the paths.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    protected void assertContents(Path srcjar, String... paths) throws IOException
    {
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


    /**
     * Builds the given target in the examples workspace.
     *
     * @param   target  the target to build.
     *
     * @return  the path to the bazel-bin directory of the workspace.
     *
     * @throws  Exception  if an error occurred.
     */
    protected Path build(String target) throws Exception
    {
        TestWorkspace workspace = new TestWorkspace();

        Command c = new Command(workspace.root, target).build();
        assertEquals(c.output(), 0, c.exitValue());

        return workspace.path("bazel-bin");
    }
}
