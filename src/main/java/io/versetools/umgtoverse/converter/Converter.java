package io.versetools.umgtoverse.converter;

import io.versetools.umgtoverse.converter.object.WObject;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.util.PrintColor;
import io.versetools.umgtoverse.converter.widget.Widget;
import io.versetools.umgtoverse.converter.widget.button.ButtonLoud;
import io.versetools.umgtoverse.converter.widget.button.ButtonQuiet;
import io.versetools.umgtoverse.converter.widget.button.ButtonRegular;
import io.versetools.umgtoverse.converter.widget.common.ColorBlock;
import io.versetools.umgtoverse.converter.widget.common.SliderRegular;
import io.versetools.umgtoverse.converter.widget.common.TextBlock;
import io.versetools.umgtoverse.converter.widget.common.TextureBlock;
import io.versetools.umgtoverse.converter.widget.panel.CanvasPanel;
import io.versetools.umgtoverse.converter.widget.panel.Overlay;
import io.versetools.umgtoverse.converter.widget.panel.StackBox;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
public class Converter {
    private Map<String, Supplier<Widget>> widgetFactories;
    private Map<String, WObject> objectsMap = new LinkedHashMap<>();
    private List<Widget> widgets;
    private Settings settings;

    public Converter(Settings settings) {
        this.settings = settings;
        this.widgetFactories = new HashMap<>();
        this.widgets = new ArrayList<>();
        registerWidgets();
    }

    private void registerWidgets() {
        widgetFactories.put("CanvasPanel", () -> new CanvasPanel(this));
        widgetFactories.put("Overlay", () -> new Overlay(this));
        widgetFactories.put("StackBox", () -> new StackBox(this));
        widgetFactories.put("Image", () -> new TextureBlock(this));
        widgetFactories.put("ColorBlock", () -> new ColorBlock(this));
        widgetFactories.put("UEFN_TextBlock_C", () -> new TextBlock(this));
        widgetFactories.put("UEFN_Slider_C", () -> new SliderRegular(this));
        widgetFactories.put("UEFN_Button_Loud_C", () -> new ButtonLoud(this));
        widgetFactories.put("UEFN_Button_Quiet_C", () -> new ButtonQuiet(this));
        widgetFactories.put("UEFN_Button_Regular_C", () -> new ButtonRegular(this));
    }

    public String convert(String input) {
        if (input == null || input.trim().isEmpty()) throw new IllegalArgumentException("Input text cannot be null or empty");
        String rootName = parseObjects(input);
        Widget rootWidget = createWidget(rootName);
        return rootWidget.generateVerseCode(0, settings);
    }

    private String parseObjects(String inputText) {
        System.out.println("========================================= Parsing objects =========================================");
        String[] lines = inputText.trim().split("\n");

        String rootName = WObject.parseName(lines[0]);
        if (rootName.isEmpty()) throw new IllegalArgumentException("Failed to parse root name from first line");
        System.out.println(rootName + PrintColor.ANSI_CYAN + ": Root object's name." + PrintColor.ANSI_RESET);

        StringBuilder currentObject = new StringBuilder();
        int currentScope = 0;
        int objectCount = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            System.out.println(PrintColor.ANSI_BLUE + "Processing line " + (i + 1) + ": " + line + PrintColor.ANSI_RESET);

            if (line.contains("Begin Object")) {
                currentScope++;
                if (currentScope == 1) currentObject = new StringBuilder();
            }

            currentObject.append(line).append("\n");

            if (line.contains("End Object")) {
                if (currentScope == 1) {
                    objectCount++;
                    System.out.println("Object #" + objectCount + PrintColor.ANSI_GREEN + ": Completed at scope level 1" + PrintColor.ANSI_RESET);
                    processObject(currentObject.toString(), objectCount);
                    currentObject = new StringBuilder();
                }
                currentScope--;
            }
        }
        System.out.println("Total objects processed at scope level 1: " + objectCount);
        System.out.println("========================================= Finished parsing objects =========================================");
        return rootName;
    }

    private void processObject(String objectStr, int objectNumber) {
        System.out.printf("========================================= Processing object #%d =========================================%n", objectNumber);
        System.out.println(PrintColor.ANSI_CYAN + objectStr.trim() + PrintColor.ANSI_RESET);

        try {
            String objectClass = WObject.parseClass(objectStr);
            String objectName = WObject.parseName(objectStr);
            if (objectClass.trim().isEmpty() || objectName.trim().isEmpty()) throw new IllegalArgumentException("Object #" + objectNumber + ": Failed to parse class or name.");

            if (widgetFactories.containsKey(objectClass)) {
                WObject obj = new WObject(objectStr);
                objectsMap.put(obj.getName(), obj);
                System.out.println(obj.getName() + PrintColor.ANSI_GREEN + ": Successfully mapped object." + PrintColor.ANSI_RESET);
            } else {
                System.out.println(objectClass + PrintColor.ANSI_RED + ": Couldn't find a factory or class is not registered" + PrintColor.ANSI_RESET);
            }
        } catch (Exception e) {
            System.err.println("Object #" + objectNumber + PrintColor.ANSI_RED + ": Error processing: " + e.getMessage() + PrintColor.ANSI_RESET);
        }

        System.out.println("Object #" + objectNumber + PrintColor.ANSI_GREEN + ": Finished processing." + PrintColor.ANSI_RESET);
        System.out.println();
    }

    public Widget createWidget(String widgetName) {
        System.out.println("========================================= Creating widget =========================================");
        if (widgetName == null || widgetName.isEmpty()) throw new IllegalArgumentException("Widget name cannot be null or empty");
        if (!objectsMap.containsKey(widgetName)) throw new IllegalArgumentException("No object found for widget name: " + widgetName);

        WObject object = objectsMap.get(widgetName);
        String widgetClass = WObject.parseClass(object.getObjectStr());

        Supplier<Widget> factory = widgetFactories.get(widgetClass);
        if (factory == null) throw new IllegalArgumentException("No widget factory found for class: " + widgetClass);

        try {
            System.out.println(widgetName + PrintColor.ANSI_PURPLE + ": Creating widget..." + PrintColor.ANSI_RESET);
            Widget widget = factory.get();
            widget.setName(widgetName);
            widget.parse(object.getObjectStr());
            System.out.println(widgetName + PrintColor.ANSI_GREEN + ": Created widget." + PrintColor.ANSI_RESET);
            widgets.add(widget);
            return widget;
        } catch (Exception e) {
            throw new IllegalArgumentException(widgetName + ": Failed to create widget.", e);
        }
    }

    public Widget createWidget(String widgetName, WObject object) {
        if (object == null) throw new IllegalArgumentException("Object cannot be null");
        if (widgetName == null || widgetName.isEmpty()) throw new IllegalArgumentException("Widget name cannot be null or empty");

        String widgetClass = WObject.parseClass(object.getObjectStr());
        Supplier<Widget> factory = widgetFactories.get(widgetClass);
        if (factory == null) throw new IllegalArgumentException("No widget factory found for class: " + widgetClass);

        try {
            System.out.println(widgetName + PrintColor.ANSI_PURPLE + ": Creating widget..." + PrintColor.ANSI_RESET);
            Widget widget = factory.get();
            widget.setName(widgetName);
            widget.parse(object.getObjectStr());
            System.out.println(widgetName + PrintColor.ANSI_GREEN + ": Created widget." + PrintColor.ANSI_RESET);
            return widget;
        } catch (Exception e) {
            throw new IllegalArgumentException(widgetName + ": Failed to create widget.", e);
        }
    }
}