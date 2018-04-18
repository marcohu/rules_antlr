package org.antlr.bazel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;


/**
 * Context class loader for the current thread.
 *
 * @author  Marco Hunsicker
 */
class ContextClassLoader extends URLClassLoader
{
    private final ClassLoader loader;
    private final Thread thread;

    /**
     * Creates a new ContextClassLoader object.
     *
     * @param  urls    the URLs from which to load classes and resources.
     * @param  parent  the parent class loader for delegation. Might be {@code null}.
     */
    public ContextClassLoader(Collection<URL> urls, ClassLoader parent)
    {
        super(urls.toArray(new URL[urls.size()]), parent);

        this.thread = Thread.currentThread();
        this.loader = thread.getContextClassLoader();
        this.thread.setContextClassLoader(this);
    }


    @Override
    public void close() throws IOException
    {
        super.close();
        thread.setContextClassLoader(loader);
    }
}
