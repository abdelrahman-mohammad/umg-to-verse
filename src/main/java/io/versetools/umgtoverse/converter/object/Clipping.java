package io.versetools.umgtoverse.converter.object;

import io.versetools.umgtoverse.converter.util.PrintColor;

public enum Clipping {
    Inherit, ClipToBounds, ClipToBoundsWithoutIntersecting, ClipToBoundsAlways, OnDemand;

    public static Clipping parseClipping(String input) {
        if(input.trim().isEmpty()) return Inherit;
        Clipping clipping = valueOf(input.trim());
        System.out.println(PrintColor.ANSI_YELLOW + "Clipping:\n\tInput: " + input);
        System.out.println("\tValue: " + clipping + PrintColor.ANSI_RESET);
        return clipping;
    }
}
