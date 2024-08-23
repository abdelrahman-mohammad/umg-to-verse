package io.versetools.umgtoverse.converter.widget.button;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public abstract class TextButtonBase extends Widget {
    protected String defaultText;

    public TextButtonBase(Converter converter) {
        super(converter);
        this.defaultText = "BUTTON";
    }

    protected abstract void parseButtonSpecificAttributes(String objectStr);

    protected abstract List<String> generateButtonSpecificAttributes(int indent, Settings settings);

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        String isBorderAdditiveStr = WObject.parseAttribute(objectStr, "IsBorderAdditive");
        String minWidthStr = WObject.parseAttribute(objectStr, "MinWidth");
        String minHeightStr = WObject.parseAttribute(objectStr, "MinHeight");
        String bSelectableStr = WObject.parseAttribute(objectStr, "bSelectable");
        String bShouldSelectUponReceivingFocusStr = WObject.parseAttribute(objectStr, "bShouldSelectUponReceivingFocus");
        String bInteractableWhenSelectedStr = WObject.parseAttribute(objectStr, "bInteractableWhenSelected");
        String clickMethodStr = WObject.parseAttribute(objectStr, "ClickMethod");
        String touchMethodStr = WObject.parseAttribute(objectStr, "TouchMethod");
        String pressMethodStr = WObject.parseAttribute(objectStr, "PressMethod");
        String inputPriorityStr = WObject.parseAttribute(objectStr, "InputPriority");
        String bIsFocusableStr = WObject.parseAttribute(objectStr, "bIsFocusable");

        String defaultTextStr = WObject.parseAttribute(objectStr, "Text", "INVTEXT\\(\"([^\"]*)\"\\)");
        if (!defaultTextStr.isEmpty()) this.defaultText = defaultTextStr;

        parseButtonSpecificAttributes(objectStr);

        System.out.println(PrintColor.ANSI_BLUE + "DefaultText: " + this.defaultText + PrintColor.ANSI_RESET);
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>(generateButtonSpecificAttributes(indent, settings));
        list.add("DefaultText := \"" + defaultText + "\"");

        return list;
    }

}