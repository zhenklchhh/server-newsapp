package com.logic.server_newsapp.repositories;

import com.logic.server_newsapp.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByNameCommunity(String name);
}
