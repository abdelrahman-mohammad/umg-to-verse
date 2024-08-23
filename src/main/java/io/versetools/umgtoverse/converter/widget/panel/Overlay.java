package io.versetools.umgtoverse.converter.widget.panel;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.widget.slot.OverlaySlot;

import java.util.ArrayList;
import java.util.List;

public class Overlay extends PanelBase<OverlaySlot> {
    public Overlay(Converter converter) {
        super(converter);
    }

    @Override
    public String getWidgetType() {
        return "overlay";
    }

    @Override
    protected OverlaySlot parseSlot(String slotContent) {
        return (OverlaySlot) new OverlaySlot(converter).parse(slotContent);
    }

    @Override
    protected void parsePanelSpecificAttributes(String objectStr) throws IllegalArgumentException {
        // Overlay doesn't have any specific attributes to parse
    }

    @Override
    protected List<String> generatePanelSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        // Overlay doesn't have any specific attributes to generate

        return list;
    }
}