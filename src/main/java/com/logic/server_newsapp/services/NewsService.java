package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.News;
import com.logic.server_newsapp.repositories.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public List<News> getAllNewsSortedByNewDate() {
        return newsRepository.findAll()
                .stream()
                .sorted((news1, news2) -> news2.getPublishDate().compareTo(news1.getPublishDate()))
                .collect(Collectors.toList());
    }
    public List<News> getAllNewsSortedByOldDate() {
        return newsRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(News::getPublishDate))
                .collect(Collectors.toList());
    }

    public List<News> getNewsByName(String name) {
        return newsRepository.findNewsByTitle(name);
    }

    public List<News> getNewsByNewName(String name) {
        return newsRepository.findNewsByTitle(name)
                .stream()
                .sorted((news1, news2) -> news2.getPublishDate().compareTo(news1.getPublishDate()))
                .collect(Collectors.toList());
    }

    public List<News> getNewsByOldName(String name) {
        return newsRepository.findNewsByTitle(name)
                .stream()
                .sorted(Comparator.comparing(News::getPublishDate))
                .collect(Collectors.toList());
    }

    public ResponseEntity<News> getNewsById(long id) {
        return newsRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Новость с id: {} не найдена", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Transactional
    public News saveNews(News news){
        return newsRepository.save(news);
    }

    @Transactional
    public News updateNews(long id, News news){
        return newsRepository.findById(id)
                .map(existingNews -> {
                    existingNews.setTitle(news.getTitle());
                    existingNews.setContent(news.getContent());
                    News savedNews = newsRepository.save(existingNews);
                    return savedNews;
                }).orElseGet(() -> {
                    log.warn("Новость с id: {} не найдена для обновления", id);
                    return null;
                });
    }

    @Transactional
    public ResponseEntity<Void> deleteNews(long id) {
        return newsRepository.findById(id)
                .map(news -> {
                    newsRepository.delete(news);
                    return ResponseEntity.ok().<Void>build();
                }).orElseGet(() -> {
                    log.warn("Новость с id: {} не найдена для удаления", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
