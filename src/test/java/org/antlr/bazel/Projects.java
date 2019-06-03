package org.antlr.bazel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class Projects
{
    private Projects()
    {
        super();
    }

    /**
     * Returns the absolute path for the given project relative path.
     *
     * @param path the project relative path.
     * @return the absolute path.
     *
     * @throws IOException if the real path could not be resolved.
     */
    public static Path path(String path)
    {
        Path result = Paths.get(path);

        // the folder structure is different when running under Bazel as the workspace
        // is linked under the runfiles directory
        if (Files.notExists(result))
        {
            Path external = Paths.get("external");

            if (Files.exists(external))
            {
                result = external.resolve(path);
            }
            else
            {
                Path root = Projects.eclipseFolder();

                if (root != null)
                {
                    result = root.resolve(path);
                }
            }
        }

        try
        {
            if (Files.exists(result))
            {
                result = result.toRealPath();
            }
        }
        catch (IOException ex)
        {
            throw new IllegalStateException(ex);
        }

        return result;
    }

    /**
     * Returns the Eclipse project folder.
     *
     * @return the Eclipse project folder.
     */
    private static Path eclipseFolder()
    {
        Path src = Paths.get(".project");

        // when using Eclipse the source files might not be under the workspace
        if (Files.exists(src))
        {
            try (BufferedReader in = Files.newBufferedReader(src))
            {
                InputSource inputXML = new InputSource( in );
                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList result = (NodeList)xpath.evaluate("//linkedResources/link/location", inputXML, XPathConstants.NODESET);

                for (int i = 0, size = result.getLength(); i < size; i++)
                {
                    Element item = (Element) result.item(i);
                    Matcher matcher = Pattern.compile(".*rules_antlr").matcher(item.getTextContent());

                    if (matcher.find())
                    {
                        return Paths.get(matcher.group());
                    }
                }
            }
            catch (Exception ex)
            {
                throw new IllegalStateException(ex);
            }
        }

        return null;
    }
}
