package com.ArnabMdev.ArkaneGames.service;

import com.ArnabMdev.ArkaneGames.entity.GameMetadata;
import com.ArnabMdev.ArkaneGames.repository.GameMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameMetadataService {

    @Autowired
    private GameMetadataRepository gameMetadataRepository;

    public List<GameMetadata> getAllGameMetadata() {
        return gameMetadataRepository.findAll();
    }

    public GameMetadata saveGameMetadata(GameMetadata gameMetadata) {
        return gameMetadataRepository.save(gameMetadata);
    }
}
