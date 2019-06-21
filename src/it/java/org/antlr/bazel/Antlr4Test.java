package org.antlr.bazel;

import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;


/**
 * ANTLR 4 tests.
 *
 * @author  Marco Hunsicker
 */
public class Antlr4Test extends BazelTestSupport
{
    @Test
    public void detectLanguage() throws Exception
    {
        Path bin = build("//antlr4/DetectLanguage:cpp");
        Path srcjar = bin.resolve("antlr4/DetectLanguage/cpp.srcjar");

        assertContents(srcjar,
            "Cpp.interp",
            "Cpp.tokens",
            "CppBaseListener.cpp",
            "CppBaseListener.h",
            "CppLexer.cpp",
            "CppLexer.h",
            "CppLexer.interp",
            "CppLexer.tokens",
            "CppListener.cpp",
            "CppListener.h",
            "CppParser.cpp",
            "CppParser.h");
    }


    @Test
    public void inheritLibFolder() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/InheritLibFolder"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars("G3.g4"))
                .namespace("a")
                .args(project.args("-package", "a", "-lib", "src/main/antlr4/imports"))
                .generate();

            project.validate("a/G1.interp",
                "a/G1.tokens",
                "a/G1BaseListener.java",
                "a/G1Listener.java",
                "a/G1Parser.java",
                "a/G2.interp",
                "a/G2.tokens",
                "a/G2BaseListener.java",
                "a/G2Listener.java",
                "a/G2Parser.java");
        }
    }


    @Test
    public void inheritSameFolder() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/InheritSameFolder"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .namespace("a")
                .args(project.args("-package", "a"))
                .generate();

            project.validate("a/NestedListener.java",
                "a/G1BaseListener.java",
                "a/NestedBaseListener.java",
                "a/G1Listener.java",
                "a/Nested.tokens",
                "a/G1.tokens",
                "a/Nested.interp",
                "a/G2.interp",
                "a/G1.interp",
                "a/G1Parser.java",
                "a/G2BaseListener.java",
                "a/G2Parser.java",
                "a/G2.tokens",
                "a/NestedParser.java");
        }
    }


    @Test
    public void invalidClasspath() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/HelloWorld"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(new String[] { "src/it/lib/", "antlr4-runtime.jar" })
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .namespace("")
                .language("")
                .layout("")
                .args(project.args())
                .generate();

            assertTrue(Files.exists(project.srcjar()));

            fail("Expected FileNotFoundException");
        }
        catch (FileNotFoundException ex)
        {
            assertEquals("antlr4-runtime.jar", ex.getMessage());
        }
    }


    @Test
    public void invalidGrammar() throws Exception
    {
        try (TestProject project = TestProject.create(
                "src/it/resources/antlr4/SeveralErrors"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .language("")
                .layout("")
                .namespace("")
                .args(project.args())
                .generate();

            fail();
        }
        catch (IllegalStateException ex)
        {
            assertEquals("ANTLR terminated with 2 errors", ex.getMessage());
        }

        try (TestProject project = TestProject.create(
                "src/it/resources/antlr4/SingleError"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .namespace("foo.bar")
                .args(project.args())
                .generate();

            fail();
        }
        catch (IllegalStateException ex)
        {
            assertEquals("ANTLR terminated with 1 error", ex.getMessage());
        }
    }


    @Test
    public void languageAttributeOverOptions() throws Exception
    {
        try (TestProject project = TestProject.create(
                "examples/antlr4/LanguageByAttribute"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .language("Swift")
                .grammars(project.grammars())
                .args(project.args("-Dlanguage=Swift"))
                .generate();

            project.validate("HelloLexer.tokens",
                "HelloBaseListener.swift",
                "HelloLexer.swift",
                "Hello.tokens",
                "HelloLexerATN.swift",
                "HelloParser.swift",
                "HelloParserATN.swift",
                "HelloLexer.interp",
                "HelloListener.swift",
                "Hello.interp");
        }
    }


    @Test
    public void languageFromOptions() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/LanguageByOption"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("HelloParser.h",
                "HelloLexer.tokens",
                "HelloBaseListener.h",
                "Hello.tokens",
                "HelloParser.cpp",
                "HelloLexer.cpp",
                "HelloLexer.h",
                "HelloLexer.interp",
                "HelloListener.h",
                "HelloBaseListener.cpp",
                "Hello.interp",
                "HelloListener.cpp");
        }
    }


    @Test
    public void languageGrammar() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/DetectLanguage"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("Cpp.interp",
                "Cpp.tokens",
                "CppBaseListener.cpp",
                "CppBaseListener.h",
                "CppLexer.cpp",
                "CppLexer.h",
                "CppLexer.interp",
                "CppLexer.tokens",
                "CppListener.cpp",
                "CppListener.h",
                "CppParser.cpp",
                "CppParser.h",
                "CSharp.interp",
                "CSharp.tokens",
                "CSharpBaseListener.cs",
                "CSharpLexer.cs",
                "CSharpLexer.interp",
                "CSharpLexer.tokens",
                "CSharpListener.cs",
                "CSharpParser.cs",
                "go_base_listener.go",
                "go_lexer.go",
                "go_listener.go",
                "go_parser.go",
                "goau_lexer.go",
                "goau_parser.go",
                "goauparser_base_listener.go",
                "goauparser_listener.go",
                "gos_base_listener.go",
                "gos_lexer.go",
                "gos_listener.go",
                "gos_parser.go",
                "java/Java.interp",
                "java/Java.tokens",
                "java/JavaBaseListener.java",
                "java/JavaLexer.interp",
                "java/JavaLexer.java",
                "java/JavaLexer.tokens",
                "java/JavaListener.java",
                "java/JavaParser.java",
                "java/Javas.interp",
                "java/Javas.tokens",
                "java/JavasBaseListener.java",
                "java/JavasLexer.interp",
                "java/JavasLexer.java",
                "java/JavasLexer.tokens",
                "java/JavasListener.java",
                "java/JavasParser.java",
                "JavaScript.interp",
                "JavaScript.tokens",
                "JavaScriptLexer.interp",
                "JavaScriptLexer.js",
                "JavaScriptLexer.tokens",
                "JavaScriptListener.js",
                "JavaScriptParser.js",
                "Python2.interp",
                "Python2.tokens",
                "Python2Lexer.interp",
                "Python2Lexer.py",
                "Python2Lexer.tokens",
                "Python2Listener.py",
                "Python2Parser.py",
                "Python3.interp",
                "Python3.tokens",
                "Python3Lexer.interp",
                "Python3Lexer.py",
                "Python3Lexer.tokens",
                "Python3Listener.py",
                "Python3Parser.py",
                "Swift.interp",
                "Swift.tokens",
                "SwiftBaseListener.swift",
                "SwiftLexer.interp",
                "SwiftLexer.swift",
                "SwiftLexer.tokens",
                "SwiftLexerATN.swift",
                "SwiftListener.swift",
                "SwiftParser.swift",
                "SwiftParserATN.swift");
        }
    }


    @Test
    public void layoutCustom() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/CustomLayout"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .layout("src/antlrgrammars")
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("foo/bar/HelloLexer.tokens",
                "foo/bar/Hello.tokens",
                "foo/bar/HelloBaseListener.java",
                "foo/bar/HelloParser.java",
                "foo/bar/HelloLexer.interp",
                "foo/bar/HelloListener.java",
                "foo/bar/HelloLexer.java",
                "foo/bar/Hello.interp");

            assertTrue(
                new String(Files.readAllBytes(project.resolve("target/HelloParser.java")),
                    StandardCharsets.UTF_8).contains("package foo.bar;"));
        }
    }


    @Test
    public void layoutFlat() throws Exception
    {
        try (TestProject project = TestProject.create(
                "examples/antlr4/NamespaceByConvention"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .layout("flat")
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("HelloLexer.tokens",
                "Hello.tokens",
                "HelloBaseListener.java",
                "HelloParser.java",
                "HelloLexer.interp",
                "HelloListener.java",
                "HelloLexer.java",
                "Hello.interp");

            assertTrue(
                !new String(
                    Files.readAllBytes(project.resolve("target/HelloParser.java")),
                    StandardCharsets.UTF_8).contains("package "));
        }
    }


    @Test
    public void log() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/HelloWorld"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args("-Xlog"))
                .generate();

            URI uri = URI.create("jar:file:" + project.srcjar().toUri().getPath());

            try (FileSystem fs = FileSystems.newFileSystem(uri,
                    new HashMap<String, String>()))
            {
                try (DirectoryStream<Path> entries = Files.newDirectoryStream(
                        fs.getPath("/"),
                        "*.log"))
                {
                    boolean found = false;

                    for (@SuppressWarnings("unused")
                        Path entry : entries)
                    {
                        found = true;
                    }

                    assertTrue(found);
                }
            }
        }
    }


    @Test
    public void namespaceAttribute() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/HelloWorld"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .namespace("foo.bar")
                .args(project.args("-package", "foo.bar"))
                .generate();

            project.validate("foo/bar/HelloLexer.tokens",
                "foo/bar/Hello.tokens",
                "foo/bar/HelloBaseListener.java",
                "foo/bar/HelloParser.java",
                "foo/bar/HelloLexer.interp",
                "foo/bar/HelloListener.java",
                "foo/bar/HelloLexer.java",
                "foo/bar/Hello.interp");

            assertTrue(
                new String(Files.readAllBytes(project.resolve("target/HelloParser.java")),
                    StandardCharsets.UTF_8).contains("package foo.bar;"));
        }
    }


    @Test
    public void namespaceByConvention() throws Exception
    {
        try (TestProject project = TestProject.create(
                "examples/antlr4/NamespaceByConvention"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("foo/bar/HelloLexer.tokens",
                "foo/bar/Hello.tokens",
                "foo/bar/HelloBaseListener.java",
                "foo/bar/HelloParser.java",
                "foo/bar/HelloLexer.interp",
                "foo/bar/HelloListener.java",
                "foo/bar/HelloLexer.java",
                "foo/bar/Hello.interp");

            assertTrue(
                new String(Files.readAllBytes(project.resolve("target/HelloParser.java")),
                    StandardCharsets.UTF_8).contains("package foo.bar;"));
        }
    }


    @Test
    public void namespaceByGrammar() throws Exception
    {
        try (TestProject project = TestProject.create(
                "examples/antlr4/NamespaceByGrammar"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args( /*"-package", "hello.world"*/))
                .generate();

            project.validate("com/company/hello/HelloLexer.tokens",
                "com/company/hello/Hello.tokens",
                "com/company/hello/HelloBaseListener.java",
                "com/company/hello/HelloParser.java",
                "com/company/hello/HelloLexer.interp",
                "com/company/hello/HelloListener.java",
                "com/company/hello/HelloLexer.java",
                "com/company/hello/Hello.interp");

            assertTrue(
                new String(Files.readAllBytes(project.resolve("target/HelloParser.java")),
                    StandardCharsets.UTF_8).contains("package com.company.hello;"));
        }
    }


    @Test
    public void namespaceGrammarOverConvention() throws Exception
    {
        try (TestProject project = TestProject.create(
                "examples/antlr4/NamespaceGrammarOverConvention"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("hello/HelloLexer.tokens",
                "hello/Hello.tokens",
                "hello/HelloBaseListener.java",
                "hello/HelloParser.java",
                "hello/HelloLexer.interp",
                "hello/HelloListener.java",
                "hello/HelloLexer.java",
                "hello/Hello.interp");

            assertTrue(
                new String(Files.readAllBytes(project.resolve("target/HelloParser.java")),
                    StandardCharsets.UTF_8).contains("package hello;"));
        }
    }


    @Test
    public void namespaceNone() throws Exception
    {
        try (TestProject project = TestProject.create("examples/antlr4/HelloWorld"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .args(project.args())
                .generate();

            project.validate("HelloLexer.tokens",
                "Hello.tokens",
                "HelloBaseListener.java",
                "HelloParser.java",
                "HelloLexer.interp",
                "HelloListener.java",
                "HelloLexer.java",
                "Hello.interp");
        }
    }


    @Test
    public void namespacePackageConflictsWithGrammar() throws Exception
    {
        try (TestProject project = TestProject.create(
                "examples/antlr4/NamespaceByGrammar"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .namespace("foo.bar")
                .args(project.args())
                .generate();

            fail();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(
                "Specified package attribute 'foo.bar' conflicting with namespace 'com.company.hello' in grammar Hello.g4",
                ex.getMessage());
        }

        try (TestProject project = TestProject.create(
                "examples/antlr4/NamespaceByGrammar"))
        {
            AntlrRules.create(project.root())
                .srcjar(project.srcjar().toString())
                .version("4")
                .classpath(project.antlr4())
                .outputDirectory(project.outputDirectory().toString())
                .encoding("UTF-8")
                .grammars(project.grammars())
                .namespace("com.company.hello")
                .args(project.args())
                .generate();
        }
    }
}
