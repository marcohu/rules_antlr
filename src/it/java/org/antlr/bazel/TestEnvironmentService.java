package org.antlr.bazel;

import java.util.HashMap;
import java.util.Map;


public class TestEnvironmentService implements EnvironmentService
{
    private final Map<String, String> env = new HashMap<>(System.getenv());

    @Override
    public String set(String name, String value)
    {
        return env.put(name, value);
    }

    @Override
    public String get(String name)
    {
        return env.get(name);
    }
}
