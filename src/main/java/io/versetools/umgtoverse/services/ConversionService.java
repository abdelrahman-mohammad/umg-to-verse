package io.versetools.umgtoverse.services;

import io.versetools.umgtoverse.converter.Converter;
import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.settings.WidgetSettings;
import io.versetools.umgtoverse.exception.ResourceNotFoundException;
import io.versetools.umgtoverse.models.Conversion;
import io.versetools.umgtoverse.repositories.IConversionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConversionService {

    private final IConversionRepository conversionRepository;
    private final UserService userService;

    public Conversion convert(String userId, String input) {
        Settings settings = userService.getConversionSettings(userId);
        Converter converter = new Converter(settings);
        String output = converter.convert(input);

        Conversion conversion = Conversion.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .input(input)
                .output(output)
                .build();

        conversionRepository.insert(conversion);
        return conversion;
    }

    public Conversion getConversion(String conversionId) {
        return conversionRepository.findById(conversionId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find conversion with id: %s".formatted(conversionId)));
    }

    public List<Conversion> getConversionsByUserId(String userId) {
        return conversionRepository.findConversionsByUserId(userId);
    }
}
