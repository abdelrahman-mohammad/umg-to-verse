package io.versetools.umgtoverse.converter.widget;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class Widget {
    protected Converter converter;
    @NonNull
    protected String name;
    @NonNull
    protected String exportPath;

    public Widget(Converter converter) {
        if (converter == null) throw new IllegalArgumentException("Parser cannot be null");
        this.converter = converter;
        this.name = "";
        this.exportPath = "";
    }

    public Widget parse(String objectStr) {
        if (objectStr == null || objectStr.isEmpty()) throw new IllegalArgumentException("Object string cannot be null or empty");

        for(String line : objectStr.split("\n")) System.out.println(PrintColor.ANSI_BLUE + line + PrintColor.ANSI_RESET);

        String objectName = WObject.parseName(objectStr);
        setName(objectName);
        setExportPath(WObject.parseExportPath(objectStr));

        System.out.println(objectName + PrintColor.ANSI_YELLOW + ": Parsing attributes..." + PrintColor.ANSI_RESET);
        parseWidgetSpecificAttributes(objectStr);
        System.out.println(objectName + PrintColor.ANSI_GREEN + ": Attributes parsed." + PrintColor.ANSI_RESET);

        return this;
    }

    // ============= Abstract methods =============
    protected abstract String getWidgetType();

    protected abstract void parseWidgetSpecificAttributes(String objectStr);

    protected abstract List<String> generateWidgetSpecificAttributes(int indent, Settings settings);

    // ============= Verse code generation =============
    public String generateVerseCode(int indent, Settings settings) {
        StringBuilder sb = new StringBuilder();
        List<String> attributesList;
        if(settings.getWidgetSettings(getWidgetType()).isVariable()) {
            sb.append(name).append(" := ").append(getWidgetType());
            attributesList = generateWidgetSpecificAttributes(0, settings);
        } else {
            sb.append(getIndent(indent)).append(getWidgetType());
            attributesList = generateWidgetSpecificAttributes(indent, settings);
        }

        if (settings.getWidgetSettings(getWidgetType()).isExpanded()) {
            sb.append(generateExpandedForm(attributesList, indent));
        } else {
            sb.append(generateCompactForm(attributesList));
        }

        return sb.toString();
    }

    protected String getIndent(int indent) {
        return "    ".repeat(indent);
    }

    protected String generateExpandedForm(List<String> content, int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(":\n");
        for (String line : content) {
            if (!line.trim().isEmpty()) {
                sb.append(getIndent(indent + 1)).append(line).append("\n");
            }
        }
        return sb.toString();
    }

    protected String generateCompactForm(List<String> content) {
        return String.format("{%s}", String.join(", ", content));
    }
}