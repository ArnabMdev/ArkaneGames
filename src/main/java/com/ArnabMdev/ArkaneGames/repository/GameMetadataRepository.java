package com.ArnabMdev.ArkaneGames.repository;

import com.ArnabMdev.ArkaneGames.entity.GameMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMetadataRepository extends MongoRepository<GameMetadata, String> {

}
