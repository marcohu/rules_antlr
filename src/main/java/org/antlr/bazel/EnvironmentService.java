package org.antlr.bazel;


/**
 * Environment variables service.
 *
 * @author Marco Hunsicker
 */
public interface EnvironmentService
{
    /**
     * Sets the given variable.
     *
     * @param name the name.
     * @param value the value.
     *
     * @return the old value, might be {@code null}.
     */
    String set(String name, String value);

    /**
     * Returns the variable with the given name.
     *
     * @param name the name.
     *
     * @return the value.
     */
    String get(String name);
}
