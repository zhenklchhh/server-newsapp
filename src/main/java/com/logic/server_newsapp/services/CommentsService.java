package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.Comments;
import com.logic.server_newsapp.models.Community;
import com.logic.server_newsapp.repositories.CommentsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class CommentsService {
    private final CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    // Create or Update Community
    public Comments saveComment(Comments comment) {
        log.info("Saving comment: {}", comment.getUser() + " write on " + comment.getNews());
        return commentsRepository.save(comment);
    }

    // Get All Communities
    public List<Comments> getAllComments() {
        log.info("Fetching all comments");
        return commentsRepository.findAll();
    }

    // Get Community by ID
    public Optional<Comments> getCommentById(Long id) {
        log.info("Fetching comment with ID: {}", id);
        return commentsRepository.findById(id);
    }

    // Delete Community by ID
    public void deleteCommentById(Long id) {
        log.info("Deleting comment with ID: {}", id);
        commentsRepository.deleteById(id);
    }
}
