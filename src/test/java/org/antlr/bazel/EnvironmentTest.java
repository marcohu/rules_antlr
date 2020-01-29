package org.antlr.bazel;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class EnvironmentTest
{
    @Test
    public void get()
    {
        assertThat(Environment.variable("USER")).isNotNull();
    }

    @Test
    public void set()
    {
        assertThat(catchThrowable(() -> Environment.variable("USER", "TEST")))
            .isInstanceOf(UnsupportedOperationException.class)
            .hasMessage("Environment variables are read-only");
    }
}
