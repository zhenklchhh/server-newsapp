package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.models.Comments;
import com.logic.server_newsapp.services.CommentsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for managing comments. */
@Log4j2
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {

  /** Service for comment-related operations. */
    @Autowired
    private final CommentsService commentsService;

    /**
     * Creates or updates a comment.
     *
     * @param comments The comment to save.
     * @return The saved comment.
     */
    @PostMapping
    public ResponseEntity<Comments> saveComment(
            @RequestBody final Comments comments) {
        log.info(
                "Запрос на создание комментария: {},"
                        + " написанным под новость: {}",
                comments.getUser(),
                comments.getNews());
        return ResponseEntity.ok(commentsService.saveComment(comments));
    }

    /**
     * Retrieves all comments.
     *
     * @return A list of all comments.
     */
    @GetMapping
    public ResponseEntity<List<Comments>> getAllComments() {
        log.info("Запрос на вывод всех комментариев");
        return ResponseEntity.ok(commentsService.getAllComments());
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param id The ID of the comment to retrieve.
     * @return The comment, or a 404 Not Found response
     * if the comment is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comments> getCommentById(
            @PathVariable final Long id) {
        log.info("Запрос вывода комментария по id: {}", id);
        return commentsService
                .getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id The ID of the comment to delete.
     * @return A 204 No Content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable final Long id) {
        log.info("Запрос удаления комментария по ID: {}", id);
        commentsService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }
}
