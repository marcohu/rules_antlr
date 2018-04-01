package org.antlr.bazel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;


/**
 * Helper class to improve code coverage.
 *
 * @author  Marco Hunsicker
 */
class UtilityClassTestSupport
{
    /** Creates a new UtilityClassTestSupport object. */
    private UtilityClassTestSupport()
    {
        super();
    }

    /**
     * Invokes the private constructur of the given class.
     *
     * @param   cl  the class.
     *
     * @throws  ReflectiveOperationException  if an error occurred.
     */
    public static void test(Class<?> cl) throws ReflectiveOperationException
    {
        Constructor<?> c = cl.getDeclaredConstructor();

        if (!Modifier.isPrivate(c.getModifiers()))
        {
            throw new AssertionError(
                MessageFormat.format("Constructor {0} is not private", c));
        }

        c.setAccessible(true);
        c.newInstance();
    }
}
