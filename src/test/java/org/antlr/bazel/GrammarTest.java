package org.antlr.bazel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import static org.antlr.bazel.Language.*;
import static org.antlr.bazel.Version.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Tests for {@link Grammar}.
 *
 * @author  Marco Hunsicker
 */
public class GrammarTest
{
    @Test
    public void compare() throws IOException
    {
        List<Grammar> g = Arrays.asList(
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G3.g4")),
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G1.g4")),
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G2.g4")),
            grammar(V4,
                path("examples/antlr4/InheritSameFolder/src/main/antlr4/Nested.g4")));

        List<Grammar> expected = Arrays.asList(
            grammar(V4,
                path("examples/antlr4/InheritSameFolder/src/main/antlr4/Nested.g4")),
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G1.g4")),
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G3.g4")),
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G2.g4")));

        Collections.sort(g);

        assertEquals(expected, g);
    }


    @Test
    public void detectHeader() throws IOException
    {
        try (FileSystem fs = fs(Configuration.unix(), 2))
        {
            Grammar g = grammar(V2, fs.getPath("root/src/main/antlr/Java.g"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of("java"), g.namespace);
            assertEquals("java", g.getNamespacePath().toString());
        }
    }


    @Test
    public void equals() throws IOException
    {
        Grammar g1 = grammar(V4,
            path("examples/antlr4/InheritSameFolder/src/main/antlr4/G1.g4"));
        assertEquals(g1, g1);
        assertEquals(
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G3.g4")),
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G3.g4")));
        assertNotEquals(g1,
            grammar(V4, path("examples/antlr4/InheritSameFolder/src/main/antlr4/G2.g4")));
        assertNotEquals(g1, null);
        assertNotEquals(g1, new String());
    }


    @Test
    public void hashcode() throws IOException
    {
        Path path = path("examples/antlr4/InheritSameFolder/src/main/antlr4/G1.g4");
        Grammar g = grammar(V4, path);
        assertEquals(31 + path.hashCode(), g.hashCode());
    }


    @Test
    public void imports() throws Exception
    {
        try (FileSystem fs = fs(Configuration.unix(), 3))
        {
            Path f = fs.getPath("test.g");
            Files.write(f, "grammar Simple;\n".getBytes(StandardCharsets.UTF_8));

            Grammar g = grammar(V3, f);
            assertEquals(Arrays.asList("").toString(), g.imports.toString());

            Files.write(f, "grammar Simple;\nimport A;".getBytes(StandardCharsets.UTF_8));
            g = grammar(V3, f);
            assertEquals(Arrays.asList("A").toString(), g.imports.toString());

            Files.write(f,
                "grammar Simple;\nimport A ;".getBytes(StandardCharsets.UTF_8));
            g = grammar(V3, f);
            assertEquals(Arrays.asList("A").toString(), g.imports.toString());

            Files.write(f,
                "grammar Simple;\nimport A,B;".getBytes(StandardCharsets.UTF_8));
            g = grammar(V3, f);
            assertEquals(Arrays.asList("A, B").toString(), g.imports.toString());

            Files.write(f,
                "grammar Simple;\nimport A, B;".getBytes(StandardCharsets.UTF_8));
            g = grammar(V3, f);
            assertEquals(Arrays.asList("A, B").toString(), g.imports.toString());

            Files.write(f,
                "grammar Simple;\nimport A ,B;".getBytes(StandardCharsets.UTF_8));
            g = grammar(V3, f);
            assertEquals(Arrays.asList("A, B").toString(), g.imports.toString());

            Files.write(f,
                "grammar Simple;\nimport A, B;".getBytes(StandardCharsets.UTF_8));
            g = grammar(V3, f);
            assertEquals(Arrays.asList("A, B").toString(), g.imports.toString());

            Files.write(f,
                "grammar Simple;\nimport A,B,C;".getBytes(StandardCharsets.UTF_8));
            g = grammar(V3, f);
            assertEquals(Arrays.asList("A, B, C").toString(), g.imports.toString());

            g = grammar(V4, path("examples/antlr4/DetectLanguage/src/main/antlr4/swift/GrammarParser.g4"));
            assertTrue(g.imports.isEmpty());

            g = grammar(V4, path("examples/antlr3/InheritLibFolder/src/main/antlr3/lib/CommonLexer.g"));
            assertTrue(g.imports.isEmpty());
        }
    }


    @Test
    public void names() throws IOException
    {
        Grammar grammar = grammar(V2,
            path("examples/antlr2/Calc/src/main/antlr2/calc.g"));
        assertEquals(JAVA, grammar.language);
        assertEquals(Namespace.of("calc"), grammar.namespace);
        assertEquals("calc", grammar.getNamespacePath().toString());
        assertArrayEquals(
            Arrays.asList("CalcParser", "CalcLexer", "CalcTreeWalker", "calc").toArray(),
            grammar.names.toArray());

        grammar = grammar(V2,
            path("examples/antlr2/InheritTinyC/src/main/antlr2/subc.g"));
        assertEquals(JAVA, grammar.language);
        assertEquals(Namespace.of(""), grammar.namespace);
        assertEquals("", grammar.getNamespacePath().toString());
        assertArrayEquals(Arrays.asList("MyCParser", "subc").toArray(),
            grammar.names.toArray());

        grammar = grammar(V3, path("examples/antlr3/DetectLanguage/src/main/antlr3/T.g"));
        assertEquals(CSHARP, grammar.language);
        assertEquals(Namespace.of("Antlr.Examples.HoistedPredicates"), grammar.namespace);
        assertEquals("Antlr/Examples/HoistedPredicates",
            grammar.getNamespacePath().toString());
        assertArrayEquals(Arrays.asList("T").toArray(), grammar.names.toArray());
    }


    @Test
    public void test() throws IOException
    {
        try (FileSystem fs = fs(Configuration.unix(), 4))
        {
            Grammar g = grammar(V4, fs.getPath("root/src/main/antlr4/Hello.g4"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of(""), g.namespace);
            assertEquals("", g.getNamespacePath().toString());

            g = grammar(V4, fs.getPath("root/src/main/antlr4/nested/Hello.g4"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of("nested"), g.namespace);
            assertEquals("nested", g.getNamespacePath().toString());

            g = grammar(V4, fs.getPath("root/src/main/antlr4/nested/deeply/Hello.g4"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of("nested.deeply"), g.namespace);
            assertEquals("nested/deeply", g.getNamespacePath().toString());

            g = grammar(V4, fs.getPath("root/src/main/nested/deeply/Hello.g4"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of(""), g.namespace);
            assertEquals("", g.getNamespacePath().toString());

            g = new Grammar(V4,
                fs.getPath("root/src/main/antlr4/Hello.g4"),
                CSHARP,
                Namespace.of("com.foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(CSHARP, g.language);
            assertEquals(Namespace.of("com.foo"), g.namespace);
            assertEquals("com/foo", g.getNamespacePath().toString());

            g = new Grammar(V4,
                fs.getPath("root/src/main/antlr4/Hello.g4"),
                GO,
                Namespace.of("com/foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(GO, g.language);
            assertEquals(Namespace.of("com/foo"), g.namespace);
            assertEquals("com/foo", g.getNamespacePath().toString());

            g = new Grammar(V4,
                fs.getPath("root/src/main/antlr4/Hello.g4"),
                GO,
                Namespace.of("com/foo"),
                StandardCharsets.UTF_8,
                "src/antlr");
            assertEquals(GO, g.language);
            assertEquals(Namespace.of("com/foo"), g.namespace);
            assertEquals("com/foo", g.getNamespacePath().toString());
        }

        try (FileSystem fs = fs(Configuration.windows(), 4))
        {
            Grammar g = grammar(V4, fs.getPath("root\\src\\main\\antlr4\\Hello.g4"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of(""), g.namespace);
            assertEquals("".toString(), g.getNamespacePath().toString());

            g = grammar(V4, fs.getPath("root\\src\\main\\antlr4\\nested\\Hello.g4"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of("nested"), g.namespace);
            assertEquals("nested", g.getNamespacePath().toString());

            g = grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\nested\\deeply\\Hello.g4"));
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of("nested.deeply"), g.namespace);
            assertEquals("nested\\deeply", g.getNamespacePath().toString());

            g = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\Hello.g4"),
                CPP,
                Namespace.of("com::foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(CPP, g.language);
            assertEquals(Namespace.of("com::foo"), g.namespace);
            assertEquals("com\\foo", g.getNamespacePath().toString());

            g = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\Hello.g4"),
                CSHARP,
                Namespace.of("com.foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(CSHARP, g.language);
            assertEquals(Namespace.of("com.foo"), g.namespace);
            assertEquals("com\\foo", g.getNamespacePath().toString());

            g = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\Hello.g4"),
                GO,
                Namespace.of("com/foo"),
                StandardCharsets.UTF_8,
                null);
            assertEquals(GO, g.language);
            assertEquals(Namespace.of("com/foo"), g.namespace);
            assertEquals("com\\foo", g.getNamespacePath().toString());

            g = new Grammar(V4,
                fs.getPath("root\\src\\main\\antlr4\\nested\\deeply\\Hello.g4"),
                JAVA,
                Namespace.of("com.foo"),
                StandardCharsets.UTF_8,
                "flat");
            assertEquals(JAVA, g.language);
            assertEquals(Namespace.of("com.foo"), g.namespace);
            assertEquals("", g.getNamespacePath().toString());
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


    private Path path(String path)
    {
        return Projects.path(path);
    }
}
