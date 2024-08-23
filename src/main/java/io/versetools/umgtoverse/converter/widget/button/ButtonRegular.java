package io.versetools.umgtoverse.converter.widget.button;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;

import java.util.ArrayList;
import java.util.List;

public class ButtonRegular extends TextButtonBase {

    public ButtonRegular(Converter converter) {
        super(converter);
    }

    @Override
    public String getWidgetType() {
        return "button_regular";
    }

    @Override
    protected void parseButtonSpecificAttributes(String objectStr) {
        // Parse any ButtonRegular specific attributes here
    }

    @Override
    protected List<String> generateButtonSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        // Add any ButtonRegular specific attributes here

        return list;
    }
}