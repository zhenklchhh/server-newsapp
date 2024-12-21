package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<User> getUserById(long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Пользователь с id: {} не найдена", id);
                    return ResponseEntity.notFound().build();
                });
    }

    public ResponseEntity<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (user.getLogin() != null) {
                        existingUser.setLogin(user.getLogin());
                    }
                    if (user.getPassword() != null) {
                        existingUser.setPassword(user.getPassword());
                    }
                    if (user.getEmail() != null) {
                        existingUser.setEmail(user.getEmail());
                    }
                    User savedUser = userRepository.save(existingUser);
                    return savedUser;
                }).orElseGet(() -> {
                    log.warn("Новость с id: {} не найдена для обновления", id);
                    return null;
                });
    }

    @Transactional
    public ResponseEntity<Void> deleteUser(long id) {
        return userRepository.findById(id)
                .map(news -> {
                    userRepository.delete(news);
                    return ResponseEntity.ok().<Void>build();
                }).orElseGet(() -> {
                    log.warn("Пользователь с id: {} не найдена для удаления", id);
                    return ResponseEntity.notFound().build();
                });
    }


    public Optional<User> getUserByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

}