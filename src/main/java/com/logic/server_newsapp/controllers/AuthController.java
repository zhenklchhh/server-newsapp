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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Log4j2
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> registration(@RequestBody User user) throws IOException, JOSEException {
        user.setRole("USER");
        userService.saveUser(user);
        String token = JWTGenerator.signJWT(user.getId());
        Map<String, String> response = new HashMap<>();
        response.put("jwt", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) throws IOException, JOSEException {
        Optional<User> authUser = userService.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
        if (authUser.isPresent()) {
            String token = JWTGenerator.signJWT(authUser.get().getId());
            log.info("Пользователь с username: {} успешно аутентифицирован", user.getLogin());
            Map<String, String> response = new HashMap<>();
            response.put("jwt", token);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Пользователь с username: {} не прошел аутентификацию", user.getLogin());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }
}
