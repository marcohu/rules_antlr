package org.antlr.bazel;


/**
 * String helper.
 *
 * @author  Marco Hunsicker
 */
class Strings
{
    /** Creates a new Strings object. */
    private Strings()
    {
        super();
    }

    /**
     * Strips the file extension from the given file path.
     *
     * @param   path  the file path.
     *
     * @return  the stripped path or <em>path</em> if no extension could be found.
     */
    public static String stripFileExtension(String path)
    {
        int dot = path.lastIndexOf('.');

        return (dot > 0) ? path.substring(0, dot) : path;
    }
}
