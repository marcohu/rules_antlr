package org.antlr.bazel;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Represents an ANTLR grammar file.
 *
 * @author  Marco Hunsicker
 */
class Grammar implements Comparable<Grammar>
{
    /** The used encoding. */
    public final Charset encoding;

    /** The imported grammars. */
    public final Collection<String> imports;

    /** The target language. */
    public final Language language;

    /** The possible corresponding source file name prefixes. */
    public final Collection<String> names;

    /** The namespace. */
    public final Namespace namespace;

    /** The file path. */
    public final Path path;

    private final DirectoryLayout layout;

    /**
     * Creates a new Grammar object.
     *
     * @param   version    the used ANTLR version.
     * @param   path       the file path.
     * @param   language   the target language to use.
     * @param   namespace  the namespace to use.
     * @param   encoding   the encoding to use.
     * @param   layout     the common directory layout for grammar files.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    public Grammar(Version version,
        Path path,
        Language language,
        Namespace namespace,
        Charset encoding,
        String layout) throws IOException
    {
        String text = new String(Files.readAllBytes(path), encoding);
        this.path = path;
        this.encoding = encoding;
        this.language = (language != null) ? language : Language.detect(text);
        this.layout = (layout != null) ? new DirectoryLayout(layout)
                                       : this.language.getLayout();
        this.namespace = namespace(namespace, text);
        this.names = detectNames(version, text);
        this.imports = detectImports(text);
    }

    @Override
    public int compareTo(Grammar other)
    {
        if (imports.isEmpty())
        {
            if (!other.imports.isEmpty())
            {
                return 1;
            }
        }
        else if (other.imports.isEmpty())
        {
            return -1;
        }
        else if (imports.contains(Strings.stripFileExtension(other.toString())))
        {
            return -1;
        }

        return 0;
    }


    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }

        if ((other == null) || (getClass() != other.getClass()))
        {
            return false;
        }

        Grammar that = (Grammar) other;

        return path.equals(that.path);
    }


    /**
     * Returns the corresponding namespace path.
     *
     * @return  the corresponding namespace path.
     */
    public Path getNamespacePath()
    {
        // flat layout might be forced for namespace
        return path.getFileSystem()
            .getPath(layout.isFlat() ? "" : namespace.toPath(language));
    }


    @Override
    public int hashCode()
    {
        return 31 + path.hashCode();
    }


    @Override
    public String toString()
    {
        return path.getFileName().toString();
    }


    private Collection<String> detectImports(String text)
    {
        Collection<String> imports = new ArrayList<>();

        // strip blocks to avoid matching language specific import
        Pattern p = Pattern.compile("import\\s+(.*?);");
        Matcher matcher = p.matcher(text.replaceAll("(?s)\\{.*?\\}", ""));

        if (matcher.find())
        {
            for (String type : matcher.group(1).split(","))
            {
                imports.add(type.trim());
            }
        }

        return Collections.unmodifiableCollection(imports);
    }


    private Set<String> detectNames(Version version, String text)
    {
        Set<String> names = new LinkedHashSet<>(5);

        if (version == Version.V2)
        {
            Pattern[] patterns =
                {
                    Pattern.compile("^\\s*class\\s+(\\S*?)\\s+extends\\s+\\S*?Parser",
                        Pattern.DOTALL | Pattern.MULTILINE),
                    Pattern.compile("^\\s*class\\s+(\\S*?)\\s+extends\\s+\\S*?Lexer",
                        Pattern.DOTALL | Pattern.MULTILINE),
                    Pattern.compile("^\\s*class\\s+(\\S*?)\\s+extends\\s+\\S*?TreeParser",
                        Pattern.DOTALL | Pattern.MULTILINE)
                };

            for (Pattern pattern : patterns)
            {
                String name = findName(pattern, text);

                if (name != null)
                {
                    names.add(name);
                }
            }
        }
        else
        {
            Pattern pattern = Pattern.compile(
                "^\\s*(?:(?:parser|lexer|tree|combined)\\s+)?grammar\\s+(\\S*?)\\s*;",
                Pattern.DOTALL | Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find())
            {
                names.add(matcher.group(1));
            }
        }

        names.add(Strings.stripFileExtension(path.getFileName().toString()));

        return Collections.unmodifiableSet(names);
    }


    private String findName(Pattern pattern, String text)
    {
        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group(1) : null;
    }


    private Namespace namespace(Namespace namespace, String text)
    {
        Namespace result = namespace;

        // always detect the grammar namespace to be able to report conflicts
        Namespace ns = language.detectNamespace(text);

        if (result == null)
        {
            result = ns;
        }
        else if (ns != null && !ns.equals(namespace))
        {
            throw new IllegalStateException(
                String.format(
                    "Specified package attribute '%s' %s namespace '%s' in grammar %s",
                    namespace,
                    "conflicting with",
                    ns,
                    path.getFileName()));
        }

        if (result == null)
        {
            // if the grammar does not contain a namespace, we resort to the
            // directory layout convention
            result = Namespace.of(layout.getRelativePath(path), language);
        }

        return result;
    }
}
