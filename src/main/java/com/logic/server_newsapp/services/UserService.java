package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Service class for managing users. */
@Slf4j
@Service
@Transactional
public class UserService {

    /** The repository for accessing user data. */
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepositoryParam The repository for user data.
     */
    public UserService(final UserRepository userRepositoryParam) {
        this.userRepository = userRepositoryParam;
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A ResponseEntity containing the user if found,
     * or a 404 Not Found response if not.
     */
    public ResponseEntity<User> getUserById(final long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> {
                            log.warn("User with id: {} not found", id);
                            return ResponseEntity.notFound().build();
                        });
    }

    /**
     * Retrieves a user by its login.
     *
     * @param login The login of the user to retrieve.
     * @return A ResponseEntity containing the user if found,
     * or a 404 Not Found response if not.
     */
    public ResponseEntity<User> getUserByLogin(final String login) {
        return userRepository.findByLogin(login)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> {
                            log.warn("User with name: {} not found", login);
                            return ResponseEntity.notFound().build();
                        });
    }

    /**
     * Retrieves the role of a user by their login.
     *
     * @param login The login of the user to get the role.
     * @return A ResponseEntity containing the user's role if found,
     * or a 404 Not Found response if not.
     */
    public ResponseEntity<String> getUserRoleByLogin(final String login) {
        return userRepository.findByLogin(login)
                .map(existingUser -> ResponseEntity.ok(existingUser.getRole()))
                .orElseGet(
                        () -> {
                            log.warn("User with name: {} not found", login);
                            return ResponseEntity.notFound().build();
                        });
    }

    /**
     * Saves a user or updates it if it already exists.
     *
     * @param user The user to be saved or updated.
     * @return The saved or updated user.
     */
    @Transactional
    public User saveUser(final User user) {
        log.info("User : {} registered", user.getLogin());
        System.out.println(
                user.getId()
                        + " "
                        + user.getEmail()
                        + " "
                        + user.getLogin()
                        + " "
                        + user.getPassword()
                        + " "
                        + user.getRole());
        return userRepository.save(user);
    }

    /**
     * Updates a user by their ID.
     *
     * @param id The ID of the user to update.
     * @param user The user object containing the updated data.
     * @return The updated user, or null if
     * the user with the given ID does not exist.
     */
    @Transactional
    public User updateUser(final Long id, final User user) {
        return userRepository.findById(id)
                .map(
                        existingUser -> {
                            if (user.getLogin() != null) {
                                existingUser.setLogin(user.getLogin());
                            }
                            if (user.getPassword() != null) {
                                existingUser.setPassword(user.getPassword());
                            }
                            if (user.getEmail() != null) {
                                existingUser.setEmail(user.getEmail());
                            }
                            return userRepository.save(existingUser);
                        })
                .orElseGet(
                        () -> {
                            log.warn("News with id: {} not found for update",
                                    id);
                            return null;
                        });
    }

    /**
     * Updates the role of a user by their login.
     *
     * @param login The login of the user to update.
     * @param role The new role to set for the user.
     * @return A ResponseEntity indicating success (204 No Content)
     * if the role was updated, or a 404
     *     Not Found response if the user with the given login does not exist.
     */
    @Transactional
    public ResponseEntity<Void> updateUserRole(
            final String login, final String role) {
        return userRepository.findByLogin(login)
                .map(
                        existingUser -> {
                            existingUser.setRole(role);
                            return ResponseEntity.noContent().<Void>build();
                        })
                .orElseGet(
                        () -> {
                            log.warn("User with name : {} not found for update",
                                    login);
                            return ResponseEntity.notFound().build();
                        });
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return A ResponseEntity indicating success (200 OK)
     * if the user was deleted, or a 404 Not Found
     *     response if the user with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteUser(final long id) {
        return userRepository.findById(id)
                .map(
                        news -> {
                            userRepository.delete(news);
                            return ResponseEntity.ok().<Void>build();
                        })
                .orElseGet(
                        () -> {
                            log.warn("User with id: {} not found for delete",
                                    id);
                            return ResponseEntity.notFound().build();
                        });
    }

    /**
     * Retrieves a user by their login and password.
     *
     * @param login The login of the user to retrieve.
     * @param password The password of the user to retrieve.
     * @return An Optional containing the user if found,
     * or an empty Optional if not.
     */
    public Optional<User> getUserByLoginAndPassword(
            final String login, final String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
