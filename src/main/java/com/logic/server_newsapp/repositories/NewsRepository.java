package com.logic.server_newsapp.repositories;


import com.logic.server_newsapp.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findNewsByTitle(String name);
}
