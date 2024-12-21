package com.logic.server_newsapp.repositories;

import com.logic.server_newsapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.login = :login AND u.password = :password")
    Optional<User> findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    @Query("SELECT u FROM User u WHERE u.login = :login")
    Optional<User> findByLogin(@Param("login") String login);
}
