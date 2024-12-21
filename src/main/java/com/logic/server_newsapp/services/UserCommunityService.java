package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.models.User;
import com.logic.server_newsapp.repositories.CommunityRepository;
import com.logic.server_newsapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommunityService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    public ResponseEntity<String> subscribeUserToCommunity(String login, String nameCommunity) {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            log.warn("Пользователь с логином {} не найден", login);
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }

        User user = userOptional.get();
        Optional<Community> communityOptional = communityRepository.findByNameCommunity(nameCommunity);
        Community community = communityOptional.orElseGet(() -> {
            if ("EDITOR".equalsIgnoreCase(user.getRole()) || "ADMIN".equalsIgnoreCase(user.getRole())) {
                log.info("Создание нового сообщества {} пользователем {}", nameCommunity, login);
                Community newCommunity = new Community(user, nameCommunity);
                user.getCommunities().add(newCommunity);
                return communityRepository.save(newCommunity);
            } else {
                log.warn("Пользователь {} не имеет прав для создания сообщества", login);
                throw new IllegalStateException("Недостаточно прав для создания сообщества");
            }
        });

        if (!community.getUsers().contains(user)) {
            community.getUsers().add(user);
            user.getCommunities().add(community);
            communityRepository.save(community);
            log.info("Пользователь {} подписан на сообщество {}", login, nameCommunity);
            return ResponseEntity.ok("Пользователь успешно подписан на сообщество");
        }

        log.warn("Пользователь {} уже подписан на сообщество {}", login, nameCommunity);
        return ResponseEntity.badRequest().body("Пользователь уже подписан на сообщество");
    }

    public ResponseEntity<List<String>> getUsersByCommunity(String nameCommunity) {
        Optional<Community> communityOptional = communityRepository.findByNameCommunity(nameCommunity);

        if (communityOptional.isEmpty()) {
            log.warn("Сообщество с именем {} не найдено", nameCommunity);
            return ResponseEntity.badRequest().body(null);
        }

        Community community = communityOptional.get();
        List<String> userLogins = community.getUsers()
                .stream()
                .map(User::getLogin)
                .collect(Collectors.toList());

        log.info("Получен список пользователей для сообщества {}", nameCommunity);
        return ResponseEntity.ok(userLogins);
    }
}

