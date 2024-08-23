package io.versetools.umgtoverse.repositories;

import io.versetools.umgtoverse.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
