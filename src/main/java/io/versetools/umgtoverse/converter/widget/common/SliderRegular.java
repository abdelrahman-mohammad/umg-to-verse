package io.versetools.umgtoverse.converter.widget.common;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class SliderRegular extends Widget {
    // Default values
    private static final double DEFAULT_MAX_VALUE = 1.0;
    private static final double DEFAULT_MIN_VALUE = 0.0;
    private static final double DEFAULT_STEP_SIZE = 0.1;
    private static final double DEFAULT_VALUE = 0.0;

    // Attributes
    private final double defaultMaxValue;
    private final double defaultMinValue;
    private final double defaultStepSize;
    private final double defaultValue;

    public SliderRegular(Converter converter) {
        super(converter);
        this.defaultMaxValue = DEFAULT_MAX_VALUE;
        this.defaultMinValue = DEFAULT_MIN_VALUE;
        this.defaultStepSize = DEFAULT_STEP_SIZE;
        this.defaultValue = DEFAULT_VALUE;
    }

    @Override
    public String getWidgetType() {
        return "slider_regular";
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        // Slider doesn't have any specific attributes to parse
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()) {
            list.add("DefaultMaxValue := " + DEFAULT_MAX_VALUE);
            list.add("DefaultMinValue := " + DEFAULT_MIN_VALUE);
            list.add("DefaultStepSize := " + DEFAULT_STEP_SIZE);
            list.add("DefaultValue := " + DEFAULT_VALUE);
        } else {
            if(defaultMaxValue != DEFAULT_MAX_VALUE) list.add("DefaultMaxValue := " + defaultMaxValue);
            if(defaultMinValue != DEFAULT_MIN_VALUE) list.add("DefaultMinValue := " + defaultMinValue);
            if(defaultStepSize != DEFAULT_STEP_SIZE) list.add("DefaultStepSize := " + defaultStepSize);
            if(defaultValue != DEFAULT_VALUE) list.add("DefaultValue := " + defaultValue);
        }

        return list;
    }
}
