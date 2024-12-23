package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.jwt.JWTGenerator;
import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.services.UserService;
import com.nimbusds.jose.JOSEException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user authentication.
 */
@RestController
@Log4j2
public class AuthController {

    /** Service for user-related operations. */
    @Autowired
    private UserService userService;

    /** HTTP Not Authenticated code. */
    @SuppressWarnings("checkstyle:MemberName")
    private final int notauthenticated = 401;

    /**
     * Handles user registration.
     *
     * @param user The user to register.
     * @return A response containing the generated JWT token.
     * @throws IOException If an I/O error occurs.
     * @throws JOSEException If a JOSE error occurs.
     */
    @PostMapping(
            value = "/register",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Map<String, String>> registration(
            @RequestBody final User user) throws IOException, JOSEException {
        user.setRole("USER");
        userService.saveUser(user);
        String token = JWTGenerator.signJWT(user.getId());
        Map<String, String> response = new HashMap<>();
        response.put("jwt", token);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles user login.
     *
     * @param user The user trying to log in.
     * @return A response containing the generated
     * JWT token if login is successful or an error message
     *     if not.
     * @throws IOException If an I/O error occurs.
     * @throws JOSEException If a JOSE error occurs.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @PostMapping(value = "/login", consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody final User user) throws IOException, JOSEException {
        Optional<User> authUser =
                userService.getUserByLoginAndPassword(
                        user.getLogin(), user.getPassword());
        if (authUser.isPresent()) {
            String token = JWTGenerator.signJWT(authUser.get().getId());
            log.info(
                    "Пользователь с username: {} успешно аутентифицирован",
                    user.getLogin());
            Map<String, String> response = new HashMap<>();
            response.put("jwt", token);
            return ResponseEntity.ok(response);
        } else {
            log.warn(
                    "Пользователь с username: {} не прошел аутентификацию",
                    user.getLogin());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(notauthenticated).body(response);
        }
    }
}
