package io.versetools.umgtoverse.controllers;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.settings.WidgetSettings;
import io.versetools.umgtoverse.models.Conversion;
import io.versetools.umgtoverse.services.ConversionService;
import io.versetools.umgtoverse.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/umg-to-verse")
public class ConversionController {

    private final ConversionService conversionService;

    @PostMapping
    public ResponseEntity<Conversion> convert(@RequestParam String userId, @RequestBody String input) {
        return ResponseEntity.ok(conversionService.convert(userId, input));
    }

    @GetMapping("{conversionId}")
    public ResponseEntity<Conversion> getConversion(@PathVariable String conversionId) {
        return ResponseEntity.ok(conversionService.getConversion(conversionId));
    }
}
