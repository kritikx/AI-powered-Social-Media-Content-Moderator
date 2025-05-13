package com.example.content_moderator.repository;

import com.example.content_moderator.model.UserStrike;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.content_moderator.model.UserStrike;

import java.util.List;

public interface UserStrikeRepository extends MongoRepository<UserStrike, String> {
    // Spring Data auto-creates basic CRUD methods
    List<UserStrike> findByBanned(boolean banned);
}
