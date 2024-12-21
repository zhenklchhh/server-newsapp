package com.logic.server_newsapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="news")
public class News {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="communityId")
    private Long communityId;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;

    @Column(name="publishDate")
    private LocalDateTime publishDate;

    @Column(name="source")
    private String source;
}
