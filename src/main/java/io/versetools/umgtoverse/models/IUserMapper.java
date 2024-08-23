package io.versetools.umgtoverse.models;

import io.versetools.umgtoverse.dtos.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toUser(UserDTO userDTO);
    UserDTO fromUser(User user);
}
