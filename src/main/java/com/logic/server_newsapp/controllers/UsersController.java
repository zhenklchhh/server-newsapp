package com.logic.server_newsapp.controllers;


import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllNews() {
        log.info("Получен запрос на получение всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/role")
    public ResponseEntity<String> getUserRoleByLogin(@PathVariable String login) {
        return userService.getUserRoleByLogin(login);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Получен запрос на получение пользователя с id: {}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        log.info("Получен запрос на получение пользователя с login: {}", login);
        return userService.getUserByLogin(login);
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя: {}", user.getLogin());
        user.setRole("USER");
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/updateUserRoleEditor")
    public ResponseEntity<Void> updateUserRoleEditor(@RequestBody String login, @RequestBody String role) {
        if (role == "ADMIN") {
            return userService.updateUserRole(login, "EDITOR");
        } else {
            log.warn("User haven't permission to update user role");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/updateUserRoleAdmin")
    public ResponseEntity<Void> updateUserRoleAdmin(@RequestBody String login, @RequestBody String role) {
        if (role == "ADMIN") {
            return userService.updateUserRole(login, "ADMIN");
        } else {
            log.warn("User haven't permission to make user admin");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Получен запрос на обновление пользователя с id: {}", id);
        User savedUser = userService.updateUser(id, updatedUser);
        if (savedUser != null) {
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        return userService.deleteUser(id);
    }
}
