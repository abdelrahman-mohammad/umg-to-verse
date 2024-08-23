package io.versetools.umgtoverse.converter.widget.common;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.attribute.Color;
import io.versetools.umgtoverse.converter.attribute.ImageTiling;
import io.versetools.umgtoverse.converter.attribute.Vector2;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class TextureBlock extends Widget {
    // Default values
    private static final Vector2 DEFAULT_DESIRED_SIZE = new Vector2(32.0, 32.0);
    private static final String DEFAULT_IMAGE = "<# Insert image path here #>";
    private static final Color DEFAULT_TINT = new Color();
    private static final ImageTiling DEFAULT_HORIZONTAL_TILING = ImageTiling.Repeat;
    private static final ImageTiling DEFAULT_VERTICAL_TILING = ImageTiling.Repeat;

    // Attributes
    private Vector2 desiredSize;
    private String image;
    private Color tint;
    private ImageTiling horizontalTiling;
    private ImageTiling verticalTiling;

    public TextureBlock(Converter converter) {
        super(converter);
        this.desiredSize = DEFAULT_DESIRED_SIZE;
        this.image = DEFAULT_IMAGE;
        this.tint = DEFAULT_TINT;
        this.horizontalTiling = DEFAULT_HORIZONTAL_TILING;
        this.verticalTiling = DEFAULT_VERTICAL_TILING;
    }

    @Override
    protected String getWidgetType() {
        return "texture_block";
    }

    private static String parseResourceObject(String input) {
        String path = input.substring(input.indexOf('\'') + 2, input.lastIndexOf('.')).replaceAll("/", ".");
        System.out.println(PrintColor.ANSI_GREEN + "Parsed resource object: " + path + PrintColor.ANSI_RESET);
        return path;
    }

    @Override
    protected void parseWidgetSpecificAttributes(String objectStr) {
        String brushStr = WObject.parseAttribute(objectStr, "Brush");
        String tilingStr = WObject.parseAttribute(brushStr, "Tiling");
        String imageTypeStr = WObject.parseAttribute(brushStr, "ImageType");
        String imageSizeStr = WObject.parseAttribute(brushStr, "ImageSize");
        String tintColorStr = WObject.parseAttribute(brushStr, "TintColor");
        String resourceObjectStr = WObject.parseAttribute(brushStr, "ResourceObject");

        if (!imageSizeStr.isEmpty()) this.desiredSize = Vector2.parseVector2(imageSizeStr);
        if(!resourceObjectStr.isEmpty()) this.image = parseResourceObject(resourceObjectStr);
        if (!tintColorStr.isEmpty()) this.tint = Color.parseColor(tintColorStr);
        if (!tilingStr.isEmpty()) {
            this.horizontalTiling = ImageTiling.Stretch;
            this.verticalTiling = ImageTiling.Stretch;
        }

        System.out.println(PrintColor.ANSI_BLUE + "DefaultDesiredSize: " + this.desiredSize + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultImage: " + this.image + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "DefaultTint: " + this.tint + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "Horizontal Tiling: " + this.horizontalTiling + PrintColor.ANSI_RESET);
        System.out.println(PrintColor.ANSI_BLUE + "Vertical Tiling: " + this.verticalTiling + PrintColor.ANSI_RESET);
    }

    @Override
    protected List<String> generateWidgetSpecificAttributes(int indent, Settings settings) {
        List<String> attributes = new ArrayList<>();
        attributes.add("DefaultImage := " + image);
        if(settings.getWidgetSettings(getWidgetType()).isIncludeDefaults()) {
            attributes.add("DefaultDesiredSize := " + desiredSize.generateVerseCode(indent, settings));
            attributes.add("DefaultTint := " + tint.generateVerseCode(indent, settings));
            attributes.add("DefaultHorizontalTiling := image_tiling." + horizontalTiling);
            attributes.add("DefaultVerticalTiling := image_tiling." + verticalTiling);
        } else {
            if(!desiredSize.equals(DEFAULT_DESIRED_SIZE)) attributes.add("DefaultDesiredSize := " + desiredSize.generateVerseCode(indent, settings));
            if(!tint.equals(DEFAULT_TINT)) attributes.add("DefaultTint := " + tint.generateVerseCode(indent, settings));
            if(horizontalTiling != DEFAULT_HORIZONTAL_TILING) attributes.add("DefaultHorizontalTiling := image_tiling." + horizontalTiling);
            if(verticalTiling != DEFAULT_VERTICAL_TILING) attributes.add("DefaultVerticalTiling := image_tiling." + verticalTiling);
        }

        return attributes;
    }
}
