package org.antlr.bazel;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;


/**
 * ANTLR 3 tests.
 *
 * @author  Marco Hunsicker
 */
public class Antlr3Test extends BazelTestSupport
{
    @Test
    public void basic() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr3/SimpleC"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("3")
                .classpath(project.antlr3())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("SimpleCLexer.java", "SimpleCParser.java", "SimpleC.tokens");
        }
    }


    @Test
    public void detectLanguage() throws Exception
    {
        Path bin = build("//antlr3/DetectLanguage");
        Path srcjar = bin.resolve("antlr3/DetectLanguage/csharp.srcjar");

        assertContents(srcjar,
            "Antlr/Examples/HoistedPredicates//TLexer.cs",
            "Antlr/Examples/HoistedPredicates//TParser.cs",
            "Antlr/Examples/HoistedPredicates//T.tokens");
    }


    @Test
    public void inheritFromLibFolder() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr3/InheritLibFolder"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("3")
                .classpath(project.antlr3())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars())
                .args(project.args("-lib", "src/main/antlr3/lib"))
                .generate();

            project.validate("Simple_CommonLexer.java",
                "SimpleLexer.java",
                "SimpleParser.java",
                "Simple.tokens");
        }
    }


    @Test
    public void inheritFromSameFolder() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr3/InheritSameFolder"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("3")
                .classpath(project.antlr3())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars("CommonLexer.g"))
                .args(project.args())
                .generate();

            project.validate("Simple_CommonLexer.java",
                "SimpleLexer.java",
                "SimpleParser.java",
                "Simple.tokens");
        }
    }


    @Test
    public void saveLexer() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr3/InheritLibFolder"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("3")
                .classpath(project.antlr3())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars("CommonLexer.g"))
                .args(project.args("-lib", "src/main/antlr3/lib", "-XsaveLexer"))
                .generate();

            project.validate("Simple_CommonLexer.java",
                "SimpleLexer.java",
                "CommonLexer.tokens",
                "SimpleParser.java",
                "Simple.tokens");
        }
    }


    @Test
    public void severalErrors() throws Exception
    {
        try (TestProject project = TestProject.create(
                "src/it/resources/antlr3/SeveralErrors"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("3")
                .classpath(project.antlr3())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            assertTrue(Files.exists(project.srcjar()));

            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException ex)
        {
            assertEquals("ANTLR terminated with 7 errors", ex.getMessage());
        }
    }


    @Test
    public void singleError() throws Exception
    {
        try (TestProject project = TestProject.create(
                "src/it/resources/antlr3/SingleError"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("3")
                .classpath(project.antlr3())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            assertTrue(Files.exists(project.srcjar()));

            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException ex)
        {
            assertEquals("ANTLR terminated with 1 error", ex.getMessage());
        }
    }
}
