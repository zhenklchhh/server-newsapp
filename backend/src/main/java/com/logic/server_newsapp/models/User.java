package com.logic.server_newsapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Represents a user of the application. */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    /** Number of symbols of login. */
    private final int minLengthLogin = 3;

    /** Number of symbols of login. */
    private final int minLengthPassword = 6;

    /** The unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The login (username) for the user.
     *  Must be unique and have at least 3 characters. */
    @Size(min = minLengthLogin,
            message = "Логин должен содержать не меньше 3 символов")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "Логин не должен содержать специальные символы")
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /** The email address of the user.
     *  Must be unique and a valid email format. */
    @Email(message = "Введите действительный адрес электронной почты")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The password for the user.
     * Must have at least 6 characters and contain only alphanumeric
     * characters.
     */
    @Size(min = minLengthPassword,
            message = "Пароль должен содержать не меньше 6 символов")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "Пароль не должен содержать специальные символы")
    @Column(name = "password", nullable = false)
    private String password;

    /** The role of the user. */
    @Pattern(regexp = "^[a-zA-Z]+$",
            message = "Роль не может содержать специальные символы")
    @Column(name = "role")
    private String role;


      /** List of communities to which the user is subscribed. */
    @ManyToMany
    @JoinTable(
            name = "user_community",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "communityid")
    )
    private List<Community> communities;

    /** List of comments made by this user. */
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    /** Default constructor for JPA. */
    public User() { }


    /**
     * Constructor for creating a new User.
     *
     * @param loginParam The login (username) for the user.
     * @param emailParam The email address of the user.
     * @param passwordParam The password for the user.
     * @param roleParam The role of the user.
     */
    public User(
            final String loginParam,
            final String emailParam,
            final String passwordParam,
            final String roleParam) {
        this.login = loginParam;
        this.email = emailParam;
        this.password = passwordParam;
        this.role = roleParam;
    }
}
