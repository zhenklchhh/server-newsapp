package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.News;
import com.logic.server_newsapp.repositories.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@Transactional
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
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
