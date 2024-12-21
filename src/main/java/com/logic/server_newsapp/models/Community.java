package com.logic.server_newsapp.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "community")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "iduser", nullable = false)
    private User user;

    @Column(name = "namecommunity", nullable = false, unique = true)
    private String nameCommunity;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> newsList;

    public Community() {}

    public Community(User user, String nameCommunity) {
        this.user = user;
        this.nameCommunity = nameCommunity;
    }
}


