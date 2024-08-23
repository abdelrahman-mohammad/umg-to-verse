package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class Anchors extends Widget {
    // Default values
    private static final Vector2 DEFAULT_MINIMUM = new Vector2(0.0, 0.0);
    private static final Vector2 DEFAULT_MAXIMUM = new Vector2(0.0, 0.0);

    // Attributes
    private Vector2 minimum;
    private Vector2 maximum;

    public Anchors() {
        this.minimum = new Vector2();
        this.maximum = new Vector2();
    }

    public Anchors(Vector2 minimum, Vector2 maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public String getWidgetType() {
        return "anchors";
    }

    public static Anchors parseAnchors(String input) {

        String minimumStr = WObject.parseAttribute(input, "Minimum");
        String maximumStr = WObject.parseAttribute(input, "Maximum");

        Anchors anchors = new Anchors();
        if (!minimumStr.isEmpty()) anchors.minimum = Vector2.parseVector2(minimumStr);
        if (!maximumStr.isEmpty()) anchors.maximum = Vector2.parseVector2(maximumStr);

        System.out.println(PrintColor.ANSI_YELLOW + "Anchors:\n\tInput: " + input);
        System.out.println("\tMinimum: " + anchors.minimum);
        System.out.println("\tMaximum: " + anchors.maximum + PrintColor.ANSI_RESET);

        return anchors;
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        // TBA
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add("Minimum := " + minimum.generateVerseCode(indent, settings));
            list.add("Maximum := " + maximum.generateVerseCode(indent, settings));
        } else {
            if(!minimum.equals(DEFAULT_MINIMUM)) list.add("Minimum := " + minimum.generateVerseCode(indent, settings));
            if(!maximum.equals(DEFAULT_MAXIMUM)) list.add("Maximum := " + maximum.generateVerseCode(indent, settings));
        }
        return list;
    }

    @Override
    public String toString() {
        return "(" + minimum + ", " + maximum + ")";
    }
}