package com.logic.server_newsapp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsDTO {
    private Long id;
    private Long communityId;
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private String source;

    // Конструктор
    public NewsDTO(Long id, Long communityId, String title, String content, LocalDateTime publishedDate, String source) {
        this.id = id;
        this.communityId = communityId;
        this.title = title;
        this.content = content;
        this.publishedDate = publishedDate;
        this.source = source;
    }
}

