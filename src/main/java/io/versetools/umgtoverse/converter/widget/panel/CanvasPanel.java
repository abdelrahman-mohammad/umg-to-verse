package io.versetools.umgtoverse.converter.widget.panel;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.widget.slot.CanvasPanelSlot;

import java.util.ArrayList;
import java.util.List;

public class CanvasPanel extends PanelBase<CanvasPanelSlot> {
    public CanvasPanel(Converter converter) {
        super(converter);
    }

    @Override
    protected String getWidgetType() {
        return "canvas";
    }

    @Override
    protected CanvasPanelSlot parseSlot(String slotContent) {
        return (CanvasPanelSlot) new CanvasPanelSlot(converter).parse(slotContent);
    }

    @Override
    protected void parsePanelSpecificAttributes(String objectStr) throws IllegalArgumentException {
        // CanvasPanel doesn't have any specific attributes to parse
    }

    @Override
    protected List<String> generatePanelSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        // CanvasPanel doesn't have any specific attributes to generate

        return list;
    }
}