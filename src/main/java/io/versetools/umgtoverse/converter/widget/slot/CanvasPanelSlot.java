package io.versetools.umgtoverse.converter.widget.slot;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.attribute.Anchors;
import io.versetools.umgtoverse.converter.attribute.Margin;
import io.versetools.umgtoverse.converter.attribute.Vector2;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

import java.util.ArrayList;
import java.util.List;

public class CanvasPanelSlot extends Slot {
    // Default values
    private static final Anchors DEFAULT_ANCHORS = new Anchors();
    private static final Margin DEFAULT_OFFSETS = new Margin();
    private static final boolean DEFAULT_SIZE_TO_CONTENT = false;
    private static final Vector2 DEFAULT_ALIGNMENT = new Vector2();
    private static final int DEFAULT_Z_ORDER = 0;

    // Attributes
    private Anchors anchors;
    private Margin offsets;
    private boolean sizeToContent;
    private Vector2 alignment;
    private int zOrder;

    public CanvasPanelSlot(Converter converter) {
        super(converter);
        this.anchors = DEFAULT_ANCHORS;
        this.offsets = DEFAULT_OFFSETS;
        this.sizeToContent = DEFAULT_SIZE_TO_CONTENT;
        this.alignment = DEFAULT_ALIGNMENT;
        this.zOrder = DEFAULT_Z_ORDER;
    }

    @Override
    protected String getWidgetType() {
        return "canvas_slot";
    }

    @Override
    protected void parseSlotSpecificAttributes(String objectStr) throws IllegalArgumentException {
        String offsets = WObject.parseAttribute(objectStr, "Offsets");
        String anchorsMin = WObject.parseAttribute(objectStr, "Minimum");
        String anchorsMax = WObject.parseAttribute(objectStr, "Maximum");
        String alignment = WObject.parseAttribute(objectStr, "Alignment");
        String sizeToContentStr = WObject.parseAttribute(objectStr, "bAutoSize");
        String zOrderStr = WObject.parseAttribute(objectStr, "ZOrder");

        if (!anchorsMin.isEmpty() && !anchorsMax.isEmpty()) {
            Vector2 minAnchors = Vector2.parseVector2(anchorsMin);
            Vector2 maxAnchors = Vector2.parseVector2(anchorsMax);
            this.anchors = new Anchors(minAnchors, maxAnchors);
        }
        if (!offsets.isEmpty()) this.offsets = Margin.parseMargin(offsets);
        if (!alignment.isEmpty()) this.alignment = Vector2.parseVector2(alignment);
        if (!sizeToContentStr.isEmpty()) this.sizeToContent = Boolean.parseBoolean(sizeToContentStr.trim());
        if (!zOrderStr.isEmpty()) this.zOrder = Integer.parseInt(zOrderStr.trim());

        System.out.println(PrintColor.ANSI_BLUE + "Anchors: " + this.anchors + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "Offsets: " + this.offsets + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "Alignment: " + this.alignment + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "SizeToContent: " + this.sizeToContent + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "ZOrder: " + this.zOrder + PrintColor.ANSI_RESET);
    }

    @Override
    protected List<String> generateSlotSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();

        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add(getIndent(indent) + "Anchors := " + anchors.generateVerseCode(0, settings));
            list.add(getIndent(indent) + "Offsets := " + offsets.generateVerseCode(0, settings));
            list.add(getIndent(indent) + "SizeToContent := " + sizeToContent);
            list.add(getIndent(indent) + "Alignment := " + alignment.generateVerseCode(0, settings));
            list.add(getIndent(indent) + "ZOrder := " + zOrder);
        } else {
            if (!anchors.equals(DEFAULT_ANCHORS))
                list.add(getIndent(indent) + "Anchors := " + anchors.generateVerseCode(0, settings));
            if (!offsets.equals(DEFAULT_OFFSETS))
                list.add(getIndent(indent) + "Offsets := " + offsets.generateVerseCode(0, settings));
            if (sizeToContent != DEFAULT_SIZE_TO_CONTENT)
                list.add(getIndent(indent) + "SizeToContent := " + sizeToContent);
            if (!alignment.equals(DEFAULT_ALIGNMENT))
                list.add(getIndent(indent) + "Alignment := " + alignment.generateVerseCode(0, settings));
            if (zOrder != DEFAULT_Z_ORDER) list.add(getIndent(indent) + "ZOrder := " + zOrder);
        }

        return list;
    }
}
