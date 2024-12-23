package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.services.CommunityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for managing communities. */
@Log4j2
@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
  
      /** Service for community-related operations. */
    @Autowired
    private final CommunityService communityService;

    /**
     * Creates or updates a community.
     *
     * @param community The community to save.
     * @return The saved community.
     */
    @PostMapping
    public ResponseEntity<Community> saveCommunity(
            @RequestBody final Community community) {
        log.info("Запрос на создания сообщества: {}",
                community.getNameCommunity());
        return ResponseEntity.ok(communityService.saveCommunity(community));
    }

    /**
     * Retrieves all communities.
     *
     * @return A list of all communities.
     */
    @GetMapping
    public ResponseEntity<List<Community>> getAllCommunities() {
        log.info("Запрос на вывод всех сообществ");
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    /**
     * Retrieves a community by its ID.
     *
     * @param id The ID of the community to retrieve.
     * @return The community, or a 404 Not Found response
     * if the community is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(
            @PathVariable final Long id) {
        log.info("Запрос на вывод сообществ по id: {}", id);
        return communityService
                .getCommunityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a community by its ID.
     *
     * @param id The ID of the community to delete.
     * @return A 204 No Content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunityById(
            @PathVariable final Long id) {
        log.info("Запрос удаления сообщества по id: {}", id);
        communityService.deleteCommunityById(id);
        return ResponseEntity.noContent().build();
    }
}
