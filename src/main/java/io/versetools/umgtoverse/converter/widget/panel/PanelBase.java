package io.versetools.umgtoverse.converter.widget.panel;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.widget.Widget;
import io.versetools.umgtoverse.converter.widget.slot.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PanelBase<T extends Slot> extends Widget {
    protected List<T> slots;

    public PanelBase(Converter converter) {
        super(converter);
        this.slots = new ArrayList<>();
    }

    public void addSlot(T slot) {
        if (slot == null) throw new IllegalArgumentException("Slot cannot be null");
        slots.add(slot);
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        parseSlots(objectStr);
        parsePanelSpecificAttributes(objectStr);
    }

    protected void parseSlots(String objectStr) {
        Pattern slotPattern = Pattern.compile("Begin Object Name=\"([^\"]*)\".*?End Object", Pattern.DOTALL);
        Matcher slotMatcher = slotPattern.matcher(objectStr);

        while (slotMatcher.find()) {
            String slotContent = slotMatcher.group();
            T slot = parseSlot(slotContent);
            if (slot != null) {
                slot.setParent(this);
                addSlot(slot);
            }
        }
    }

    protected abstract T parseSlot(String slotContent);

    protected abstract void parsePanelSpecificAttributes(String objectStr) throws IllegalArgumentException;

    protected abstract List<String> generatePanelSpecificAttributes(int indent, Settings settings);

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        list.addAll(generatePanelSpecificAttributes(indent, settings));
        list.addAll(generateSlots(indent, settings));

        return list;
    }

    protected List<String> generateSlots(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        if(settings.getWidgetSettings(getWidgetType()).isExpanded()) {
            list.add(getIndent(indent) + "Slots := array:");
            for (T slot : slots) list.add(slot.generateVerseCode(indent + 1, settings));
        } else {
            list.add("Slots := array{");
            for (T slot : slots) list.add(slot.generateVerseCode(0, settings));
            list.add("}");
        }

        return list;
    }
}