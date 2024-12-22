package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.models.News;
import com.logic.server_newsapp.services.CommunityService;
import com.logic.server_newsapp.services.NewsService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

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
    public ResponseEntity<List<News>> getAllNews() {
        log.info("Получен запрос на получение всех новостей");
        return ResponseEntity.ok(newsService.getAllNews());
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
     * @param news The news object to create.
     * @param communityName The name of the community the news belongs to.
     * @return The created news entry with a 201 CREATED status.
     */
    @PostMapping
    public ResponseEntity<News> createNews(
            @RequestBody final News news,
            @RequestParam final String communityName) {
        log.info("Получен запрос на создание новой новости: {}",
                news.getTitle());
        news.setPublishDate(LocalDateTime.now());
        ResponseEntity<Community> community =
                communityService.getCommunityByName(communityName);
        if (community.getBody() != null) {
            news.setCommunity(community.getBody());
            return new ResponseEntity<>(
                    newsService.saveNews(news), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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
