package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.services.UserCommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/userCommunity")
@RequiredArgsConstructor
public class UserCommunityController {

    @Autowired
    private final UserCommunityService userCommunityService;

    @PostMapping
    public ResponseEntity<String> subscribeUserToCommunity(@RequestParam String login, @RequestParam String nameCommunity) {
        return userCommunityService.subscribeUserToCommunity(login, nameCommunity);
    }
    @GetMapping("/{nameCommunity}")
    public ResponseEntity<List<String>> getUsersByCommunity(@RequestParam String nameCommunity) {
        return userCommunityService.getUsersByCommunity(nameCommunity);
    }
}