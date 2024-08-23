package io.versetools.umgtoverse.converter.settings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Settings {
    private String name;
    private Map<String, WidgetSettings> widgetsSettings;

    public Settings(String name) {
        this.name = name;
        this.widgetsSettings = new HashMap<>();

        String[] widgetTypes = {"canvas", "canvas_slot", "overlay", "overlay_slot", "stack_box", "stack_box_slot", "texture_block", "color_block", "text_block", "slider_regular", "button_loud", "button_quiet", "button_regular", "vector2", "margin", "anchors", "color"};
        for(String widgetType : widgetTypes) {
            boolean containsParent = widgetType.contains("canvas") || widgetType.contains("overlay") || widgetType.contains("stack_box");
            boolean equalsParent = widgetType.equals("canvas") || widgetType.equals("overlay") || widgetType.equals("stack_box");
            widgetsSettings.put(widgetType, new WidgetSettings(containsParent, false, equalsParent));
        }
    }

    public WidgetSettings getWidgetSettings(String widgetType) {
        return widgetsSettings.get(widgetType);
    }

    public void setWidgetSettings(String widgetType, WidgetSettings settings) {
        widgetsSettings.put(widgetType, settings);
    }

    public void updateWidgetSettings(String widgetType, boolean expanded, boolean includeDefaults, boolean variable) {
        WidgetSettings settings = widgetsSettings.get(widgetType);
        settings.setExpanded(expanded);
        settings.setIncludeDefaults(includeDefaults);
        settings.setVariable(variable);
    }
}