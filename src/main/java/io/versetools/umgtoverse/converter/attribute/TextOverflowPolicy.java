package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

public enum TextOverflowPolicy {
    Clip,
    Ellipsis;

    public String generateVerseCode(int indent, Settings settings) {
        return "text_overflow_policy." + name();
    }

    public static TextOverflowPolicy parseTextOverflowPolicy(String input) {
        if(input == null || input.trim().isEmpty()) return Clip;
        TextOverflowPolicy policy = valueOf(input.trim());
        System.out.println(PrintColor.ANSI_GREEN + "Text overflow policy: " + policy + PrintColor.ANSI_RESET);
        return policy;
    }
}