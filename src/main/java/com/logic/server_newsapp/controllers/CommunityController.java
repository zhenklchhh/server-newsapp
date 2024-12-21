package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.services.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // Create or Update Community
    @PostMapping
    public ResponseEntity<Community> saveCommunity(@RequestBody Community community) {
        log.info("Request to save community: {}", community.getNameCommunity());
        return ResponseEntity.ok(communityService.saveCommunity(community));
    }

    // Get All Communities
    @GetMapping
    public ResponseEntity<List<Community>> getAllCommunities() {
        log.info("Request to fetch all communities");
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    // Get Community by ID
    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable Long id) {
        log.info("Request to fetch community with ID: {}", id);
        return communityService.getCommunityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Community by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunityById(@PathVariable Long id) {
        log.info("Request to delete community with ID: {}", id);
        communityService.deleteCommunityById(id);
        return ResponseEntity.noContent().build();
    }
}

