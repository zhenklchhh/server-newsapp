package com.logic.server_newsapp.services;

import com.logic.server_newsapp.models.Comments;
import com.logic.server_newsapp.repositories.CommentsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class CommentsService {

    private final CommentsRepository commentsRepository;
    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    // Create or Update Community
    public Comments saveComment(Comments comment) {
        log.info("Сохранение комментария: {}", comment.getUser() + " write on " + comment.getNews());
        return commentsRepository.save(comment);
    }

    // Get All Communities
    public List<Comments> getAllComments() {
        log.info("Получение всех комментариев");
        return commentsRepository.findAll();
    }

    // Get Community by ID
    public Optional<Comments> getCommentById(Long id) {
        log.info("Получение комментария по ID: {}", id);
        return commentsRepository.findById(id);
    }

    // Delete Community by ID
    public void deleteCommentById(Long id) {
        log.info("Удаление комментария по ID: {}", id);
        commentsRepository.deleteById(id);
    }
}
