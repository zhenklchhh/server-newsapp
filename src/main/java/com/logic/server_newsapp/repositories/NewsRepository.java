package com.logic.server_newsapp.repositories;


import com.logic.server_newsapp.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

}
