package org.antlr.bazel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Case format helper.
 *
 * @author  Marco Hunsicker
 */
class CaseFormat
{
    private static final Pattern UPPER_CAMEL = Pattern.compile(
        "((?:^|_)\\p{javaLowerCase})");

    private static final Pattern LOWER_UNDERSCORE = Pattern.compile(
        "(.)(\\p{javaUpperCase})");

    /** Creates a new CaseFormat object. */
    private CaseFormat()
    {
        super();
    }

    /**
     * Converts the given string to lower underscore casing.
     *
     * @param   s  a string.
     *
     * @return  the converted string.
     */
    public static String toLowerUnderscore(String s)
    {
        return LOWER_UNDERSCORE.matcher(s).replaceAll("$1_$2").toLowerCase();
    }


    /**
     * Converts the given string to upper camel casing.
     *
     * @param   s  a string.
     *
     * @return  the converted string.
     */
    public static String toUpperCamelCase(String s)
    {
        Matcher matcher = UPPER_CAMEL.matcher(s);

        StringBuffer buf = new StringBuffer(s.length());

        while (matcher.find())
        {
            if (matcher.start() == 0)
            {
                matcher.appendReplacement(buf, matcher.group(1).toUpperCase());
            }
            else
            {
                matcher.appendReplacement(buf,
                    matcher.group(1).substring(1).toUpperCase());
            }
        }

        matcher.appendTail(buf);

        return buf.toString();
    }
}
