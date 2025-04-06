package org.playlists.backend.dto;

import lombok.Getter;

@Getter
public class ChapterDTO {
    private String time;
    private String title;

    public ChapterDTO(String time, String title) {
        this.time = time;
        this.title = title;
    }

    public String getTime() {
        return time;
    }
    public String getTitle() {
        return title;
    }
}
