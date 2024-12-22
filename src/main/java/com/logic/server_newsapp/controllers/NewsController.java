package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.DTO.NewsDTO;
import com.logic.server_newsapp.models.News;
import com.logic.server_newsapp.services.CommunityService;
import com.logic.server_newsapp.services.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private NewsService newsService;
    private CommunityService communityService;

    @Autowired
    public NewsController(CommunityService communityService, NewsService newsService) {
        this.communityService = communityService;
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        log.info("Получен запрос на получение всех новостей");
        return ResponseEntity.ok(
                newsService.getAllNews().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/name")
    public ResponseEntity<List<NewsDTO>> getNewsByName(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        log.info("Получен запрос на получение новостей по названию: {}", name);

        List<News> newsList = newsService.getNewsByName(name);
        if (newsList.isEmpty()) {
            log.info("Новостей с названием '{}' не найдено", name);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                newsList.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList())
        );
    }
    @PostMapping("/date")
    public ResponseEntity<List<NewsDTO>> getNewsByDate(@RequestBody Map<String, String> request) {
        String dateStr = request.get("date");
        log.info("Получен запрос на получение новостей за дату: {}", dateStr);

        try {
            LocalDateTime date = LocalDate.parse(dateStr).atStartOfDay();
            List<News> newsList = newsService.getNewsByDate(date);
            if (newsList.isEmpty()) {
                log.info("Новостей за дату '{}' не найдено", dateStr);
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(
                    newsList.stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList())
            );
        } catch (DateTimeParseException e) {
            log.error("Ошибка парсинга даты: {}", dateStr, e);
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsDTO newsDTO, @RequestParam String communityName, @RequestHeader("User-Role") String userRole) {
        if (!"EDITOR".equalsIgnoreCase(userRole)) {
            log.warn("Недостаточно прав для создания новости");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно прав для создания новости");
        }
        log.info("Создание новости пользователем с ролью EDITOR: {}", newsDTO.getTitle());
        News news = newsService.saveNews(convertToEntity(newsDTO, communityName));
        return new ResponseEntity<>(convertToDTO(news), HttpStatus.CREATED);
    }

    private NewsDTO convertToDTO(News news) {
        return new NewsDTO(
                news.getId(),
                news.getCommunity().getId(),
                news.getTitle(),
                news.getContent(),
                news.getPublishDate(),
                news.getSource()
        );
    }

    private News convertToEntity(NewsDTO newsDTO, String communityName) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setPublishDate(LocalDateTime.now());
        news.setSource(newsDTO.getSource());
        news.setCommunity(communityService.getCommunityByName(communityName).getBody());
        return news;
    }
}