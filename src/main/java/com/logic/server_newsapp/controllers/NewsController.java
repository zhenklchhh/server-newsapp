package com.logic.server_newsapp.controllers;


import com.logic.server_newsapp.models.News;
import com.logic.server_newsapp.services.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        log.info("Получен запрос на получение всех новостей");
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        log.info("Получен запрос на получение новости с id: {}", id);
        return newsService.getNewsById(id);
    }


    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        log.info("Получен запрос на создание новой новости: {}", news.getTitle());
        news.setPublishDate(LocalDateTime.now());
        return new ResponseEntity<>(newsService.saveNews(news), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News updatedNews) {
        log.info("Получен запрос на обновление новости с id: {}, новые данные: {}", id, updatedNews);
        News savedNews = newsService.updateNews(id, updatedNews);
        if (savedNews != null) {
            return new ResponseEntity<>(savedNews, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        log.info("Получен запрос на удаление новости с id: {}", id);
        return newsService.deleteNews(id);
    }
}