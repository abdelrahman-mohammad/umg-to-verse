package io.versetools.umgtoverse.converter.object;

import io.versetools.umgtoverse.converter.attribute.Vector2;
import io.versetools.umgtoverse.converter.util.PrintColor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ToString
@Getter
@Setter
public class WObject {
    @ToString.Exclude
    private String objectStr;
    private String className;
    private String name;
    private String exportPath;
    private boolean flipForRightToLeftFlowDirection = false;
    private Transform renderTransform = new Transform();
    private Vector2 renderTransformPivot = new Vector2(0.5, 0.5);
    private FlowDirection flowDirectionPreference = FlowDirection.LeftToRight;
    private boolean isEnabled = true;
    private Clipping clipping = Clipping.Inherit;
    private Visibility visibility = Visibility.SelfHitTestInvisible;

    public WObject(String objectStr) {
        this.objectStr = objectStr;
        parseObject();
    }

    private void parseObject() {
        System.out.println(PrintColor.ANSI_PURPLE + "Parsing object..." + PrintColor.ANSI_RESET);
        String className = parseClass(objectStr);
        String name = parseName(objectStr);

        if(name.isEmpty() || className.isEmpty()) throw new IllegalArgumentException("non-valid object string.");

        System.out.println(PrintColor.ANSI_GREEN + "Found object: " + className + "'" + name + "'" + PrintColor.ANSI_RESET);

        this.className = className;
        this.name = name;
        this.exportPath = parseExportPath(objectStr);

        String flipForRightToLeftFlowDirectionStr = parseAttribute(objectStr, "bFlipForRightToLeftFlowDirection");
        if(!flipForRightToLeftFlowDirectionStr.isEmpty()) this.flipForRightToLeftFlowDirection = Boolean.parseBoolean(flipForRightToLeftFlowDirectionStr);

        String renderTransformStr = parseAttribute(objectStr, "RenderTransform");
        if(!renderTransformStr.isEmpty()) this.renderTransform = Transform.parseTransform(renderTransformStr);

        String renderTransformPivotStr = parseAttribute(objectStr, "RenderTransformPivot");
        if(!renderTransformPivotStr.isEmpty()) this.renderTransformPivot = Vector2.parseVector2(renderTransformPivotStr);

        String flowDirectionPreferenceStr = parseAttribute(objectStr, "FlowDirectionPreference");
        if(!flowDirectionPreferenceStr.isEmpty()) this.flowDirectionPreference = FlowDirection.parseFlowDirection(flowDirectionPreferenceStr);

        String isEnabledStr = parseAttribute(objectStr, "bIsEnabled");
        if(!isEnabledStr.isEmpty()) this.isEnabled = Boolean.parseBoolean(isEnabledStr);

        String clippingStr = parseAttribute(objectStr, "Clipping");
        if(!clippingStr.isEmpty()) this.clipping = Clipping.parseClipping(clippingStr);

        String visibilityStr = parseAttribute(objectStr, "Visibility");
        if(!visibilityStr.isEmpty()) this.visibility = Visibility.parseVisibility(visibilityStr);

        System.out.println(PrintColor.ANSI_PURPLE + "Parsed object!\n" + this + PrintColor.ANSI_RESET);
    }

    public static String parseClass(String objectStr) {
        Pattern classPattern = Pattern.compile("Class=(\\S+)");
        Matcher matcher = classPattern.matcher(objectStr);
        if(matcher.find()) {
            String className = matcher.group(1);
            return className.substring(className.lastIndexOf('.') + 1);
        } else {
            return "";
        }
    }

    public static String parseName(String objectStr) {
        Pattern namePattern = Pattern.compile("Name=\"([^\"]*)\"");
        Matcher matcher = namePattern.matcher(objectStr);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static String parseExportPath(String objectStr) {
        Pattern exportPathPattern = Pattern.compile("ExportPath=\"([^\"]*)\"");
        Matcher matcher = exportPathPattern.matcher(objectStr);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static String parseAttribute(String input, String attributeName) {
        Pattern pattern = Pattern.compile("%s=(\\((?:[^()]+|\\((?:[^()]+|\\([^()]*\\))*\\))*\\)|[^,\\n]+)".formatted(attributeName));
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static String parseAttribute(String input, String attributeName, String valuePattern) {
        Pattern pattern = Pattern.compile("(\\w+)=" + valuePattern);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.group(1).equals(attributeName)) {
                return matcher.group(2);
            }
        }
        return "";
    }
}
