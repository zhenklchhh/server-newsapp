package com.logic.server_newsapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 3, message = "Логин должен содержать не меньше 3 символов")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Логин не должен содержать специальные символы")
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Email(message = "Введите действительный адрес электронной почты")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(min = 6, message = "Пароль должен содержать не меньше 6 символов")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Пароль не должен содержать специальные символы")
    @Column(name = "password", nullable = false)
    private String password;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Роль не может содержать специальные символы")
    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Community> communities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    public User() {}

    public User(String login, String email, String password, String role) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

