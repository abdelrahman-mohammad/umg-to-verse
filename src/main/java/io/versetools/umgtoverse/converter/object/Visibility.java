package io.versetools.umgtoverse.converter.object;

import io.versetools.umgtoverse.converter.util.PrintColor;

public enum Visibility {
    Visible, Collapsed, Hidden, HitTestInvisible, SelfHitTestInvisible;

    public static Visibility parseVisibility(String input) {
        if(input.trim().isEmpty()) return Visible;
        Visibility visibility = valueOf(input.trim());
        System.out.println(PrintColor.ANSI_YELLOW + "Visibility:\n\tInput: " + input);
        System.out.println("\tValue: " + visibility + PrintColor.ANSI_RESET);
        return visibility;
    }
}
