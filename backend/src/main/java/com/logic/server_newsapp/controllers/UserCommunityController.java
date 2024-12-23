package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.services.UserCommunityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for managing user-community relationships. */
@Log4j2
@RestController
@RequestMapping("/userCommunity")
@RequiredArgsConstructor
public class UserCommunityController {

  /** Service for user-communities operations. */
    @Autowired
    private final UserCommunityService userCommunityService;

    /**
     * Subscribes a user to a community.
     *
     * @param login The login of the user.
     * @param nameCommunity The name of the community to subscribe to.
     * @return A response indicating the success or failure of the subscription.
     */
    @PostMapping
    public ResponseEntity<String> subscribeUserToCommunity(
            @RequestParam final String login,
            @RequestParam final String nameCommunity) {
        return userCommunityService.
                subscribeUserToCommunity(login, nameCommunity);
    }

    /**
     * Retrieves all users subscribed to a community.
     *
     * @param nameCommunity The name of the community to retrieve users from.
     * @return A list of all users subscribed to the community.
     */
    @GetMapping
    public ResponseEntity<List<String>> getUsersByCommunity(
            @RequestParam final String nameCommunity) {
        return userCommunityService.getUsersByCommunity(nameCommunity);
    }
}
