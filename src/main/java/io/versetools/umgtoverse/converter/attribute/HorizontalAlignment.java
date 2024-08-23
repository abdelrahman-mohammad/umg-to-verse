package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

public enum HorizontalAlignment {
    Left,
    Center,
    Right,
    Fill;

    public static HorizontalAlignment parseHorizontalAlignment(String input) {
        if(input == null || input.trim().isEmpty()) return Left;
        HorizontalAlignment alignment = valueOf(input.split("_")[1].trim());
        System.out.println(PrintColor.ANSI_GREEN + "Horizontal alignment: " + alignment + PrintColor.ANSI_RESET);
        return alignment;
    }

    public String generateVerseCode(int indent, Settings settings) {
        return "horizontal_alignment." + name();
    }
}