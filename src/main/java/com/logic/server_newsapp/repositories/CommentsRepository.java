package com.logic.server_newsapp.repositories;

import com.logic.server_newsapp.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
