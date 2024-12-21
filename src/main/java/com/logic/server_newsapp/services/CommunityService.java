package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.repositories.CommunityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    // Create or Update Community
    public Community saveCommunity(Community community) {
        log.info("Созранение новости: {}", community.getNameCommunity());
        return communityRepository.save(community);
    }

    // Get All Communities
    public List<Community> getAllCommunities() {
        log.info("Получение всех новостей");
        return communityRepository.findAll();
    }

    // Get Community by ID
    public Optional<Community> getCommunityById(Long id) {
        log.info("Получение новости по ID: {}", id);
        return communityRepository.findById(id);
    }

    // Get Community by name
    public Optional<Community> getCommunityByName(String name) {
        log.info("Получение новости по name: {}", name);
        return communityRepository.findByNameCommunity(name);
    }

    // Delete Community by ID
    public void deleteCommunityById(Long id) {
        log.info("Удаление новости по ID: {}", id);
        communityRepository.deleteById(id);
    }
}