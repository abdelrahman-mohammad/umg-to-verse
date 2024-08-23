package io.versetools.umgtoverse.services;

import io.versetools.umgtoverse.converter.settings.Settings;
import io.versetools.umgtoverse.models.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User findById(String id);
    User findByUsername(String username);
    User insertUser(User user);
    User updateUser(String id, User user);
    void deleteUser(String id);
    Settings getConversionSettings(String userId);
}
