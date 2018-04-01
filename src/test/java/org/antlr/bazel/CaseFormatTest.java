package org.antlr.bazel;

import static org.antlr.bazel.CaseFormat.toLowerUnderscore;
import static org.antlr.bazel.CaseFormat.toUpperCamelCase;
import static org.junit.Assert.assertEquals;

import org.antlr.bazel.CaseFormat;
import org.junit.Test;


/**
 * Tests for {@link CaseFormat}.
 *
 * @author  Marco Hunsicker
 */
public class CaseFormatTest
{
    @Test
    public void camelCase()
    {
        assertEquals("Abcdef", toUpperCamelCase("abcdef"));
        assertEquals("AbCdEf", toUpperCamelCase("ab_cd_ef"));
        assertEquals("AbCdEf", toUpperCamelCase("abCdEf"));
        assertEquals("AbCdEf", toUpperCamelCase("AbCdEf"));
    }


    @Test
    public void constructor() throws Exception
    {
        UtilityClassTestSupport.test(CaseFormat.class);
    }


    @Test
    public void lowerUnderscore()
    {
        assertEquals("abcdef", toLowerUnderscore("abcdef"));
        assertEquals("ab_cd_ef", toLowerUnderscore("AbCdEf"));
        assertEquals("ab_cd_ef", toLowerUnderscore("abCdEf"));
        assertEquals("ab_cd_ef", toLowerUnderscore("ab_cd_ef"));
    }
}
