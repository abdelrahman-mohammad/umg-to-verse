package io.versetools.umgtoverse.models;

import io.versetools.umgtoverse.converter.widget.Widget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("conversions")
public class Conversion {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String input;
    private String output;
}
