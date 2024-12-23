package com.logic.server_newsapp.repositories;

import com.logic.server_newsapp.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    /**
     * Finds a community by its name.
     *
     * @param name The name of the community to find.
     * @return An Optional containing the found community,
     * or an empty Optional if no community with
     *     the given name exists.
     */
    Optional<Community> findByNameCommunity(String name);
}
