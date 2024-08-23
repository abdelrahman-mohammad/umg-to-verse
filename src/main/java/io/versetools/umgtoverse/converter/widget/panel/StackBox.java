package io.versetools.umgtoverse.converter.widget.panel;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.attribute.Orientation;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.widget.slot.StackBoxSlot;

import java.util.ArrayList;
import java.util.List;

public class StackBox extends PanelBase<StackBoxSlot> {
    private Orientation orientation;

    public StackBox(Converter converter) {
        super(converter);
        this.orientation = Orientation.Vertical;
    }

    @Override
    public String getWidgetType() {
        return "stack_box";
    }

    @Override
    protected StackBoxSlot parseSlot(String slotContent) {
        return (StackBoxSlot) new StackBoxSlot(converter).parse(slotContent);
    }

    @Override
    protected void parsePanelSpecificAttributes(String objectStr) throws IllegalArgumentException {
        String orientationStr = WObject.parseAttribute(objectStr, "Orientation");
        if (!orientationStr.isEmpty()) this.orientation = Orientation.parseOrientation(orientationStr);
    }

    @Override
    protected List<String> generatePanelSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add("Orientation := orientation." + orientation);
        } else {
            if (orientation != Orientation.Vertical) list.add("Orientation := orientation." + orientation);
        }

        return list;
    }
}