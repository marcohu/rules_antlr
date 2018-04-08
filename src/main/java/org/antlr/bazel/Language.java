package org.antlr.bazel;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Enumeration of target languages. Support varies with ANTLR versions.
 *
 * @author  Marco Hunsicker
 */
enum Language
{
    /** The C target language. */
    C()
    {
        @Override
        public String toPath(String namespace)
        {
            return namespace;
        }


        @Override
        public String toId(Path path)
        {
            return path.toString();
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return LAYOUT;
        }
    },

    /** The C++ target language. */
    CPP()
    {
        @Override
        public String toPath(String namespace)
        {
            return namespace.replaceAll("::", "/");
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[/\\\\]", "::");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            Matcher matcher = NAMESPACE.matcher(grammar);

            return matcher.find() ? namespace(matcher.group(1)) : null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return LAYOUT;
        }
    },

    /** The C# target language. */
    CSHARP
    {
        @Override
        public String toPath(String namespace)
        {
            return namespace.replace('.', '/');
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[/\\\\]", ".");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            Matcher matcher = NAMESPACE.matcher(grammar);

            return matcher.find() ? namespace(matcher.group(1)) : null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return LAYOUT;
        }
    },

    /** The Go target language. */
    GO
    {
        private final DirectoryLayout layout = new DirectoryLayout();

        @Override
        public String toPath(String namespace)
        {
            return namespace;
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[\\\\]", "/");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            String header = header(grammar);

            if (header != null)
            {
                Matcher matcher = PACKAGE.matcher(header);

                if (matcher.find())
                {
                    return namespace(matcher.group(1));
                }
            }

            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return layout;
        }
    },

    /** The Java target language. */
    JAVA
    {
        private final DirectoryLayout layout = new DirectoryLayout(
            Pattern.compile(".*[\\\\/]src[\\\\/]main[\\\\/]antlr[234]?[\\\\/](.*)"));

        @Override
        public String toPath(String namespace)
        {
            return namespace.replace('.', '/');
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[/\\\\]", "\\.");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            String header = header(grammar);

            if (header != null)
            {
                Matcher matcher = PACKAGE.matcher(header);

                return matcher.find() ? namespace(matcher.group(1)) : null;
            }

            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return layout;
        }
    },

    /** The JavaScript target language. */
    JAVASCRIPT
    {
        @Override
        public String toPath(String namespace)
        {
            return namespace;
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[\\\\]", "/");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return LAYOUT;
        }
    },

    /** The Objective-C target language. */
    OBJC
    {
        private final DirectoryLayout layout = new DirectoryLayout();

        @Override
        public String toPath(String namespace)
        {
            return namespace;
        }


        @Override
        public String toId(Path path)
        {
            return path.toString();
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return layout;
        }
    },

    /** The Python target language. */
    PYTHON
    {
        @Override
        public String toPath(String namespace)
        {
            return namespace.replace('.', '/');
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[\\\\]", "/");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return LAYOUT;
        }
    },

    /** The Ruby target language. */
    RUBY
    {
        private final Pattern module = Pattern.compile("module\\s*(.*?)\\s*$",
            Pattern.DOTALL | Pattern.MULTILINE);

        @Override
        public String toPath(String namespace)
        {
            return namespace.replaceAll("::", "/");
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[/\\\\]", "::");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            String header = header(grammar);

            if (header != null)
            {
                Matcher matcher = module.matcher(header);

                if (matcher.find())
                {
                    return namespace(matcher.group(1));
                }
            }

            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    },

    /** The Swift target language. */
    SWIFT
    {
        private final DirectoryLayout layout = new DirectoryLayout();

        @Override
        public String toPath(String namespace)
        {
            return namespace.replace('.', '/');
        }


        @Override
        public String toId(Path path)
        {
            return path.toString().replaceAll("[/\\\\]", ".");
        }


        @Override
        public Namespace detectNamespace(String grammar)
        {
            // Swift does not support namespaces within modules
            return null;
        }


        @Override
        public DirectoryLayout getLayout()
        {
            return layout;
        }
    };

    private static final Pattern OPTIONS = Pattern.compile("options\\s*\\{.*?\\}",
        Pattern.DOTALL);

    private static final Pattern LANGUAGE = Pattern.compile(
        "language\\s*=\\s*([a-zA-Z0-9]+)",
        Pattern.DOTALL);

    private static final Pattern HEADER = Pattern.compile(
        "@?(?:(?:parser|lexer)::)?header.*?\\{(.*?)\\}",
        Pattern.DOTALL);

    private static final Pattern NAMESPACE = Pattern.compile(
        "@(?:(?:parser|lexer)::)?namespace\\s*\\{\\s*(.*?)\\s*\\}",
        Pattern.DOTALL);

    private static final Pattern PACKAGE = Pattern.compile("package\\s+(.+?)\\s*;?\\s*$",
        Pattern.DOTALL | Pattern.MULTILINE);

    private static final DirectoryLayout LAYOUT = new DirectoryLayout(
        DirectoryLayout.PATH);

    /**
     * Determines the namespace from the grammar header when present.
     *
     * @param   grammar  the grammar.
     *
     * @return  the namespace path. Returns {@code null} if there is no namespace defined
     *          in the grammar.
     */
    public abstract Namespace detectNamespace(String grammar);


    /**
     * Returns the default source directory.
     *
     * @return  the default source directory
     */
    public abstract DirectoryLayout getLayout();


    /**
     * Translates the given file system path into the corresponding namespace.
     *
     * @param   path  the (relative) file system path.
     *
     * @return  the namespace.
     */
    public abstract String toId(Path path);


    /**
     * Translates the given namespace into the corresponding file system path.
     *
     * @param   namespace  the namespace.
     *
     * @return  the file system path.
     */
    public abstract String toPath(String namespace);


    /**
     * Determines the grammar target language from the grammar options when present.
     *
     * @param   grammar  the grammar file contents.
     *
     * @return  the detected language. Returns {Language#JAVA} if no language option could
     *          be found.
     */
    public static Language detect(String grammar)
    {
        Matcher options = OPTIONS.matcher(grammar);

        if (options.find())
        {
            Matcher language = LANGUAGE.matcher(options.group());

            if (language.find())
            {
                return Language.of(language.group(1));
            }
        }

        // return the default if no language is provided
        return Language.JAVA;
    }


    /**
     * Returns the language for the given language name.
     *
     * @param   name  the language name.
     *
     * @return  the language.
     */
    public static Language of(String name)
    {
        switch (name)
        {
            case "C" :
            {
                return C;
            }

            case "Cpp" :
            {
                return CPP;
            }

            case "CSharp" :
            case "CSharp2" :
            case "CSharp3" :
            {
                return CSHARP;
            }

            case "Go" :
            {
                return GO;
            }

            case "Java" :
            {
                return JAVA;
            }

            case "JavaScript" :
            {
                return JAVASCRIPT;
            }

            case "ObjC" :
            {
                return OBJC;
            }

            case "Python" :
            case "Python2" :
            case "Python3" :
            {
                return PYTHON;
            }

            case "Ruby" :
            {
                return RUBY;
            }

            case "Swift" :
            {
                return SWIFT;
            }

            default :
            {
                throw new IllegalArgumentException("Unsupported language: " + name);
            }
        }
    }


    /**
     * Returns the header section of the given grammar.
     *
     * @param   grammar  the grammar.
     *
     * @return  the header section. Returns {@¢ode null} if no header section is present.
     */
    private static String header(String grammar)
    {
        Matcher header = HEADER.matcher(grammar);

        return header.find() ? header.group(1) : null;
    }


    private static Namespace namespace(String namespace)
    {
        return Namespace.of(namespace, true);
    }
}
