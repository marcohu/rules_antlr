package org.antlr.bazel;

import java.util.Comparator;


/**
 * Sorts strings from longest to shortest.
 *
 * @author  Marco Hunsicker
 */
class LengthComparator implements Comparator<String>
{
    @Override
    public int compare(String first, String second)
    {
        int length1 = first.length();
        int length2 = second.length();

        if (length1 < length2)
        {
            return 1;
        }
        else if (length1 > length2)
        {
            return -1;
        }

        return first.compareToIgnoreCase(second);
    }
}
