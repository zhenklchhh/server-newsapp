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

/** REST controller for managing news. */
@Log4j2
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    /** Service for news-related operations. */
    private final NewsService newsService;

    /** Service for community-related operations. */
    private final CommunityService communityService;

    /**
     * Constructor to inject dependencies.
     *
     * @param communityServiceParam The service for
     *                             community-related operations.
     * @param newsServiceParam The service for news-related operations.
     */
    @Autowired
    public NewsController(
            final CommunityService communityServiceParam,
            final NewsService newsServiceParam) {
        this.communityService = communityServiceParam;
        this.newsService = newsServiceParam;
    }

    /**
     * Retrieves all news.
     *
     * @return A list of all news.
     */
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

    /**
     * Retrieves all news sorted by the newest date first.
     *
     * @return A list of all news sorted by newest date.
     */
    @GetMapping("/date/new")
    public ResponseEntity<List<News>> getAllNewsSortedByNew() {
        log.info("Получен запрос на получение"
                + " всех новостей, отсортированных по новизне");
        return ResponseEntity.ok(newsService.getAllNewsSortedByNewDate());
    }

    /**
     * Retrieves all news sorted by the oldest date first.
     *
     * @return A list of all news sorted by oldest date.
     */
    @GetMapping("/date/old")
    public ResponseEntity<List<News>> getAllNewsSortedByOld() {
        log.info("Получен запрос на получение"
                + " всех новостей, отсортированных по давности");
        return ResponseEntity.ok(newsService.getAllNewsSortedByOldDate());
    }

    /**
     * Retrieves news by its ID.
     *
     * @param id The ID of the news to retrieve.
     * @return The news, or a 404 Not Found response if the news is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable final Long id) {
        log.info("Получен запрос на получение новости с id: {}", id);
        return newsService.getNewsById(id);
    }

    /**
     * Retrieves all news by name.
     *
     * @param name The name of the news to retrieve.
     * @return A list of all news by the given name.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<News>> getNewsByName(
            @PathVariable final String name) {
        log.info("Получен запрос на получение новости по названию {}", name);
        return ResponseEntity.ok(newsService.getNewsByName(name));
    }

    /**
     * Retrieves all news by name, sorted by the newest date first.
     *
     * @param name The name of the news to retrieve.
     * @return A list of all news by the given name, sorted by newest date.
     */
    @GetMapping("/date/{name}/new")
    public ResponseEntity<List<News>> getNewsByNewName(
            @PathVariable final String name) {
        log.info("Получен запрос на получение всех"
                + " новостей по имени {}, отсортированных по новизне", name);
        return ResponseEntity.ok(newsService.getNewsByNewName(name));
    }

    /**
     * Retrieves all news by name, sorted by the oldest date first.
     *
     * @param name The name of the news to retrieve.
     * @return A list of all news by the given name, sorted by oldest date.
     */
    @GetMapping("/date/{name}/old")
    public ResponseEntity<List<News>> getNewsByOldName(
            @PathVariable final String name) {
        log.info("Получен запрос на получение всех"
                + " новостей по имени {}, отсортированных по давности", name);
        return ResponseEntity.ok(newsService.getNewsByOldName(name));
    }

    /**
     * Creates a new news entry.
     *
     * @param newsDTO The news object to create.
     * @param communityName The name of the community the news belongs to.
     * @return The created news entry with a 201 CREATED status.
     */
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

    /**
     * Updates an existing news entry.
     *
     * @param id The ID of the news entry to update.
     * @param updatedNews The updated news data.
     * @return The updated news entry with a 200 OK status,
     * or a 404 Not Found status if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(
            @PathVariable final Long id, @RequestBody final News updatedNews) {
        log.info("Получен запрос на обновление новости"
                + " с id: {}, новые данные: {}", id, updatedNews);
        News savedNews = newsService.updateNews(id, updatedNews);
        if (savedNews != null) {
            return new ResponseEntity<>(savedNews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a news entry by its ID.
     *
     * @param id The ID of the news entry to delete.
     * @return A 204 No Content response,
     * or a 404 Not Found status if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable final Long id) {
        log.info("Получен запрос на удаление новости с id: {}", id);
        ResponseEntity<Void> result = newsService.deleteNews(id);
        if (result.getStatusCode() == HttpStatus.NO_CONTENT) {
            return result;
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
