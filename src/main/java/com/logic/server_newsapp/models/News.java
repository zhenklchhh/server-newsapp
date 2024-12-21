package com.logic.server_newsapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "communityid", nullable = false)
    private Community community;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "publishdate")
    private LocalDateTime publishDate;

    @Column(name = "source")
    private String source;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    public News() {}

    public News(Community community, String title, String content, LocalDateTime publishDate, String source) {
        this.community = community;
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.source = source;
    }
}

