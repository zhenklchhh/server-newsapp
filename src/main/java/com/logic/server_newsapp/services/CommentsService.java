package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.Comments;
import com.logic.server_newsapp.repositories.CommentsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Service class for managing comments.
 */
@Log4j2
@Service
@Transactional
public class CommentsService {

  
    private final CommentsRepository commentsRepository;
    
  @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    /**
     * Saves a comment or updates it if it already exists.
     *
     * @param comment The comment to be saved or updated.
     * @return The saved or updated comment.
     */
    public Comments saveComment(final Comments comment) {
        log.info(
                "Saving comment: {} wrote on {}",
                comment.getUser(), comment.getNews());
        return commentRepository.save(comment);
    }

    /**
     * Retrieves all comments.
     *
     * @return A list of all comments.
     */
    public List<Comments> getAllComments() {
        log.info("Retrieving all comments");
        return commentRepository.findAll();
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param id The ID of the comment to retrieve.
     * @return An Optional containing the comment if found,
     * or an empty Optional if not.
     */
    public Optional<Comments> getCommentById(final Long id) {
        log.info("Retrieving comment by ID: {}", id);
        return commentRepository.findById(id);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id The ID of the comment to delete.
     */
    public void deleteCommentById(final Long id) {
        log.info("Deleting comment by ID: {}", id);
        commentRepository.deleteById(id);
    }
}
