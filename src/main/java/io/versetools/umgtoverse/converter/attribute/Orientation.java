package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

public enum Orientation {
    Horizontal,
    Vertical;

    public String generateVerseCode(int indent, Settings settings) {
        return "orientation." + name();
    }

    public static Orientation parseOrientation(String input) {
        if(input == null || input.trim().isEmpty()) return Horizontal;
        String value = input.split("_")[1];
        Orientation orientation = valueOf(value.trim());
        System.out.println(PrintColor.ANSI_GREEN + "Orientation: " + orientation + PrintColor.ANSI_RESET);
        return orientation;
    }
}