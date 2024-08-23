package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

public enum TextJustification {
    Left,
    Center,
    Right,
    InvariantLeft,
    InvariantRight;

    public String generateVerseCode(int indent, Settings settings) {
        return "text_justification." + name();
    }

    public static TextJustification parseTextJustification(String input) {
        if(input == null || input.trim().isEmpty()) return Left;
        TextJustification justification = valueOf(input.trim());
        System.out.println(PrintColor.ANSI_GREEN + "Text justification: " + justification + PrintColor.ANSI_RESET);
        return justification;
    }
}