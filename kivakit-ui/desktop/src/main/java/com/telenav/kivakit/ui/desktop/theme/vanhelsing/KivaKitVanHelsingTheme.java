package com.telenav.kivakit.ui.desktop.theme.vanhelsing;

import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;
import com.telenav.kivakit.ui.desktop.theme.KivaKitColors;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitVanHelsingTheme extends KivaKitTheme
{
    private final Color TEXT = KivaKitColors.CHARCOAL;

    private final Color FADED_TEXT = Color.GRAY;

    private final Color DARK_BACKGROUND = KivaKitColors.CHARCOAL;

    private final Color MEDIUM_BACKGROUND = KivaKitColors.SILVER;

    private final Color HIGHLIGHT = KivaKitColors.AQUA.darker();

    public KivaKitVanHelsingTheme()
    {
    }

    @Override
    public Color colorBorder()
    {
        return KivaKitColors.FOSSIL;
    }

    @Override
    public Color colorCaret()
    {
        return HIGHLIGHT;
    }

    @Override
    public Color colorPanel()
    {
        return KivaKitColors.PLATINUM;
    }

    @Override
    public Color colorSeparator()
    {
        return colorBorder();
    }

    @Override
    public Color colorSubPanel()
    {
        return MEDIUM_BACKGROUND;
    }

    @Override
    public boolean isDark()
    {
        return false;
    }

    @Override
    public Style styleButton()
    {
        return Style.create()
                .withTextFont(fontNormal())
                .withTextColor(TEXT);
    }

    @Override
    public Style styleCaption()
    {
        return styleTitle().withTextFont(fontNormal().deriveFont(11.0f));
    }

    @Override
    public Style styleComponentLabel()
    {
        return Style.create()
                .withTextFont(fontSmall())
                .withDrawColor(KivaKitColors.SILVER);
    }

    @Override
    public Style styleDropdown()
    {
        return Style.create()
                .withDrawColor(HIGHLIGHT)
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleInformationLabel()
    {
        return Style.create()
                .withTextFont(fontNormal())
                .withDrawColor(KivaKitColors.BLUE_RIDGE_MOUNTAINS);
    }

    @Override
    public Style styleLabel()
    {
        return Style.create()
                .withTextFont(fontNormal())
                .withTextColor(TEXT);
    }

    @Override
    public Style styleList()
    {
        return Style.create()
                .withDrawColor(KivaKitColors.AQUA.darker())
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleNote()
    {
        return Style.create()
                .withTextFont(fontSmall())
                .withTextColor(FADED_TEXT);
    }

    @Override
    public Style styleProgressBar()
    {
        return Style.create()
                .withTextFont(fontSmall())
                .withDrawColor(FADED_TEXT)
                .withFillColor(MEDIUM_BACKGROUND);
    }

    @Override
    public Style styleSelectedTab()
    {
        return Style.create()
                .withDrawColor(HIGHLIGHT)
                .withFillColor(colorPanel());
    }

    @Override
    public Style styleSelection()
    {
        return Style.create()
                .withDrawColor(KivaKitColors.WHITE_SMOKE)
                .withFillColor(KivaKitColors.OCEAN);
    }

    @Override
    public Style styleTab()
    {
        return Style.create()
                .withDrawColor(TEXT)
                .withFillColor(colorPanel());
    }

    @Override
    public Style styleTabHover()
    {
        return Style.create()
                .withDrawColor(HIGHLIGHT)
                .withFillColor(DARK_BACKGROUND.brighter());
    }

    @Override
    public Style styleTable()
    {
        return Style.create()
                .withDrawColor(KivaKitColors.TANGERINE)
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTableHeader()
    {
        return Style.create()
                .withDrawColor(TEXT)
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTextArea()
    {
        return Style.create()
                .withTextColor(TEXT)
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTextField()
    {
        return Style.create()
                .withDrawColor(KivaKitColors.PLATINUM)
                .withFillColor(MEDIUM_BACKGROUND);
    }

    @Override
    public Style styleTitle()
    {
        return Style.create()
                .withTextFont(fontNormal().deriveFont(16.0f))
                .withDrawColor(HIGHLIGHT)
                .withFillColor(DARK_BACKGROUND);
    }
}
