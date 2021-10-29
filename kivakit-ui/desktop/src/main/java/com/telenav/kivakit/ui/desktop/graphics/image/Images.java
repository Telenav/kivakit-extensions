package com.telenav.kivakit.ui.desktop.graphics.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * @author jonathanl (shibo)
 */
public class Images
{
    public static BufferedImage buffer(Image image)
    {
        if (image instanceof BufferedImage)
        {
            return (BufferedImage) image;
        }
        else
        {
            var width = image.getWidth(null);
            var height = image.getHeight(null);
            BufferedImage buffered = new BufferedImage(width, height, TYPE_INT_ARGB);
            Graphics2D graphics = buffered.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(image, 0, 0, width, height, 0, 0, width, height, null);
            graphics.dispose();
            return buffered;
        }
    }
}
