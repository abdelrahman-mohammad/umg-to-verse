package io.versetools.umgtoverse.models;

import io.versetools.umgtoverse.dtos.UserDTO;
import io.versetools.umgtoverse.dtos.UserDTO.UserDTOBuilder;
import io.versetools.umgtoverse.models.User.UserBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-23T17:19:54+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class IUserMapperImpl implements IUserMapper {

    @Override
    public User toUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UserBuilder<?, ?> user = User.builder();

        user.discordId( userDTO.getDiscordId() );
        user.username( userDTO.getUsername() );
        user.email( userDTO.getEmail() );

        return user.build();
    }

    @Override
    public UserDTO fromUser(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTOBuilder<?, ?> userDTO = UserDTO.builder();

        userDTO.discordId( user.getDiscordId() );
        userDTO.username( user.getUsername() );
        userDTO.email( user.getEmail() );

        return userDTO.build();
    }
}
