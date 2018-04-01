package org.antlr.bazel;

import static org.junit.Assert.assertEquals;

import org.antlr.bazel.Version;
import org.junit.Test;


/**
 * Tests for {@link Version}.
 *
 * @author  Marco Hunsicker
 */
public class VersionTest
{
    @Test(expected = IllegalArgumentException.class)
    public void invalid()
    {
        Version.of("1");
    }


    @Test
    public void of()
    {
        assertEquals(Version.V2, Version.of("2"));
        assertEquals(Version.V3, Version.of("3"));
        assertEquals(Version.V4, Version.of("4"));
    }
}
