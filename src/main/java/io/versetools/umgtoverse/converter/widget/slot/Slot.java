package io.versetools.umgtoverse.converter.widget.slot;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public abstract class Slot extends Widget {
    protected Widget parent;
    protected Widget widget;

    public Slot(Converter converter) {
        super(converter);
        this.parent = null;
        this.widget = null;
    }

    public void setParent(Widget parent) {
        if (parent == null) throw new IllegalArgumentException("Parent widget cannot be null");
        this.parent = parent;
    }

    public void setWidget(Widget widget) {
        if (widget == null) throw new IllegalArgumentException("Child widget cannot be null");
        this.widget = widget;
    }

    protected abstract void parseSlotSpecificAttributes(String objectStr) throws IllegalArgumentException;

    protected abstract List<String> generateSlotSpecificAttributes(int indent, Settings settings);

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        String fullWidgetName = WObject.parseAttribute(objectStr, "Content");
        parseSlotSpecificAttributes(objectStr);
        System.out.println(PrintColor.ANSI_BLUE + "Widget: " + fullWidgetName + PrintColor.ANSI_RESET);

        String widgetName = fullWidgetName.indexOf('\'') != fullWidgetName.lastIndexOf('\'') ? fullWidgetName.substring(fullWidgetName.indexOf('\'') + 1, fullWidgetName.lastIndexOf('\'')) : fullWidgetName;
        setWidget(converter.createWidget(widgetName));
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>(generateSlotSpecificAttributes(indent, settings));
        list.add(getIndent(indent) + "Widget := " + widget.generateVerseCode(0, settings).trim().replace("\n", ""));

        return list;
    }


}