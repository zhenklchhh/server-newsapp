package com.logic.server_newsapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "iduser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "idnews", nullable = false)
    private News news;

    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String comment;

    public Comments() {}

    public Comments(User user, News news, String comment) {
        this.user = user;
        this.news = news;
        this.comment = comment;
    }
}

