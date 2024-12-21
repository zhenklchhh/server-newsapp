package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.jwt.JWTGenerator;
import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.services.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@Log4j2
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registration(@RequestBody User user) throws IOException, JOSEException {
        user.setRole("USER");
        userService.saveUser(user);
        String token = JWTGenerator.signJWT(user.getId());
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody User user) throws IOException, JOSEException {
        Optional<User> authUser = userService.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
        if(authUser.isPresent()) {
            String token = JWTGenerator.signJWT(user.getId());
            log.info("Пользователь с username: {} успешно аутентифицирован", user.getLogin());
            return ResponseEntity.ok(token);
        } else {
            log.warn("Пользователь с username: {} не прошел аутентификацию", user.getLogin());
            return ResponseEntity.status(401).build();
        }
    }
}
