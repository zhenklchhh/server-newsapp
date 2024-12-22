package com.logic.server_newsapp.repositories;


import com.logic.server_newsapp.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    /**
     * Finds news items by their title.
     *
     * @param name The title of the news items to find.
     * @return A list of news items with the given title.
     */
    List<News> findNewsByTitle(String name);
}
