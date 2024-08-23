package io.versetools.umgtoverse.converter.widget.common;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.attribute.Color;
import io.versetools.umgtoverse.converter.attribute.Vector2;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class ColorBlock extends Widget {
    // Default values
    private static final Color DEFAULT_COLOR = new Color();
    private static final Vector2 DEFAULT_DESIRED_SIZE = new Vector2();
    private static final double DEFAULT_OPACITY = 1.0;

    // Attributes
    private final Color color;
    private final Vector2 desiredSize;
    private final double opacity;

    public ColorBlock(Converter converter) {
        super(converter);
        this.color = DEFAULT_COLOR;
        this.desiredSize = DEFAULT_DESIRED_SIZE;
        this.opacity = DEFAULT_OPACITY;
    }

    @Override
    public String getWidgetType() {
        return "color_block";
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        // ColorBlock doesn't have any specific attributes to parse (Isn't a part of UE widgets yet)
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add("Color := " + color.generateVerseCode(indent, settings));
            list.add("DesiredSize := " + desiredSize.generateVerseCode(indent, settings));
            list.add("Opacity := " + opacity);
        } else {
            if (!color.equals(DEFAULT_COLOR)) list.add("Color := " + color.generateVerseCode(indent, settings));
            if (!desiredSize.equals(DEFAULT_DESIRED_SIZE)) list.add("DesiredSize := " + desiredSize.generateVerseCode(indent, settings));
            if (opacity != DEFAULT_OPACITY) list.add("Opacity := " + opacity);
        }

        return list;
    }
}
