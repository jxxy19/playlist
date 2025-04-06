package org.playlists.backend.service;

import org.playlists.backend.dto.ChapterDTO;

import java.util.List;

public interface YoutubeServiceIf {
    List<ChapterDTO> extractChapters(String videoId);
}
