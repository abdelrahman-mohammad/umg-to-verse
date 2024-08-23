package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class Vector2 extends Widget {
    // Default values
    private static final double DEFAULT_X = 0.0;
    private static final double DEFAULT_Y = 0.0;

    // Attributes
    private double x;
    private double y;

    public Vector2() {
        this(DEFAULT_X, DEFAULT_Y);
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String getWidgetType() {
        return "vector2";
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        // TBA
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add("X := " + x);
            list.add("Y := " + y);
        } else {
            if(x != DEFAULT_X) list.add("X := " + x);
            if(y != DEFAULT_Y) list.add("Y := " + y);
        }

        return list;
    }

    public static Vector2 parseVector2(String input) {
        input = input.replaceAll("^\\(|\\)$", "");

        String xValue = WObject.parseAttribute(input, "X");
        String yValue = WObject.parseAttribute(input, "Y");

        Vector2 vector = new Vector2();
        if (!xValue.isEmpty()) vector.x = Float.parseFloat(xValue);
        if (!yValue.isEmpty()) vector.y = Float.parseFloat(yValue);

        System.out.println(PrintColor.ANSI_YELLOW + "Vector2:\n\tInput: " + input);
        System.out.println("\tX: " + vector.x);
        System.out.println("\tY: " + vector.y + PrintColor.ANSI_RESET);

        return vector;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        Vector2 other = (Vector2) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
