package com.logic.server_newsapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Represents a user comment on a news item. */
@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comments {

    /** The unique identifier for the comment. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The user who made the comment. */
    @ManyToOne
    @JoinColumn(name = "iduser", nullable = false)
    private User user;

    /** The news item the comment is for. */
    @ManyToOne
    @JoinColumn(name = "idnews", nullable = false)
    private News news;

    /** The content of the comment. */
    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String comment;

    /** Default constructor for JPA. */
    public Comments() { }

    /**
     * Constructor for creating a new Comment.
     *
     * @param userParam The user making the comment.
     * @param newsParam The news item the comment is for.
     * @param commentParam The content of the comment.
     */
    public Comments(final User userParam,
                    final News newsParam, final String commentParam) {
        this.user = userParam;
        this.news = newsParam;
        this.comment = commentParam;
    }
}
