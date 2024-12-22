package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.models.Comments;
import com.logic.server_newsapp.services.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {
    @Autowired
    private final CommentsService commentsService;

    // Create or Update comments
    @PostMapping
    public ResponseEntity<Comments> saveComment(@RequestBody Comments comments) {
        log.info("Запрос на создание комментария: {}, написанным под новость: {}", comments.getUser(), comments.getNews());
        return ResponseEntity.ok(commentsService.saveComment(comments));
    }

    // Get All comments
    @GetMapping
    public ResponseEntity<List<Comments>> getAllComments() {
        log.info("Запрос на вывод всех комментариев");
        return ResponseEntity.ok(commentsService.getAllComments());
    }

    // Get comment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Comments> getCommentById(@PathVariable Long id) {
        log.info("Запрос вывода комментария по id: {}", id);
        return commentsService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete comment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        log.info("Запрос удаления комментария по ID: {}", id);
        commentsService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }
}
