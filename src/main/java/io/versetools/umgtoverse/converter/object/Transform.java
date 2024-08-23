package io.versetools.umgtoverse.converter.object;

import io.versetools.umgtoverse.converter.attribute.Vector2;
import io.versetools.umgtoverse.converter.util.PrintColor;

public class Transform {
    // Default values
    private static final Vector2 DEFAULT_TRANSLATION = new Vector2();
    private static final Vector2 DEFAULT_SCALE = new Vector2(1.0, 1.0);
    private static final Vector2 DEFAULT_SHEAR = new Vector2();
    private static final double DEFAULT_ANGLE = 0.0;

    // Attributes
    private Vector2 translation;
    private Vector2 scale;
    private Vector2 shear;
    private double angle;

    public Transform() {
        this.translation = DEFAULT_TRANSLATION;
        this.scale = DEFAULT_SCALE;
        this.shear = DEFAULT_SHEAR;
        this.angle = DEFAULT_ANGLE;
    }

    public static Transform parseTransform(String input) {
        input = input.replaceAll("^\\(|\\)$", "");

        String translationStr = WObject.parseAttribute(input, "Translation");
        String scaleStr = WObject.parseAttribute(input, "Scale");
        String shearStr = WObject.parseAttribute(input, "Shear");
        String angleStr = WObject.parseAttribute(input, "Angle");

        Transform transform = new Transform();
        if(!translationStr.isEmpty()) transform.translation = Vector2.parseVector2(translationStr);
        if(!scaleStr.isEmpty()) transform.scale = Vector2.parseVector2(scaleStr);
        if(!shearStr.isEmpty()) transform.shear = Vector2.parseVector2(shearStr);
        if(!angleStr.isEmpty()) transform.angle = Double.parseDouble(angleStr);

        System.out.println(PrintColor.ANSI_YELLOW + "Transform:\n\tInput: " + input);
        System.out.println("\tTranslation: " + transform.translation);
        System.out.println("\tScale: " + transform.scale);
        System.out.println("\tShear: " + transform.shear);
        System.out.println("\tAngle: " + transform.angle + PrintColor.ANSI_RESET);

        return transform;
    }

    @Override
    public String toString() {
        return String.format("(Translation=%s, Scale=%s, Shear=%s, Angle=%.5f)", translation, scale, shear, angle);
    }
}
