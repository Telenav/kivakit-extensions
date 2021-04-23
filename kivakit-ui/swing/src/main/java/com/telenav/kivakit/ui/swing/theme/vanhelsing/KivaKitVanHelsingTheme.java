package com.telenav.kivakit.ui.swing.theme.vanhelsing;

import com.telenav.kivakit.ui.swing.graphics.color.Color;
import com.telenav.kivakit.ui.swing.graphics.style.Style;
import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import static com.telenav.kivakit.ui.swing.graphics.color.Color.GRAY;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.AQUA;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.BLUE_RIDGE_MOUNTAINS;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.CHARCOAL;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.FOSSIL;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.OCEAN;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.PLATINUM;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.SILVER;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.TANGERINE;
import static com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors.WHITE_SMOKE;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitVanHelsingTheme extends KivaKitTheme
{
    private final Color TEXT = CHARCOAL;

    private final Color FADED_TEXT = GRAY;

    private final Color DARK_BACKGROUND = CHARCOAL;

    private final Color MEDIUM_BACKGROUND = SILVER;

    private final Color HIGHLIGHT = AQUA.darkened();

    public KivaKitVanHelsingTheme()
    {
    }

    @Override
    public Color colorBorder()
    {
        return FOSSIL;
    }

    @Override
    public Color colorCaret()
    {
        return HIGHLIGHT;
    }

    @Override
    public Color colorPanel()
    {
        return PLATINUM;
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
                .withFont(fontNormal())
                .withTextColor(TEXT);
    }

    @Override
    public Style styleComponentLabel()
    {
        return Style.create()
                .withFont(fontSmall())
                .withForegroundColor(SILVER);
    }

    @Override
    public Style styleDropdown()
    {
        return Style.create()
                .withForegroundColor(HIGHLIGHT)
                .withBackgroundColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleInformationLabel()
    {
        return Style.create()
                .withFont(fontNormal())
                .withForegroundColor(BLUE_RIDGE_MOUNTAINS);
    }

    @Override
    public Style styleLabel()
    {
        return Style.create()
                .withFont(fontNormal())
                .withTextColor(TEXT);
    }

    @Override
    public Style styleList()
    {
        return Style.create()
                .withForegroundColor(AQUA.darkened())
                .withBackgroundColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleNote()
    {
        return Style.create()
                .withFont(fontSmall())
                .withTextColor(FADED_TEXT);
    }

    @Override
    public Style styleProgressBar()
    {
        return Style.create()
                .withFont(fontSmall())
                .withForegroundColor(FADED_TEXT)
                .withBackgroundColor(MEDIUM_BACKGROUND);
    }

    @Override
    public Style styleSelectedTab()
    {
        return Style.create()
                .withForegroundColor(HIGHLIGHT)
                .withBackgroundColor(colorPanel());
    }

    @Override
    public Style styleSelection()
    {
        return Style.create()
                .withForegroundColor(WHITE_SMOKE)
                .withBackgroundColor(OCEAN);
    }

    @Override
    public Style styleTab()
    {
        return Style.create()
                .withForegroundColor(TEXT)
                .withBackgroundColor(colorPanel());
    }

    @Override
    public Style styleTabHover()
    {
        return Style.create()
                .withForegroundColor(HIGHLIGHT)
                .withBackgroundColor(DARK_BACKGROUND.lightened());
    }

    @Override
    public Style styleTable()
    {
        return Style.create()
                .withForegroundColor(TANGERINE)
                .withBackgroundColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTableHeader()
    {
        return Style.create()
                .withForegroundColor(TEXT)
                .withBackgroundColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTextArea()
    {
        return Style.create()
                .withTextColor(TEXT)
                .withBackgroundColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTextField()
    {
        return Style.create()
                .withForegroundColor(PLATINUM)
                .withBackgroundColor(MEDIUM_BACKGROUND);
    }

    @Override
    public Style styleTitle()
    {
        return Style.create()
                .withFont(fontNormal().deriveFont(16.0f))
                .withForegroundColor(HIGHLIGHT)
                .withBackgroundColor(DARK_BACKGROUND);
    }
}
