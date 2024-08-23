package io.versetools.umgtoverse.repositories;

import io.versetools.umgtoverse.models.Conversion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConversionRepository extends MongoRepository<Conversion, String> {
    List<Conversion> findConversionsByUserId(String userId);
}
