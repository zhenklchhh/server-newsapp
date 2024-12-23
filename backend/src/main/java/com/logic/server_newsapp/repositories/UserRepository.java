package com.logic.server_newsapp.repositories;

import com.logic.server_newsapp.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for
 * accessing {@link com.logic.server_newsapp.models.User} entities in the
 * database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their login and password.
     *
     * @param login The login of the user.
     * @param password The password of the user.
     * @return An Optional containing the found user,
     * or an empty Optional if no user with the given
     *     login and password exists.
     */
    @Query("SELECT u FROM User u WHERE u.login"
            + " = :login AND u.password = :password")
    Optional<User> findByLoginAndPassword(
            @Param("login") String login,
            @Param("password") String password);

    /**
     * Finds a user by their login.
     *
     * @param login The login of the user.
     * @return An Optional containing the found user,
     * or an empty Optional if no user with the given
     *     login exists.
     */
    @Query("SELECT u FROM User u WHERE u.login = :login")
    Optional<User> findByLogin(
            @Param("login")  String login);
}
