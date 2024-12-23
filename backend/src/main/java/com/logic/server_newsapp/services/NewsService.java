package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.News;
import com.logic.server_newsapp.repositories.NewsRepository;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing news.
 */
@Log4j2
@Service
@Transactional
public class NewsService {

    /**
     * The repository for accessing news data.
     */
    @Autowired private NewsRepository newsRepository;

    /**
     * Retrieves all news.
     *
     * @return A list of all news.
     */
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    /**
     * Retrieves all news sorted by publish date in
     * descending order (newest first).
     *
     * @return A list of all news sorted by publish date (newest first).
     */
    public List<News> getAllNewsSortedByNewDate() {
        return newsRepository.findAll().stream()
                .sorted((news1, news2) ->
                        news2.getPublishDate().
                                compareTo(news1.getPublishDate()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all news sorted by publish date
     * in ascending order (oldest first).
     *
     * @return A list of all news sorted by publish date (oldest first).
     */
    public List<News> getAllNewsSortedByOldDate() {
        return newsRepository.findAll().stream()
                .sorted(Comparator.comparing(News::getPublishDate))
                .collect(Collectors.toList());
    }

    public List<News> getNewsByName(String name) {
        return newsRepository.findByTitleContainingIgnoreCase(name);
    }


    public List<News> getNewsByNewName(String name) {
        return newsRepository.findNewsByTitle(name)
                .stream()
                .sorted((news1, news2) -> news2.getPublishDate().compareTo(news1.getPublishDate()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves news by title sorted by publish date
     * in ascending order (oldest first).
     *
     * @param name The title of the news to retrieve.
     * @return A list of news with the specified title,
     * sorted by publish date (oldest first).
     */
    public List<News> getNewsByOldName(final String name) {
        return newsRepository.findNewsByTitle(name).stream()
                .sorted(Comparator.comparing(News::getPublishDate))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves news by its ID.
     *
     * @param id The ID of the news to retrieve.
     * @return A ResponseEntity containing the news if found,
     * or a 404 Not Found response if not.
     */
    public ResponseEntity<News> getNewsById(final long id) {
        return newsRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("News with id: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Saves a news or updates it if it already exists.
     *
     * @param news The news to be saved or updated.
     * @return The saved or updated news.
     */
    @Transactional
    public News saveNews(final News news) {
        return newsRepository.save(news);
    }

    /**
     * Updates a news by its ID.
     *
     * @param id The ID of the news to update.
     * @param news The news object containing the updated data.
     * @return The updated news, or null if
     * the news with the given ID does not exist.
     */
    @Transactional
    public News updateNews(final long id, final News news) {
        return newsRepository.findById(id)
                .map(
                        existingNews -> {
                            existingNews.setTitle(news.getTitle());
                            existingNews.setContent(news.getContent());
                            return newsRepository.save(existingNews);
                        })
                .orElseGet(
                        () -> {
                            log.warn("News with id: {} not found for update",
                                    id);
                            return null;
                        });
    }

    /**
     * Deletes a news by its ID.
     *
     * @param id The ID of the news to delete.
     * @return A ResponseEntity indicating success (200 OK)
     * if the news was deleted, or a 404 Not
     *     Found response if the news with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteNews(final long id) {
        return newsRepository.findById(id)
                .map(
                        news -> {
                            newsRepository.delete(news);
                            return ResponseEntity.ok().<Void>build();
                        })
                .orElseGet(
                        () -> {
                            log.warn("News with id: {} not found for delete",
                                    id);
                            return ResponseEntity.notFound().build();
                        });
    }

    public List<News> getNewsByDate(LocalDateTime date) {
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

        return newsRepository.findByPublishDateBetween(startOfDay, endOfDay);
    }

}
