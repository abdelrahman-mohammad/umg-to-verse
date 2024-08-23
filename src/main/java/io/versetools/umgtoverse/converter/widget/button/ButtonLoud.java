package io.versetools.umgtoverse.converter.widget.button;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;

import java.util.ArrayList;
import java.util.List;

public class ButtonLoud extends TextButtonBase {

    public ButtonLoud(Converter converter) {
        super(converter);
    }

    @Override
    public String getWidgetType() {
        return "button_loud";
    }

    @Override
    protected void parseButtonSpecificAttributes(String objectStr) {
        // Parse any ButtonLoud specific attributes here
    }

    @Override
    protected List<String> generateButtonSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        // Add any ButtonLoud specific attributes here

        return list;
    }
}