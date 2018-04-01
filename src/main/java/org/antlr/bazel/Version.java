package org.antlr.bazel;


/**
 * Enumeration of ANTLR versions.
 *
 * @author  Marco Hunsicker
 */
enum Version
{
    V2,
    V3,
    V4;

    /**
     * Returns the version for the given major version.
     *
     * @param   version  the major version.
     *
     * @return  the version.
     */
    public static Version of(String version)
    {
        switch (version)
        {
            case "2" :
            {
                return V2;
            }

            case "3" :
            {
                return V3;
            }

            case "4" :
            {
                return V4;
            }

            default :
            {
                throw new IllegalArgumentException("Unknown version" + version);
            }
        }
    }
}
