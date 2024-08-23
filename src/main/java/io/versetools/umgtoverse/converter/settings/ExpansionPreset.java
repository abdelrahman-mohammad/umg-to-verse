package io.versetools.umgtoverse.converter.settings;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ExpansionPreset {
    @Getter
    private final String name;
    private Map<String, Boolean> widgetExpansionStates;

    public ExpansionPreset(String name) {
        this.name = name;
        this.widgetExpansionStates = new HashMap<>();
    }

    public void setWidgetExpansion(String widgetType, boolean expanded) {
        widgetExpansionStates.put(widgetType, expanded);
    }

    public boolean isWidgetExpanded(String widgetType) {
        return widgetExpansionStates.getOrDefault(widgetType, false);
    }

    public boolean getWidgetExpansion(String widgetType) {
        return widgetExpansionStates.get(widgetType);
    }

    public Map<String, Boolean> getWidgetExpansionStates() {
        return new HashMap<>(widgetExpansionStates);
    }

    public void setWidgetExpansionStates(Map<String, Boolean> states) {
        this.widgetExpansionStates = new HashMap<>(states);
    }
}