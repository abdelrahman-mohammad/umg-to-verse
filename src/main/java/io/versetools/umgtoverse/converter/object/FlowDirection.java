package io.versetools.umgtoverse.converter.object;

import io.versetools.umgtoverse.converter.util.PrintColor;

public enum FlowDirection {
    Inherit, Culture, LeftToRight, RightToLeft;

    public static FlowDirection parseFlowDirection(String input) {
        if(input.trim().isEmpty()) return Inherit;
        FlowDirection direction = valueOf(input.trim());
        System.out.println(PrintColor.ANSI_YELLOW + "FlowDirection:\n\tInput: " + input);
        System.out.println("\tValue: " + direction + PrintColor.ANSI_RESET);
        return direction;
    }
}
