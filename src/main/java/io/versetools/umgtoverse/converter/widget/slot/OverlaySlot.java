package io.versetools.umgtoverse.converter.widget.slot;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.attribute.HorizontalAlignment;
import io.versetools.umgtoverse.converter.attribute.Margin;
import io.versetools.umgtoverse.converter.attribute.VerticalAlignment;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

import java.util.ArrayList;
import java.util.List;

public class OverlaySlot extends Slot {
    // Default values
    private static final HorizontalAlignment DEFAULT_HORIZONTAL_ALIGNMENT = HorizontalAlignment.Center;
    private static final VerticalAlignment DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignment.Center;
    private static final Margin DEFAULT_PADDING = new Margin();

    // Attributes
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private Margin padding;

    public OverlaySlot(Converter converter) {
        super(converter);
        this.horizontalAlignment = DEFAULT_HORIZONTAL_ALIGNMENT;
        this.verticalAlignment = DEFAULT_VERTICAL_ALIGNMENT;
        this.padding = DEFAULT_PADDING;
    }

    @Override
    protected String getWidgetType() {
        return "overlay_slot";
    }

    @Override
    protected void parseSlotSpecificAttributes(String objectStr) throws IllegalArgumentException {
        String padding = WObject.parseAttribute(objectStr, "Padding");
        String horizontalAlignment = WObject.parseAttribute(objectStr, "HorizontalAlignment");
        String verticalAlignment = WObject.parseAttribute(objectStr, "VerticalAlignment");

        if (!padding.isEmpty()) this.padding = Margin.parseMargin(padding);
        if (!horizontalAlignment.isEmpty()) this.horizontalAlignment = HorizontalAlignment.parseHorizontalAlignment(horizontalAlignment);
        if (!verticalAlignment.isEmpty()) this.verticalAlignment =  VerticalAlignment.parseVerticalAlignment(verticalAlignment);

        System.out.println(PrintColor.ANSI_BLUE + "Padding: " + this.padding + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "HorizontalAlignment: " + this.horizontalAlignment + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "VerticalAlignment: " + this.verticalAlignment + PrintColor.ANSI_RESET);
    }

    @Override
    protected List<String> generateSlotSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();

        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add(getIndent(indent) + "HorizontalAlignment := horizontal_alignment." + horizontalAlignment);
            list.add(getIndent(indent) + "VerticalAlignment := vertical_alignment." + verticalAlignment);
            list.add(getIndent(indent) + "Padding := " + padding.generateVerseCode(0, settings));
        } else {
            if (horizontalAlignment != DEFAULT_HORIZONTAL_ALIGNMENT)
                list.add(getIndent(indent) + "HorizontalAlignment := horizontal_alignment." + horizontalAlignment);
            if (verticalAlignment != DEFAULT_VERTICAL_ALIGNMENT)
                list.add(getIndent(indent) + "VerticalAlignment := vertical_alignment." + verticalAlignment);
            if (padding != DEFAULT_PADDING)
                list.add(getIndent(indent) + "Padding := " + padding.generateVerseCode(0, settings));
        }

        return list;
    }
}
