package io.versetools.umgtoverse.converter.widget.common;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.attribute.Color;
import io.versetools.umgtoverse.converter.attribute.TextJustification;
import io.versetools.umgtoverse.converter.attribute.TextOverflowPolicy;
import io.versetools.umgtoverse.converter.attribute.Vector2;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class TextBlock extends Widget {
    // Default values
    private static final TextJustification DEFAULT_JUSTIFICATION = TextJustification.Left;
    private static final TextOverflowPolicy DEFAULT_OVERFLOW_POLICY = TextOverflowPolicy.Clip;
    private static final Color DEFAULT_SHADOW_COLOR = new Color();
    private static final Vector2 DEFAULT_SHADOW_OFFSET = new Vector2();
    private static final double DEFAULT_SHADOW_OPACITY = 1.0;
    private static final String DEFAULT_TEXT = "Text Block";
    private static final Color DEFAULT_TEXT_COLOR = new Color();
    private static final double DEFAULT_TEXT_OPACITY = 1.0;

    // Attributes
    private TextJustification justification;
    private TextOverflowPolicy overflowPolicy;
    private Color shadowColor;
    private Vector2 shadowOffset;
    private double shadowOpacity;
    private String text;
    private Color textColor;
    private double textOpacity;

    public TextBlock(Converter converter) {
        super(converter);
        this.justification = DEFAULT_JUSTIFICATION;
        this.overflowPolicy = DEFAULT_OVERFLOW_POLICY;
        this.shadowColor = new Color();
        this.shadowOffset = new Vector2();
        this.shadowOpacity = DEFAULT_SHADOW_OPACITY;
        this.text = DEFAULT_TEXT;
        this.textColor = new Color();
        this.textOpacity = DEFAULT_TEXT_OPACITY;
    }

    @Override
    protected String getWidgetType() {
        return "text_block";
    }

    @Override
        protected void parseWidgetSpecificAttributes(String objectStr) {
        String textStr = WObject.parseAttribute(objectStr, "Text", "NSLOCTEXT\\(\"[^\"]*\", \"[^\"]*\", \"([^\"]*)\"\\)");
        String colorAndOpacityStr = WObject.parseAttribute(objectStr, "ColorAndOpacity");
        String color = WObject.parseAttribute(colorAndOpacityStr, "SpecifiedColor");

        String fontStr = WObject.parseAttribute(objectStr, "Font");
        String shadowOffsetStr = WObject.parseAttribute(objectStr, "ShadowOffset");
        String shadowColorAndOpacityStr = WObject.parseAttribute(objectStr, "ShadowColorAndOpacity");
        String minDesiredWidthStr = WObject.parseAttribute(objectStr, "MinDesiredWidth");
        String textTransformPolicyStr = WObject.parseAttribute(objectStr, "TextTransformPolicy");
        String justificationStr = WObject.parseAttribute(objectStr, "Justification");
        String overflowPolicyStr = WObject.parseAttribute(objectStr, "TextOverflowPolicy");

        if (!textStr.isEmpty()) this.text = textStr;
        if (!colorAndOpacityStr.isEmpty()) {
            this.textColor = Color.parseColor(color);
            this.textOpacity = this.textColor.getA();
        }
        if (!shadowOffsetStr.isEmpty()) this.shadowOffset = Vector2.parseVector2(shadowOffsetStr);
        if (!shadowColorAndOpacityStr.isEmpty()) {
            this.shadowColor = Color.parseColor(shadowColorAndOpacityStr);
            this.shadowOpacity = this.shadowColor.getA();
        }
        if (!justificationStr.isEmpty()) this.justification = TextJustification.parseTextJustification(justificationStr);
        if (!overflowPolicyStr.isEmpty()) this.overflowPolicy = TextOverflowPolicy.parseTextOverflowPolicy(overflowPolicyStr);

        System.out.println(PrintColor.ANSI_BLUE + "DefaultText: " + text + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultTextColor: " + textColor + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultTextOpacity: " + textOpacity + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultShadowOffset: " + shadowOffset + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultShadowColor: " + shadowColor + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultShadowOpacity: " + shadowOpacity + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultJustification: " + justification + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultOverflowPolicy: " + overflowPolicy + PrintColor.ANSI_RESET);
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        list.add("DefaultText := \"" + text + "\"");
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add("DefaultTextColor := " + textColor.generateVerseCode(0, settings));
            list.add("DefaultTextOpacity := " + textOpacity);
            list.add("DefaultShadowOffset := " + shadowOffset.generateVerseCode(0, settings));
            list.add("DefaultShadowColor := " + shadowColor.generateVerseCode(0, settings));
            list.add("DefaultShadowOpacity := " + shadowOpacity);
            list.add("DefaultOverflowPolicy := " + overflowPolicy.generateVerseCode(0, settings));
            list.add("DefaultJustification := " + justification.generateVerseCode(0, settings));
        } else {
            if (!textColor.equals(DEFAULT_TEXT_COLOR))
                list.add("DefaultTextColor := " + textColor.generateVerseCode(0, settings));
            if (textOpacity != DEFAULT_TEXT_OPACITY) list.add("DefaultTextOpacity := " + textOpacity);
            if (!shadowOffset.equals(DEFAULT_SHADOW_OFFSET))
                list.add("DefaultShadowOffset := " + shadowOffset.generateVerseCode(0, settings));
            if (!shadowColor.equals(DEFAULT_SHADOW_COLOR))
                list.add("DefaultShadowColor := " + shadowColor.generateVerseCode(0, settings));
            if (shadowOpacity != DEFAULT_SHADOW_OPACITY) list.add("DefaultShadowOpacity := " + shadowOpacity);
            if (overflowPolicy != DEFAULT_OVERFLOW_POLICY)
                list.add("DefaultOverflowPolicy := " + overflowPolicy.generateVerseCode(0, settings));
            if (justification != DEFAULT_JUSTIFICATION)
                list.add("DefaultJustification := " + justification.generateVerseCode(0, settings));
        }

        return list;
    }
}
