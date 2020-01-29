package org.antlr.bazel;

import java.util.Map;
import java.util.ServiceLoader;


/**
 * Provides access to system environment variables.
 *
 * @author  Marco Hunsicker
 */
class Environment
{
    /** Creates a new Environment object. */
    private Environment()
    {
        super();
    }

    /**
     * Sets or updates the given environment variable.
     *
     * @param name the name.
     * @param value the value.
     *
     * @return the prior value, might be {@code null}.
     */
    public static String variable(String name, String value)
    {
        return Service.PROVIDER.set(name, value);
    }

    /**
     * Returns the value of the given environment variable.
     *
     * @param   name  the name.
     *
     * @return  the value.
     */
    public static String variable(String name)
    {
        return Service.PROVIDER.get(name);
    }

    private static class Service
    {
        public static final EnvironmentService PROVIDER = provider();

        private static EnvironmentService provider()
        {
            ServiceLoader<EnvironmentService> services = ServiceLoader.load(
                EnvironmentService.class);

            // the default implementation does not allow mutation, but during testing we
            // might want to use a different implementation to be able to alter the
            // environment
            for (EnvironmentService service : services)
            {
                return service;
            }

            return new EnvironmentService()
            {
                Map<String, String> env = System.getenv();

                @Override
                public String set(String name, String value)
                {
                    throw new UnsupportedOperationException("Environment variables are read-only");
                }

                @Override
                public String get(String name)
                {
                    return env.get(name);
                }
            };
        }
    }
}
