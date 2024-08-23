package io.versetools.umgtoverse.controllers;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.settings.WidgetSettings;
import io.versetools.umgtoverse.dtos.UserDTO;
import io.versetools.umgtoverse.models.Conversion;
import io.versetools.umgtoverse.models.IUserMapper;
import io.versetools.umgtoverse.models.User;
import io.versetools.umgtoverse.services.ConversionService;
import io.versetools.umgtoverse.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final ConversionService conversionService;
    private final UserService userService;
    private final IUserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(userMapper::fromUser).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO userDTO) {
        User userToInsert = userMapper.toUser(userDTO);
        User insertedUser = userService.insertUser(userToInsert);
        return ResponseEntity.ok(userMapper.fromUser(insertedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.fromUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        User userToUpdate = userMapper.toUser(userDTO);
        User updatedUser = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(userMapper.fromUser(updatedUser));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.fromUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/conversions")
    public ResponseEntity<List<Conversion>> getConversionsByUserId(@PathVariable String id) {
        return ResponseEntity.ok(conversionService.getConversionsByUserId(id));
    }

    @GetMapping("/{id}/settings")
    public ResponseEntity<Settings> getConversionSettings(@PathVariable String id) {
        return ResponseEntity.ok(userService.getConversionSettings(id));
    }

    @GetMapping("{id}/settings/{widgetType}")
    public ResponseEntity<WidgetSettings> getWidgetSettings(@PathVariable String id, @PathVariable String widgetType) {
        return ResponseEntity.ok(userService.getWidgetSettings(id, widgetType));
    }

    @PostMapping("{id}/settings/{widgetType}")
    public ResponseEntity<?> setWidgetSettings(@PathVariable String id, @PathVariable String widgetType, @RequestBody WidgetSettings widgetSettings) {
        userService.setWidgetSettings(id, widgetType, widgetSettings);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}/settings/{widgetType}")
    public ResponseEntity<?> updateWidgetSettings(@PathVariable String id, @PathVariable String widgetType, @RequestParam boolean expanded, @RequestParam boolean includeDefaults, @RequestParam boolean variable) {
        userService.updateWidgetSettings(id, widgetType, expanded, includeDefaults, variable);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
