package org.antlr.bazel;

import java.nio.file.Path;
import java.util.Objects;


/**
 * Represents a namespace.
 *
 * @author  Marco Hunsicker
 */
class Namespace
{
    /** The namespace identifier. */
    public final String id;

    private final boolean header;

    /**
     * Creates a new Namespace object.
     *
     * @param  id      the identifier.
     * @param  header  {@code true} to indicate that the namespace is defined in a grammar
     *                 file.
     */
    private Namespace(String id, boolean header)
    {
        this.id = Objects.requireNonNull(id);
        this.header = header;
    }

    /**
     * Returns the namespace for the given identifier.
     *
     * @param   id  the identifier.
     *
     * @return  the namespace.
     */
    public static Namespace of(String id)
    {
        return of(id, false);
    }


    /**
     * Returns the namespace for the given identifier.
     *
     * @param   id      the identifier.
     * @param   header  {@code true} to indicate that the namespace is defined in a
     *                  grammar file.
     *
     * @return  the namespace.
     */
    public static Namespace of(String id, boolean header)
    {
        return new Namespace(id, header);
    }


    /**
     * Returns the namespace for the given file path.
     *
     * @param   path      the file path.
     * @param   language  the target language.
     *
     * @return  the namespace.
     */
    public static Namespace of(Path path, Language language)
    {
        return of(language.toId(path));
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        Namespace other = (Namespace) obj;

        return id.equals(other.id);
    }


    @Override
    public int hashCode()
    {
        return 31 + id.hashCode();
    }


    /**
     * Returns whether this namespace is empty.
     *
     * @return  {@code true} if this namespace is empty.
     */
    public boolean isEmpty()
    {
        return id.isEmpty();
    }


    /**
     * Returns whether this namespace has been defined in a grammar.
     *
     * @return  {@code true} if this namespace has been defined in a grammar.
     */
    public boolean isHeader()
    {
        return header;
    }


    /**
     * Converts this namespace to the corresponding file path.
     *
     * @param   language  the target language.
     *
     * @return  the corresponding file path.
     */
    public String toPath(Language language)
    {
        return language.toPath(id);
    }


    @Override
    public String toString()
    {
        return id;
    }
}
