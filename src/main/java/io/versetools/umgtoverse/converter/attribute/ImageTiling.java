package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;

public enum ImageTiling {
    Stretch,
    Repeat;

    public static ImageTiling parseImageTiling(String input) {
        if(input == null || input.trim().isEmpty()) return Stretch;
        ImageTiling tiling = valueOf(input.trim());
        System.out.println(PrintColor.ANSI_GREEN + "Image tiling: " + tiling + PrintColor.ANSI_RESET);
        return tiling;
    }

    public String generateVerseCode(int indent, Settings settings) {
        return "image_tiling." + name();
    }
}