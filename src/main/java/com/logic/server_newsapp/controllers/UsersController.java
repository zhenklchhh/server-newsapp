package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.services.UserService;
import java.util.List;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for managing users. */
@RestController
@Log4j2
@RequestMapping("/users")
public class UsersController {

    /** Service for user-related operations. */
    @Autowired private UserService userService;

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllNews() {
        log.info("Получен запрос на получение всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user's role by their login.
     *
     * @param login The login of the user.
     * @return The role of the user.
     */
    @GetMapping("/role/{login}")
    public ResponseEntity<String> getUserRoleByLogin(
            @PathVariable final String login) {
        return userService.getUserRoleByLogin(login);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user, or a 404 Not Found response if the user is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable final Long id) {
        log.info("Получен запрос на получение пользователя с id: {}", id);
        return userService.getUserById(id);
    }

    /**
     * Retrieves a user by their login.
     *
     * @param login The login of the user to retrieve.
     * @return The user, or a 404 Not Found response if the user is not found.
     */
    @GetMapping("/login/{login}")
    public ResponseEntity<User> getUserByLogin(
            @PathVariable final String login) {
        log.info("Получен запрос на получение пользователя с login: {}", login);
        return userService.getUserByLogin(login);
    }

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return The created user with a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody final User user) {
        log.info("Получен запрос на "
                + "создание нового пользователя: {}", user.getLogin());
        user.setRole("USER");
        return new ResponseEntity<>(
                userService.saveUser(user), HttpStatus.CREATED);
    }

    /**
     * Updates a user's role to EDITOR, if the current user has admin role.
     *
     * @param login The login of the user to update.
     * @param role The current role of the user.
     * @return A 204 No Content response or a 403 Forbidden
     * if user is not authorized to update the role.
     */
    @PutMapping("/updateUserRoleEditor")
    public ResponseEntity<Void> updateUserRoleEditor(@RequestBody String login) {
        if (Objects.equals(Objects.requireNonNull(userService.getUserByLogin(login).getBody()).getRole(), "ADMIN")) {
            return userService.updateUserRole(login, "EDITOR");
        } else {
            log.warn("У пользователя недостаточно прав");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Updates a user's role to ADMIN, if the current user has admin role.
     *
     * @param login The login of the user to update.
     * @param role The current role of the user.
     * @return A 204 No Content response or a 403 Forbidden
     * if user is not authorized to update the role.
     */
    @PutMapping("/updateUserRoleAdmin")
    public ResponseEntity<Void> updateUserRoleAdmin(
            @RequestBody final String login, @RequestBody final String role) {
        if (Objects.equals(role, "ADMIN")) {
            return userService.updateUserRole(login, "ADMIN");
        } else {
            log.warn("Пользователь не является администратором");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id The ID of the user to update.
     * @param updatedUser The updated user data.
     * @return The updated user with a 200 OK status,
     * or a 404 Not Found response if the user is not
     *     found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable final Long id, @RequestBody final User updatedUser) {
        log.info("Получен запрос на обновление пользователя с id: {}", id);
        User savedUser = userService.updateUser(id, updatedUser);
        if (savedUser != null) {
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return A 204 No Content response,
     * or a 404 Not Found status if the user is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        ResponseEntity<Void> result = userService.deleteUser(id);
        if (result.getStatusCode() == HttpStatus.NO_CONTENT) {
            return result;
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
