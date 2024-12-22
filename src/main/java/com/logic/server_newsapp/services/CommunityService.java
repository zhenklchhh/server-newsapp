package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.repositories.CommunityRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service class for managing communities.
 */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    /**
     * The repository for accessing community data.
     */
    private final CommunityRepository communityRepository;

    /**
     * Saves a community or updates it if it already exists.
     *
     * @param community The community to be saved or updated.
     * @return The saved or updated community.
     */
    public Community saveCommunity(final Community community) {
        log.info("Saving community: {}", community.getNameCommunity());
        return communityRepository.save(community);
    }

    /**
     * Retrieves all communities.
     *
     * @return A list of all communities.
     */
    public List<Community> getAllCommunities() {
        log.info("Retrieving all communities");
        return communityRepository.findAll();
    }

    /**
     * Retrieves a community by its ID.
     *
     * @param id The ID of the community to retrieve.
     * @return An Optional containing the community
     * if found, or an empty Optional if not.
     */
    public Optional<Community> getCommunityById(final Long id) {
        log.info("Retrieving community by ID: {}", id);
        return communityRepository.findById(id);
    }

    /**
     * Retrieves a community by its name.
     *
     * @param name The name of the community to retrieve.
     * @return A ResponseEntity containing the community if found,
     * or a 404 Not Found response if
     *     not.
     */
    public ResponseEntity<Community> getCommunityByName(final String name) {
        log.info("Retrieving community by name: {}", name);
        return communityRepository.findByNameCommunity(name).map(community -> {
            return ResponseEntity.ok(community);
        }).orElseGet(() -> {
            log.warn("Community with name: {} not found", name);
            return ResponseEntity.notFound().build();
        });
    }

    /**
     * Deletes a community by its ID.
     *
     * @param id The ID of the community to delete.
     */
    public void deleteCommunityById(final Long id) {
        log.info("Deleting community by ID: {}", id);
        communityRepository.deleteById(id);
    }
}
