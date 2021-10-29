package com.telenav.kivakit.ui.desktop.layout;

import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;

import javax.swing.border.AbstractBorder;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author jonathanl (shibo)
 */
public class RoundedBorder extends AbstractBorder
{
    private final Color color;

    public RoundedBorder(Color color)
    {
        this.color = color;
    }

    @Override
    public Insets getBorderInsets(Component component)
    {
        return new Insets(10, 10, 10, 10);
    }

    @Override
    public Insets getBorderInsets(Component component, Insets insets)
    {
        insets.set(10, 10, 10, 10);
        return insets;
    }

    public Shape getBorderShape(int x, int y, int width, int height)
    {
        return new RoundRectangle2D.Double(x, y, width, height, height, height);
    }

    @Override
    public void paintBorder(Component component, Graphics graphics, int x, int y,
                            int width, int height)
    {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape border = getBorderShape(x, y, width - 1, height - 1);
        g2.setPaint(Color.TRANSPARENT.asAwtColor());
        Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
        corner.subtract(new Area(border));
        g2.fill(corner);
        g2.setPaint(color.asAwtColor());
        g2.draw(border);
        g2.dispose();
    }
}
