package org.antlr.bazel;

import static org.antlr.bazel.Strings.stripFileExtension;
import static org.junit.Assert.assertEquals;

import org.antlr.bazel.Strings;
import org.junit.Test;


/**
 * Tests for {@link Strings}.
 *
 * @author  Marco Hunsicker
 */
public class StringsTest
{
    @Test
    public void constructor() throws Exception
    {
        UtilityClassTestSupport.test(Strings.class);
    }


    @Test
    public void stripExtension()
    {
        assertEquals("", stripFileExtension(""));
        assertEquals("filename", stripFileExtension("filename"));
        assertEquals(".filename", stripFileExtension(".filename"));
        assertEquals(".filename", stripFileExtension(".filename.txt"));
        assertEquals("filename", stripFileExtension("filename.txt"));
        assertEquals("file.name", stripFileExtension("file.name.txt"));
    }
}
