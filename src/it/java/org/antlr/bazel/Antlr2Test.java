package org.antlr.bazel;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;


/**
 * Antlr 2 tests.
 *
 * @author  Marco Hunsicker
 */
public class Antlr2Test
{
    @Test
    public void basic() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr2/Calc"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("2")
                .encoding("")
                .classpath(project.antlr2())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("calc/CalcLexer.java",
                "calc/CalcLexer.smap",
                "calc/CalcParser.java",
                "calc/CalcParser.smap",
                "calc/CalcParserTokenTypes.java",
                "calc/CalcParserTokenTypes.txt",
                "calc/CalcTreeWalker.java",
                "calc/CalcTreeWalker.smap");
        }
    }


    @Test
    public void invalidGrammar() throws Exception
    {
        try (TestProject project = TestProject.create(
                "src/it/resources/antlr2/InvalidGrammar"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("2")
                .classpath(project.antlr2())
                .outputDirectory(project.outputDirectory().toString())
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            fail("Expected InvocationTargetException");
        }
        catch (InvocationTargetException ex)
        {
            assertEquals("ANTLR Panic: Exiting due to errors.",
                ex.getCause().getMessage());
        }
    }


    @Test
    public void missingSuppergrammar() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr2/InheritTinyC",
                true))
        {
            String r = null;

            try (TestProject ref = TestProject.create("examples/antlr2/TinyC", true))
            {
                AntlrRules.create(ref.root())
                    .srcjar(ref.srcjar().toString())
                    .version("2")
                    .classpath(ref.antlr2())
                    .outputDirectory(ref.outputDirectory().toString())
                    .grammars("src/main/antlr2/lexer.g", "src/main/antlr2/tinyc.g")
                    .args(ref.args())
                    .generate();

                Path target = ref.srcjar();
                Path link = project.outputDirectory().resolve(target.getFileName());

                Files.createSymbolicLink(link, target);

                AntlrRules.create(project.root())
                    .srcjar(project.srcjar().toString())
                    .version("2")
                    .classpath(project.antlr2())
                    .outputDirectory(project.outputDirectory().toString())
                    .grammars("src/main/antlr2/subc.g")
                    .args(
                            project.args("-glib",
                                r = ref.root()
                                        .resolve("src/main/antlr2/tinyc.g")
                                        .toString()))
                    .generate();

                fail("Expected IllegalArgumentException");
            }
            catch (IllegalArgumentException ex)
            {
                assertEquals(
                    String.format(
                        "You have to provide the .srcjar created for '%s' as well",
                        r),
                    ex.getMessage());
            }
        }
    }


    @Test
    public void separatedLexerParser() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr2/TinyC"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("2")
                .classpath(project.antlr2())
                .outputDirectory(project.outputDirectory().toString())
                .grammars("src/main/antlr2/lexer.g", "src/main/antlr2/tinyc.g")
                .args(project.args())
                .generate();

            project.validate("tinyc/TinyCLexer.java",
                "tinyc/TinyCLexer.smap",
                "tinyc/TinyCParser.java",
                "tinyc/TinyCParser.smap",
                "tinyc/TinyCTokenTypes.java",
                "tinyc/TinyCTokenTypes.txt",
                "tinyc/TinyCParserTokenTypes.java",
                "tinyc/TinyCParserTokenTypes.txt");
        }
    }


    @Test
    public void supergrammar() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr2/InheritTinyC",
                true))
        {
            try (TestProject ref = TestProject.create("examples/antlr2/TinyC", true))
            {
                AntlrRules.create(ref.root())
                    .srcjar(ref.srcjar().toString())
                    .version("2")
                    .classpath(ref.antlr2())
                    .outputDirectory(ref.outputDirectory().toString())
                    .grammars("src/main/antlr2/lexer.g", "src/main/antlr2/tinyc.g")
                    .args(ref.args())
                    .generate();

                Path target = ref.srcjar();
                Path link = project.outputDirectory().resolve(target.getFileName());

                Files.createSymbolicLink(link, target);

                AntlrRules.create(project.root())
                    .srcjar(project.srcjar().toString())
                    .version("2")
                    .classpath(project.antlr2())
                    .outputDirectory(project.outputDirectory().toString())
                    .grammars("src/main/antlr2/subc.g")
                    .args(
                            project.args("-glib",
                                ref.root().resolve("src/main/antlr2/tinyc.g;")
                                + project.relative(link).toString()))
                    .generate();
            }

            project.validate("MyCParserTokenTypes.txt",
                "MyCParser.smap",
                "MyCParser.java",
                "MyCParserTokenTypes.java");
        }
    }
}
