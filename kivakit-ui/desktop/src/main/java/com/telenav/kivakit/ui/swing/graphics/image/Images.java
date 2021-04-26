package com.telenav.kivakit.ui.swing.graphics.image;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * @author jonathanl (shibo)
 */
public class Images
{
    public static BufferedImage buffer(final Image image)
    {
        if (image instanceof BufferedImage)
        {
            return (BufferedImage) image;
        }
        else
        {
            final var width = image.getWidth(null);
            final var height = image.getHeight(null);
            final BufferedImage buffered = new BufferedImage(width, height, TYPE_INT_ARGB);
            final Graphics2D graphics = buffered.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(image, 0, 0, width, height, 0, 0, width, height, null);
            graphics.dispose();
            return buffered;
        }
    }
}
