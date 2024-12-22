package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.repositories.CommunityRepository;
import com.logic.server_newsapp.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Service class for managing user-community relationships. */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommunityService {

    /** Repository for accessing user data. */
    private final UserRepository userRepository;

    /** Repository for accessing community data. */
    private final CommunityRepository communityRepository;

    /**
     * Subscribes a user to a community. If the community does not exist,
     * it will be created if the
     * user has the appropriate role.
     *
     * @param login The login of the user to subscribe.
     * @param nameCommunity The name of the community to subscribe to.
     * @return A ResponseEntity with a success or failure message.
     */
    public ResponseEntity<String> subscribeUserToCommunity(
            final String login, final String nameCommunity) {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            log.warn("User with login {} not found", login);
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();
        Optional<Community> communityOptional =
                communityRepository.findByNameCommunity(nameCommunity);
        Community community =
                communityOptional.orElseGet(
                        () -> {
                            if ("EDITOR".equalsIgnoreCase(user.getRole())
                                    || "ADMIN".equalsIgnoreCase(
                                            user.getRole())) {
                                log.info("Creating new community {} by user {}",
                                        nameCommunity, login);
                                Community newCommunity =
                                        new Community(user, nameCommunity);
                                user.getCommunities().add(newCommunity);
                                return communityRepository.save(newCommunity);
                            } else {
                                log.warn("User {} does not have permission"
                                        + " to create community", login);
                                throw new IllegalStateException("Insufficient"
                                        + " rights to create community");
                            }
                        });

        if (!community.getUsers().contains(user)) {
            community.getUsers().add(user);
            user.getCommunities().add(community);
            communityRepository.save(community);
            log.info("User {} subscribed to community {}",
                    login, nameCommunity);
            return ResponseEntity.ok("User successfully"
                    + " subscribed to community");
        }

        log.warn("User {} already subscribed to community {}",
                login, nameCommunity);
        return ResponseEntity.badRequest()
                .body("User already subscribed to community");
    }

    /**
     * Retrieves the list of users subscribed to a community.
     *
     * @param nameCommunity The name of the community.
     * @return A ResponseEntity with a list of user logins,
     * or a 400 Bad Request if the community is not
     *     found.
     */
    public ResponseEntity<List<String>> getUsersByCommunity(
            final String nameCommunity) {
        Optional<Community> communityOptional =
                communityRepository.findByNameCommunity(nameCommunity);

        if (communityOptional.isEmpty()) {
            log.warn("Community with name {} not found", nameCommunity);
            return ResponseEntity.badRequest().body(null);
        }

        Community community = communityOptional.get();
        List<String> userLogins =
                community.getUsers().stream()
                        .map(User::getLogin).collect(Collectors.toList());

        log.info("Retrieved user list for community {}", nameCommunity);
        return ResponseEntity.ok(userLogins);
    }
}
