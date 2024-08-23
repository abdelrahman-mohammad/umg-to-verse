package io.versetools.umgtoverse.converter.settings;

import java.util.ArrayList;
import java.util.List;

public class PresetManager {
    private List<ExpansionPreset> presets;
    private ExpansionPreset currentPreset;

    public PresetManager() {
        presets = new ArrayList<>();
        initializeDefaultPresets();
    }

    private void initializeDefaultPresets() {
        ExpansionPreset fullyExpanded = new ExpansionPreset("Fully Expanded");
        ExpansionPreset compact = new ExpansionPreset("Compact");
        ExpansionPreset balanced = new ExpansionPreset("Balanced");

        String[] widgetTypes = {"canvas", "canvas_slot", "overlay", "overlay_slot", "stack_box", "stack_box_slot", "texture_block", "color_block", "text_block", "slider_regular", "button_loud", "button_quiet", "button_regular", "vector2", "margin", "anchors", "color"};

        for (String widgetType : widgetTypes) {
            fullyExpanded.setWidgetExpansion(widgetType, true);
            compact.setWidgetExpansion(widgetType, false);
            balanced.setWidgetExpansion(widgetType, widgetType.contains("canvas") || widgetType.contains("overlay") || widgetType.contains("stack_box"));
        }

        presets.add(fullyExpanded);
        presets.add(compact);
        presets.add(balanced);

        currentPreset = balanced;
    }

    public List<ExpansionPreset> getPresets() {
        return new ArrayList<>(presets);
    }

    public void setCurrentPreset(ExpansionPreset preset) {
        currentPreset = preset;
    }

    public void setCurrentPresetByName(String name) {
        for (ExpansionPreset preset : presets) {
            if (preset.getName().equals(name)) {
                currentPreset = preset;
                return;
            }
        }
    }

    public ExpansionPreset getCurrentPreset() {
        return currentPreset;
    }
}