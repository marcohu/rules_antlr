package org.antlr.bazel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import static org.antlr.bazel.Language.*;
import static org.antlr.bazel.Version.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.antlr.bazel.Grammar;
import org.antlr.bazel.Namespace;
import org.antlr.bazel.Version;
import org.junit.Test;


/**
 * Tests for {@link Grammar}.
 *
 * @author  Marco Hunsicker
 */
public class GrammarTest
{
    @Test
    public void detectHeader() throws IOException
    {
        try (FileSystem fs = fs(Configuration.unix(), 2))
        {
            Grammar grammar = grammar(V2, fs.getPath("root/src/main/antlr/Java.g"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of("java"), grammar.namespace);
            assertEquals("java", grammar.getNamespacePath().toString());
        }
    }


    @Test
    public void names() throws IOException
    {
        Grammar grammar = grammar(V2, Paths.get("src/test/resources/org/antlr/bazel/data.g"));
        assertEquals(JAVA, grammar.language);
        assertEquals(Namespace.of("java"), grammar.namespace);
        assertEquals("java", grammar.getNamespacePath().toString());
        assertArrayEquals(Arrays.asList("DataParser", "DataLexer", "data").toArray(),
            grammar.names.toArray());
        assertEquals("data.g", grammar.toString());

        grammar = grammar(V2,
            Paths.get("src/test/resources/org/antlr/bazel//subc.g"));
        assertEquals(JAVA, grammar.language);
        assertEquals(Namespace.of(""), grammar.namespace);
        assertEquals("", grammar.getNamespacePath().toString());
        assertArrayEquals(Arrays.asList("MyCParser", "subc").toArray(),
            grammar.names.toArray());

        grammar = grammar(V3, Paths.get("src/test/resources/org/antlr/bazel/Lang.g"));
        assertEquals(JAVASCRIPT, grammar.language);
        assertEquals(Namespace.of(""), grammar.namespace);
        assertEquals("", grammar.getNamespacePath().toString());
        assertArrayEquals(Arrays.asList("Lang").toArray(), grammar.names.toArray());

        grammar = grammar(V3, Paths.get("src/test/resources/org/antlr/bazel/LangDumpDecl.g"));
        assertEquals(C, grammar.language);
        assertEquals(Namespace.of(""), grammar.namespace);
        assertEquals("", grammar.getNamespacePath().toString());
        assertArrayEquals(Arrays.asList("LangDumpDecl").toArray(),
            grammar.names.toArray());
    }


    @Test
    public void test() throws IOException
    {
        try (FileSystem fs = fs(Configuration.unix(), 4))
        {
            Grammar grammar = grammar(V4, fs.getPath("root/src/main/antlr4/Hello.g4"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of(""), grammar.namespace);
            assertEquals("", grammar.getNamespacePath().toString());

            grammar = grammar(V4, fs.getPath("root/src/main/antlr4/nested/Hello.g4"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of("nested"), grammar.namespace);
            assertEquals("nested", grammar.getNamespacePath().toString());

            grammar = grammar(V4,
                fs.getPath("root/src/main/antlr4/nested/deeply/Hello.g4"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of("nested.deeply"), grammar.namespace);
            assertEquals("nested/deeply", grammar.getNamespacePath().toString());

            grammar = grammar(V4, fs.getPath("root/src/main/nested/deeply/Hello.g4"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of(""), grammar.namespace);
            assertEquals("", grammar.getNamespacePath().toString());

            grammar = new Grammar(V4,
                fs.getPath("root/src/main/antlr4/Hello.g4"),
                CSHARP,
                Namespace.of("com.foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(CSHARP, grammar.language);
            assertEquals(Namespace.of("com.foo"), grammar.namespace);
            assertEquals("com/foo", grammar.getNamespacePath().toString());

            grammar = new Grammar(V4,
                fs.getPath("root/src/main/antlr4/Hello.g4"),
                GO,
                Namespace.of("com/foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(GO, grammar.language);
            assertEquals(Namespace.of("com/foo"), grammar.namespace);
            assertEquals("com/foo", grammar.getNamespacePath().toString());

            grammar = new Grammar(V4,
                fs.getPath("root/src/main/antlr4/Hello.g4"),
                GO,
                Namespace.of("com/foo"),
                StandardCharsets.UTF_8,
                "src/antlr");
            assertEquals(GO, grammar.language);
            assertEquals(Namespace.of("com/foo"), grammar.namespace);
            assertEquals("com/foo", grammar.getNamespacePath().toString());
        }

        try (FileSystem fs = fs(Configuration.windows(), 4))
        {
            Grammar grammar = grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\Hello.g4"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of(""), grammar.namespace);
            assertEquals("".toString(), grammar.getNamespacePath().toString());

            grammar = grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\nested\\Hello.g4"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of("nested"), grammar.namespace);
            assertEquals("nested", grammar.getNamespacePath().toString());

            grammar = grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\nested\\deeply\\Hello.g4"));
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of("nested.deeply"), grammar.namespace);
            assertEquals("nested\\deeply", grammar.getNamespacePath().toString());

            grammar = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\Hello.g4"),
                CPP,
                Namespace.of("com::foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(CPP, grammar.language);
            assertEquals(Namespace.of("com::foo"), grammar.namespace);
            assertEquals("com\\foo", grammar.getNamespacePath().toString());

            grammar = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\Hello.g4"),
                CSHARP,
                Namespace.of("com.foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(CSHARP, grammar.language);
            assertEquals(Namespace.of("com.foo"), grammar.namespace);
            assertEquals("com\\foo", grammar.getNamespacePath().toString());

            grammar = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\Hello.g4"),
                GO,
                Namespace.of("com/foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(GO, grammar.language);
            assertEquals(Namespace.of("com/foo"), grammar.namespace);
            assertEquals("com\\foo", grammar.getNamespacePath().toString());

            grammar = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\nested\\deeply\\Hello.g4"),
                JAVA,
                Namespace.of("com.foo"),
                StandardCharsets.UTF_8,
                "flat");
            assertEquals(JAVA, grammar.language);
            assertEquals(Namespace.of("com.foo"), grammar.namespace);
            assertEquals("", grammar.getNamespacePath().toString());
        }
    }


    private void createTestFiles(FileSystem fs, int version) throws IOException
    {
        Path defaultFolder = fs.getPath("root/src/main/antlr");
        Path defaultVersionedFolder = fs.getPath("root/src/main/antlr" + version);
        Path customFolder = fs.getPath("root/src/main/");

        String ext = extension(version);

        Files.createDirectories(defaultFolder);
        Files.createDirectories(defaultVersionedFolder);
        Files.createDirectories(customFolder);

        Files.createDirectories(defaultFolder.resolve("nested/deeply"));
        Files.createDirectories(defaultVersionedFolder.resolve("nested/deeply"));
        Files.createDirectories(customFolder.resolve("nested/deeply"));

        byte[] hello = "grammar Hello;\nr  : 'hello' ID ;\nID : [a-z]+ ;\nWS : [ \\t\\r\\n]+ -> skip ;"
                .getBytes(StandardCharsets.UTF_8);

        Files.write(defaultFolder.resolve("Hello." + ext), hello);
        Files.write(defaultVersionedFolder.resolve("Hello." + ext), hello);
        Files.write(customFolder.resolve("Hello." + ext), hello);

        Files.write(defaultFolder.resolve("nested/Hello." + ext), hello);
        Files.write(defaultVersionedFolder.resolve("nested/Hello." + ext), hello);
        Files.write(customFolder.resolve("nested/Hello." + ext), hello);

        Files.write(defaultFolder.resolve("nested/deeply/Hello." + ext), hello);
        Files.write(defaultVersionedFolder.resolve("nested/deeply/Hello." + ext), hello);
        Files.write(customFolder.resolve("nested/deeply/Hello." + ext), hello);

        Files.write(defaultFolder.resolve("Java." + ext),
            "grammar Java;\nheader {package java;}".getBytes(StandardCharsets.UTF_8));
    }


    private String extension(int version)
    {
        switch (version)
        {
            case 2 :
            {
                return "g";
            }

            default :
            {
                return "g" + version;
            }
        }
    }


    private FileSystem fs(Configuration config, int version) throws IOException
    {
        FileSystem fs = Jimfs.newFileSystem(config);

        createTestFiles(fs, version);

        return fs;
    }


    private Grammar grammar(Version version, Path path) throws IOException
    {
        return new Grammar(version, path, null, null, StandardCharsets.UTF_8, null);
    }
}
