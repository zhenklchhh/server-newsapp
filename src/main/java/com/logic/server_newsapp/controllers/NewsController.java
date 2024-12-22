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

import java.time.LocalDateTime;
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

    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO, @RequestParam String communityName) {
        log.info("Получен запрос на создание новой новости: {}", newsDTO.getTitle());
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