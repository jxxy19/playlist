package org.playlists.backend.controller;
import org.playlists.backend.dto.ChapterDTO;
import org.playlists.backend.service.YoutubeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/youtube")
public class YoutubeProxyController {

    private final YoutubeServiceImpl youtubeService;

    public YoutubeProxyController(YoutubeServiceImpl youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GetMapping("/chapters")
    public ResponseEntity<List<ChapterDTO>> getVideoDescription(@RequestParam String videoId) {
    List<ChapterDTO> chapters = youtubeService.extractChapters(videoId);
    return ResponseEntity.ok(chapters);
    }
}
