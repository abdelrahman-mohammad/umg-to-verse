package io.versetools.umgtoverse.services;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.converter.settings.WidgetSettings;
import io.versetools.umgtoverse.exception.ResourceNotFoundException;
import io.versetools.umgtoverse.models.Conversion;
import io.versetools.umgtoverse.models.User;
import io.versetools.umgtoverse.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User insertUser(User user) {
        user.setConversionSettings(new Settings("Default"));
        return userRepository.insert(user);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id: %s".formatted(id)));
    }

    @Override
    public User updateUser(String id, User user) {
        User userToUpdate = findById(id);
        userToUpdate.setDiscordId(user.getDiscordId());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setConversionSettings(user.getConversionSettings());
        return userRepository.save(userToUpdate);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) throw new ResourceNotFoundException("Cannot find user with username: %s".formatted(username));
        return user;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public Settings getConversionSettings(String id) {
        return findById(id).getConversionSettings();
    }

    public WidgetSettings getWidgetSettings(String id, String widgetType) {
        Settings settings = getConversionSettings(id);
        return settings.getWidgetSettings(widgetType);
    }

    public void setWidgetSettings(String id, String widgetType, WidgetSettings widgetSettings) {
        Settings settings = getConversionSettings(id);
        settings.setWidgetSettings(widgetType, widgetSettings);
    }

    public void updateWidgetSettings(String id, String widgetType, boolean expanded, boolean includeDefaults, boolean variable) {
        Settings settings = getConversionSettings(id);
        settings.updateWidgetSettings(widgetType, expanded, includeDefaults, variable);
    }
}
