package io.versetools.umgtoverse.converter.attribute;

import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Color extends Widget {
    // Default values
    private static final double DEFAULT_R = 1.0;
    private static final double DEFAULT_G = 1.0;
    private static final double DEFAULT_B = 1.0;
    private static final double DEFAULT_A = 1.0;

    // Attributes
    private double r;
    private double g;
    private double b;
    @Getter
    private double a;

    public Color() {
        this(DEFAULT_R, DEFAULT_G, DEFAULT_B, DEFAULT_A);
    }

    public Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public String getWidgetType() {
        return "color";
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        // TBA
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> list = new ArrayList<>();

        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()){
            list.add("R := " + r);
            list.add("G := " + g);
            list.add("B := " + b);
        } else {
            if (r != DEFAULT_R) list.add("R := " + r);
            if (g != DEFAULT_G) list.add("G := " + g);
            if (b != DEFAULT_B) list.add("B := " + b);
        }

        return list;
    }

    public static Color parseColor(String input) {
        input = input.replaceAll("^\\(|\\)$", "");

        String rStr = WObject.parseAttribute(input, "R").replaceAll("^\\(|\\)$", "");;
        String gStr = WObject.parseAttribute(input, "G").replaceAll("^\\(|\\)$", "");;
        String bStr = WObject.parseAttribute(input, "B").replaceAll("^\\(|\\)$", "");;
        String aStr = WObject.parseAttribute(input, "A").replaceAll("^\\(|\\)$", "");;

        Color color = new Color();
        if (!rStr.isEmpty()) color.r = Double.parseDouble(rStr);
        if (!gStr.isEmpty()) color.g = Double.parseDouble(gStr);
        if (!bStr.isEmpty()) color.b = Double.parseDouble(bStr);
        if (!aStr.isEmpty()) color.a = Double.parseDouble(aStr);

        System.out.println(PrintColor.ANSI_YELLOW + "Color:\n\tInput: " + input);
        System.out.println("\tR: " + color.r);
        System.out.println("\tG: " + color.g);
        System.out.println("\tB: " + color.b);
        System.out.println("\tA: " + color.a + PrintColor.ANSI_RESET);

        return color;
    }

    @Override
    public String toString() {
        return String.format("(%.5f, %.5f, %.5f, %.5f)", r, g, b, a);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Color color = (Color) obj;
        return r == color.r && g == color.g && b == color.b && a == color.a;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(r, g, b, a);
    }
}
