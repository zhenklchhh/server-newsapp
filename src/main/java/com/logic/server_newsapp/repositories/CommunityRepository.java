package com.logic.server_newsapp.repositories;

import com.logic.server_newsapp.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
