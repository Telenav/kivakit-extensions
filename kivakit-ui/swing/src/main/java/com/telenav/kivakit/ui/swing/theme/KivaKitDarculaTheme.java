package com.telenav.kivakit.ui.swing.theme;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.telenav.kivakit.ui.swing.graphics.color.Color;
import com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitDarculaTheme extends KivaKitTheme
{
    public KivaKitDarculaTheme()
    {
        UIManager.put("Button.arc", 15);

        UIManager.put("TabbedPane.minimumTabWidth", 150);

        UIManager.put("TabbedPane.foreground", new ColorUIResource(colorTabForeground().asAwtColor()));
        UIManager.put("TabbedPane.background", new ColorUIResource(colorTabBackground().asAwtColor()));

        UIManager.put("TabbedPane.selectedForeground", colorTabSelectedForeground().asAwtColor());
        UIManager.put("TabbedPane.selectedBackground", colorTabSelectedBackground().asAwtColor());

        UIManager.put("TabbedPane.hoverColor", KivaKitColors.BLACK_OLIVE.lightened().asAwtColor());
        UIManager.put("TabbedPane.underlineColor", KivaKitColors.DARK_KIVAKIT_YELLOW.asAwtColor());

        FlatDarculaLaf.install();
    }

    @Override
    public Color colorBorder()
    {
        return KivaKitColors.FOSSIL;
    }

    @Override
    public Color colorCaret()
    {
        return KivaKitColors.KIVAKIT_YELLOW;
    }

    @Override
    public Color colorComponentLabel()
    {
        return KivaKitColors.SMOKE;
    }

    @Override
    public Color colorDropdownBackground()
    {
        return KivaKitColors.BLACK_OLIVE;
    }

    @Override
    public Color colorDropdownText()
    {
        return KivaKitColors.DARK_KIVAKIT_YELLOW;
    }

    @Override
    public Color colorFadedText()
    {
        return KivaKitColors.PEWTER;
    }

    @Override
    public Color colorInformationLabel()
    {
        return KivaKitColors.BLUE_RIDGE_MOUNTAINS;
    }

    @Override
    public Color colorNote()
    {
        return colorFadedText();
    }

    @Override
    public Color colorProgressBarBackground()
    {
        return KivaKitColors.IRON;
    }

    @Override
    public Color colorProgressBarForeground()
    {
        return KivaKitColors.PEWTER;
    }

    @Override
    public Color colorSelectionBackground()
    {
        return KivaKitColors.OCEAN;
    }

    @Override
    public Color colorSelectionText()
    {
        return KivaKitColors.WHITE_SMOKE;
    }

    @Override
    public Color colorSeparator()
    {
        return colorBorder();
    }

    @Override
    public Color colorShadedPanel()
    {
        return KivaKitColors.CHARCOAL;
    }

    @Override
    public Color colorShadedSubPanel()
    {
        return KivaKitColors.IRON;
    }

    public Color colorTabBackground()
    {
        return colorShadedPanel();
    }

    public Color colorTabForeground()
    {
        return colorText();
    }

    public Color colorTabSelectedBackground()
    {
        return KivaKitColors.CHARCOAL;
    }

    public Color colorTabSelectedForeground()
    {
        return KivaKitColors.DARK_KIVAKIT_YELLOW;
    }

    @Override
    public Color colorTableBackground()
    {
        return KivaKitColors.DARK_CHARCOAL;
    }

    @Override
    public Color colorTableHeaderBackground()
    {
        return KivaKitColors.BLACK_OLIVE;
    }

    @Override
    public Color colorTableHeaderText()
    {
        return KivaKitColors.FOSSIL;
    }

    @Override
    public Color colorTableText()
    {
        return KivaKitColors.AQUA.darkened();
    }

    @Override
    public Color colorText()
    {
        return KivaKitColors.GAINSBORO;
    }

    @Override
    public Color colorTextAreaBackground()
    {
        return KivaKitColors.DARK_CHARCOAL;
    }

    @Override
    public Color colorTextFieldBackground()
    {
        return KivaKitColors.IRON;
    }

    @Override
    public Color colorTextFieldText()
    {
        return KivaKitColors.PLATINUM;
    }

    @Override
    public Color colorTitle()
    {
        return KivaKitColors.DARK_KIVAKIT_YELLOW;
    }

    @Override
    public Color colorTitleBackground()
    {
        return KivaKitColors.BLACK_OLIVE;
    }

    @Override
    public boolean isDark()
    {
        return true;
    }
}
