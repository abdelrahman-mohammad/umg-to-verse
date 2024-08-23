package io.versetools.umgtoverse.models;

import io.versetools.umgtoverse.converter.settings.Settings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String discordId;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private Settings conversionSettings;
}
