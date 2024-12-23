package com.logic.server_newsapp.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Represents a news item. */
@Entity
@Getter
@Setter
@Table(name = "news")
public class News {

    /** The unique identifier for the news item. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The community this news item belongs to. */
    @ManyToOne
    @JoinColumn(name = "communityid", nullable = false)
    private Community community;

    /** The title of the news item. */
    @Column(name = "title")
    private String title;

    /** The content of the news item. */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /** The date and time when the news item was published. */
    @Column(name = "publishdate")
    private LocalDateTime publishDate;

    /** The source of the news item. */
    @Column(name = "source")
    private String source;

    /** A list of comments for the news item. */
    @OneToMany(mappedBy = "news",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    /** Default constructor for JPA. */
    public News() { }

    /**
     * Constructor for creating a new News object.
     *
     * @param communityParam The community this news item belongs to.
     * @param titleParam The title of the news item.
     * @param contentParam The content of the news item.
     * @param publishDateParam The date and
     *                        time when the news item was published.
     * @param sourceParam The source of the news item.
     */
    public News(
            final Community communityParam,
            final String titleParam,
            final String contentParam,
            final LocalDateTime publishDateParam,
            final String sourceParam) {
        this.community = communityParam;
        this.title = titleParam;
        this.content = contentParam;
        this.publishDate = publishDateParam;
        this.source = sourceParam;
    }
}
