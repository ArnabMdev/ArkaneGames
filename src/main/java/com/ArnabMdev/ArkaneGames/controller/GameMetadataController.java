package com.ArnabMdev.ArkaneGames.controller;

import com.ArnabMdev.ArkaneGames.entity.GameMetadata;
import com.ArnabMdev.ArkaneGames.service.GameMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameMetadataController {

    @Autowired
    private GameMetadataService gameMetadataService;

    @GetMapping("/metadata")
    public String getGameMetadata() {
        return gameMetadataService.getAllGameMetadata().toString();
    }

    @PostMapping("/metadata")
    public String saveGameMetadata(@RequestBody GameMetadata gameMetadata) {
        return gameMetadataService.saveGameMetadata(gameMetadata).toString();
    }
}
