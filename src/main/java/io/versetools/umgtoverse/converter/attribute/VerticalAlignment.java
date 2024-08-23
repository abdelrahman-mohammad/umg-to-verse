package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

public enum VerticalAlignment {
    Top,
    Center,
    Bottom,
    Fill;

    public static VerticalAlignment parseVerticalAlignment(String input) {
        if(input == null || input.trim().isEmpty()) return Top;
        VerticalAlignment alignment = valueOf(input.split("_")[1].trim());
        System.out.println(PrintColor.ANSI_GREEN + "Vertical alignment: " + alignment + PrintColor.ANSI_RESET);
        return alignment;
    }

    public String generateVerseCode(int indent, Settings settings) {
        return "vertical_alignment." + name();
    }
}