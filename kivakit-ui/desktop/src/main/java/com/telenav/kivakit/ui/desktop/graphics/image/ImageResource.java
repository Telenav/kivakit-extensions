package com.telenav.kivakit.ui.desktop.graphics.image;

import com.telenav.kivakit.resource.resources.packaged.PackageResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author jonathanl (shibo)
 */
public class ImageResource
{
    public static ImageResource of(final PackageResource resource)
    {
        return new ImageResource(resource);
    }

    public static ImageResource of(final Class<?> type, final String path)
    {
        return of(PackageResource.of(type, path));
    }

    private final PackageResource resource;

    public ImageResource(final PackageResource resource)
    {
        this.resource = resource;
    }

    public Image image()
    {
        try (final var input = resource.openForReading())
        {
            return ImageIO.read(input);
        }
        catch (final IOException ignored)
        {
            return null;
        }
    }

    public Image image(final int width, final int height)
    {
        return image().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
