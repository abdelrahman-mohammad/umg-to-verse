package io.versetools.umgtoverse.converter.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WidgetSettings {
    private boolean expanded;
    private boolean includeDefaults;
    private boolean variable;
}
