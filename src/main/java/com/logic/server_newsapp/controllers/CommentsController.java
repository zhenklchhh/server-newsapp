package com.logic.server_newsapp.controllers;

import com.logic.server_newsapp.models.Comments;
import com.logic.server_newsapp.services.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;

    // Create or Update Community
    @PostMapping
    public ResponseEntity<Comments> saveComment(@RequestBody Comments comments) {
        log.info("Request to save comment: {}", comments.getUser() + " write on " + comments.getNews());
        return ResponseEntity.ok(commentsService.saveComment(comments));
    }

    // Get All Communities
    @GetMapping
    public ResponseEntity<List<Comments>> getAllComments() {
        log.info("Request to fetch all comments");
        return ResponseEntity.ok(commentsService.getAllComments());
    }

    // Get Community by ID
    @GetMapping("/{id}")
    public ResponseEntity<Comments> getCommentById(@PathVariable Long id) {
        log.info("Request to fetch comment with ID: {}", id);
        return commentsService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Community by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        log.info("Request to delete comment with ID: {}", id);
        commentsService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }
}
