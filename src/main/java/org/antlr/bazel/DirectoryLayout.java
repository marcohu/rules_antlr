package org.antlr.bazel;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Defines the path of the common default directory where grammars files should be placed.
 *
 * @author  Marco Hunsicker
 */
class DirectoryLayout
{
    static final Pattern PATH = Pattern.compile(
        ".*[\\\\/]src[\\\\/]antlr[234]?[\\\\/](.*)");

    private boolean flat;

    private final Pattern pattern;

    /** Creates a new DirectoryLayout object. */
    public DirectoryLayout()
    {
        pattern = null;
    }


    /**
     * Creates a new DirectoryLayout object.
     *
     * @param  path  the directory path.
     */
    public DirectoryLayout(String path)
    {
        this(
            "flat".equalsIgnoreCase(path)
            ? null
            : Pattern.compile(
                ".*"
                + ((!path.startsWith("/") && !path.startsWith("\\")) ? "[\\\\/]" : "")
                + path.replaceAll("[\\\\/]", "[\\\\\\\\/]")
                + ((!path.endsWith("/") && !path.endsWith("\\")) ? "[\\\\/]" : "")
                + "(.*)"));

        flat = "flat".equalsIgnoreCase(path);
    }


    /**
     * Creates a new DirectoryLayout object.
     *
     * @param  pattern  the directory pattern.
     */
    DirectoryLayout(Pattern pattern)
    {
        this.pattern = pattern;
    }

    /**
     * Returns the path of the given grammar file relative to the common directory if the
     * file path conforms with this directory layout.
     *
     * @param   file  the grammar file.
     *
     * @return  the relative path.
     */
    public Path getRelativePath(Path file)
    {
        if (pattern != null)
        {
            Matcher matcher = pattern.matcher(file.toString());

            if (matcher.find())
            {
                Path parent = file.getFileSystem().getPath(matcher.group(1)).getParent();

                if (parent != null)
                {
                    return parent;
                }
            }
        }

        return file.getFileSystem().getPath("");
    }


    /**
     * Returns whether this layout dictates a flat directory.
     *
     * @return  {@code true} if a flat layout is dictated.
     */
    public boolean isFlat()
    {
        return flat;
    }


    @Override
    public String toString()
    {
        return (pattern != null) ? pattern.pattern() : "";
    }
}
