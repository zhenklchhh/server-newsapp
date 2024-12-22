package com.logic.server_newsapp.repositories;


import com.logic.server_newsapp.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findNewsByTitle(String name);
    List<News> findByTitleContainingIgnoreCase(String name);
}
