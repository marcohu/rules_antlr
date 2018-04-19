package org.antlr.bazel;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;


/**
 * Disk helper.
 *
 * @author  Marco Hunsicker
 */
class Disk
{
    private static final CopyOption[] ATTRIBUTES =
        { StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };

    /** Creates a new Disk object. */
    private Disk()
    {
        super();
    }

    /**
     * Copies the given file or directory to the given destination.
     *
     * @param   path     the file or directory.
     * @param   target   the target.
     * @param   options  the options.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    public static void copy(Path path, Path target, CopyOption... options)
        throws IOException
    {
        CopyOption[] opt = (options.length == 0) ? ATTRIBUTES : options;

        if (Files.isDirectory(path))
        {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>()
                {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir,
                        BasicFileAttributes attrs) throws IOException
                    {
                        Files.createDirectories(
                            target.resolve(path.relativize(dir).toString()));

                        return FileVisitResult.CONTINUE;
                    }


                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException
                    {
                        Files.copy(file,
                            target.resolve(path.relativize(file).toString()),
                            opt);

                        return FileVisitResult.CONTINUE;
                    }
                });
        }
        else
        {
            Files.copy(path, target, opt);
        }
    }


    /**
     * Deletes the given file or directory.
     *
     * @param   path  the path.
     *
     * @throws  IOException  if an I/O error occurred.
     */
    public static void delete(Path path) throws IOException
    {
        if (Files.isDirectory(path))
        {
            Files.walkFileTree(path, DeleteVisitor.INSTANCE);
        }
        else if (Files.exists(path))
        {
            Files.delete(path);
        }
    }

    private static class DeleteVisitor extends SimpleFileVisitor<Path>
    {
        public static final DeleteVisitor INSTANCE = new DeleteVisitor();

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException cause)
            throws IOException
        {
            Files.delete(dir);

            return FileVisitResult.CONTINUE;
        }


        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException
        {
            Files.delete(file);

            return FileVisitResult.CONTINUE;
        }
    }
}
