package io.versetools.umgtoverse.converter.widget.button;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;

import java.util.ArrayList;
import java.util.List;

public class ButtonQuiet extends TextButtonBase {

    public ButtonQuiet(Converter converter) {
        super(converter);
    }

    @Override
    public String getWidgetType() {
        return "button_quiet";
    }

    @Override
    protected void parseButtonSpecificAttributes(String objectStr) {
        // Parse any ButtonQuiet specific attributes here
    }

    @Override
    protected List<String> generateButtonSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        // Add any ButtonQuiet specific attributes here

        return list;
    }
}