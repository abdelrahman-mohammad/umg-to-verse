package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class Margin extends Widget {
    // Default values
    private static final double DEFAULT_LEFT = 0.0;
    private static final double DEFAULT_TOP = 0.0;
    private static final double DEFAULT_RIGHT = 0.0;
    private static final double DEFAULT_BOTTOM = 0.0;

    // Attributes
    private double left;
    private double top;
    private double right;
    private double bottom;

    public Margin() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    public Margin(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public String getWidgetType() {
        return "margin";
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        // TBA
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()) {
            list.add("Left := " + left);
            list.add("Top := " + top);
            list.add("Right := " + right);
            list.add("Bottom := " + bottom);
        } else {
            if (left != DEFAULT_LEFT) list.add("Left := " + left);
            if (top != DEFAULT_TOP) list.add("Top := " + top);
            if (right != DEFAULT_RIGHT) list.add("Right := " + right);
            if (bottom != DEFAULT_BOTTOM) list.add("Bottom := " + bottom);
        }

        return list;
    }

    public static Margin parseMargin(String input) {
        input = input.replaceAll("^\\(|\\)$", "");

        String leftStr = WObject.parseAttribute(input, "Left");
        String topStr = WObject.parseAttribute(input, "Top");
        String rightStr = WObject.parseAttribute(input, "Right");
        String bottomStr = WObject.parseAttribute(input, "Bottom");

        Margin margin = new Margin();
        if (!leftStr.isEmpty()) margin.left = Double.parseDouble(leftStr);
        if (!topStr.isEmpty()) margin.top = Double.parseDouble(topStr);
        if (!rightStr.isEmpty()) margin.right = Double.parseDouble(rightStr);
        if (!bottomStr.isEmpty()) margin.bottom = Double.parseDouble(bottomStr);

        System.out.println(PrintColor.ANSI_YELLOW + "Margin:\n\tInput: " + input);
        System.out.println("\tLeft: " + margin.left);
        System.out.println("\tTop: " + margin.top);
        System.out.println("\tRight: " + margin.right);
        System.out.println("\tBottom: " + margin.bottom + PrintColor.ANSI_RESET);

        return margin;
    }

    @Override
    public String toString() {
        return String.format("(%.1f, %.1f, %.1f, %.1f)", left, top, right, bottom);
    }
}
