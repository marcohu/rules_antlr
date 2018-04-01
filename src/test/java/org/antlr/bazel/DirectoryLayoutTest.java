package org.antlr.bazel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.antlr.bazel.DirectoryLayout;
import org.antlr.bazel.Language;
import org.junit.Test;


/**
 * Tests for {@link DirectoryLayout}.
 *
 * @author  Marco Hunsicker
 */
public class DirectoryLayoutTest
{
    @Test
    public void getRelativePath() throws IOException
    {
        DirectoryLayout directory = Language.JAVA.getLayout();
        unix("", directory, "src/main/antlr/Test.java");
        unix("", directory, "src/main/antlr2/Test.java");
        unix("", directory, "src/main/antlr3/Test.java");
        unix("", directory, "src/main/antlr4/Test.java");
        unix("a", directory, "src/main/antlr/a/Test.java");
        unix("a/b", directory, "src/main/antlr2/a/b/Test.java");
        unix("a/b/c", directory, "src/main/antlr3/a/b/c/Test.java");
        unix("a/b/c/d", directory, "src/main/antlr4/a/b/c/d/Test.java");

        windows("", directory, "src\\main\\antlr\\Test.java");
        windows("", directory, "src\\main\\antlr2\\Test.java");
        windows("", directory, "src\\main\\antlr3\\Test.java");
        windows("", directory, "src\\main\\antlr4\\Test.java");
        windows("a", directory, "src\\main\\antlr\\a\\Test.java");
        windows("a", directory, "src\\main\\antlr\\a\\Test.java");
        windows("a\\b", directory, "src\\main\\antlr2\\a\\b\\Test.java");
        windows("a\\b\\c", directory, "src\\main\\antlr3\\a\\b\\c\\Test.java");
        windows("a\\b\\c\\d", directory, "src\\main\\antlr4\\a\\b\\c\\d\\Test.java");

        directory = new DirectoryLayout("src/grammars");
        unix("", directory, "src/grammars/Test.java");
        unix("a", directory, "src/grammars/a/Test.java");
        unix("a/b", directory, "src/grammars/a/b/Test.java");
        unix("a/b/c", directory, "src/grammars/a/b/c/Test.java");
        unix("a/b/c/d", directory, "src/grammars/a/b/c/d/Test.java");
        windows("", directory, "src\\grammars\\Test.java");
        windows("a", directory, "src\\grammars\\a\\Test.java");
        windows("a\\b", directory, "src\\grammars\\a\\b\\Test.java");
        windows("a\\b\\c", directory, "src\\grammars\\a\\b\\c\\Test.java");
        windows("a\\b\\c\\d", directory, "src\\grammars\\a\\b\\c\\d\\Test.java");

        directory = new DirectoryLayout("/src/grammars/");
        assertEquals(Paths.get(""), directory.getRelativePath(Paths.get(".")));

        directory = new DirectoryLayout("src/grammars/");

        directory = new DirectoryLayout("src\\grammars");
        unix("", directory, "src/grammars/Test.java");
        unix("a", directory, "src/grammars/a/Test.java");
        unix("a/b", directory, "src/grammars/a/b/Test.java");
        unix("a/b/c", directory, "src/grammars/a/b/c/Test.java");
        unix("a/b/c/d", directory, "src/grammars/a/b/c/d/Test.java");
        windows("", directory, "src\\grammars\\Test.java");
        windows("a", directory, "src\\grammars\\a\\Test.java");
        windows("a\\b", directory, "src\\grammars\\a\\b\\Test.java");
        windows("a\\b\\c", directory, "src\\grammars\\a\\b\\c\\Test.java");
        windows("a\\b\\c\\d", directory, "src\\grammars\\a\\b\\c\\d\\Test.java");

        directory = new DirectoryLayout("src\\grammars\\");
        directory = new DirectoryLayout("\\src\\grammars\\");

        directory = Language.CPP.getLayout();
        unix("", directory, "src/antlr/Test.java");
        unix("", directory, "src/antlr2/Test.java");
        unix("", directory, "src/antlr3/Test.java");
        unix("", directory, "src/antlr4/Test.java");
        unix("a", directory, "src/antlr/a/Test.java");
        unix("a/b", directory, "src/antlr2/a/b/Test.java");
        unix("a/b/c", directory, "src/antlr3/a/b/c/Test.java");
        unix("a/b/c/d", directory, "src/antlr4/a/b/c/d/Test.java");

        windows("", directory, "src\\antlr\\Test.java");
        windows("", directory, "src\\antlr2\\Test.java");
        windows("", directory, "src\\antlr3\\Test.java");
        windows("", directory, "src\\antlr4\\Test.java");
        windows("a", directory, "src\\antlr\\a\\Test.java");
        windows("a", directory, "src\\antlr\\a\\Test.java");
        windows("a\\b", directory, "src\\antlr2\\a\\b\\Test.java");
        windows("a\\b\\c", directory, "src\\antlr3\\a\\b\\c\\Test.java");
        windows("a\\b\\c\\d", directory, "src\\antlr4\\a\\b\\c\\d\\Test.java");

        directory = new DirectoryLayout("flat");
        unix("", directory, "src/antlr/Test.java");
        windows("", directory, "src\\antlr\\Test.java");
    }


    @Test
    public void tostring()
    {
        assertEquals(".*[\\\\/]src[\\\\/]main[\\\\/]antlr[234]?[\\\\/](.*)",
            Language.JAVA.getLayout().toString());
    }


    private void test(String expected,
        DirectoryLayout dir,
        String path,
        Configuration config) throws IOException
    {
        try (FileSystem fs = Jimfs.newFileSystem(config))
        {
            Path file = fs.getPath("work").resolve(path);
            Path directory = Files.createDirectories(file.getParent());
            Files.write(file, "".getBytes(StandardCharsets.UTF_8));
            assertTrue(Files.exists(directory));
            assertEquals(expected, dir.getRelativePath(file).toString());
            assertEquals(fs.getPath(expected), dir.getRelativePath(file));
        }
    }


    private void unix(String expected, DirectoryLayout dir, String path)
        throws IOException
    {
        test(expected, dir, path, Configuration.unix());
    }


    private void windows(String expected, DirectoryLayout dir, String path)
        throws IOException
    {
        test(expected, dir, path, Configuration.windows());
    }
}
