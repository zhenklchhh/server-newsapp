package com.logic.server_newsapp.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/** Represents a community where news is posted. */
@Entity
@Getter
@Setter
@Table(name = "community")
public class Community {

    /** The unique identifier for the community. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The user who created the community. */
    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    /** The name of the community. This should be unique. */
    @Column(name = "namecommunity", nullable = false, unique = true)
    private String nameCommunity;

    /** A list of news items posted in this community. */
    @OneToMany(mappedBy = "community",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> newsList;

  /** A set of users that are subscribed to this community. */
    @ManyToMany
    @JoinTable(
            name = "user_community",
            joinColumns = @JoinColumn(name = "communityid"),
            inverseJoinColumns = @JoinColumn(name = "userid")
    )
    private Set<User> users;


    /** Default constructor for JPA. */
    public Community() {}

      /**
     * Constructor for creating a new community.
     *
     * @param nameCommunity The user who creates the community.
     * @param user The name of the community.
     */
    public Community(User user, String nameCommunity) {
        this.user = user;
        this.nameCommunity = nameCommunity;
    }
}
