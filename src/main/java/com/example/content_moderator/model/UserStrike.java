package com.example.content_moderator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_strikes")
public class UserStrike {

    @Id
    private String userId;
    private int strikeCount;
    private boolean banned;

    public UserStrike() {}

    public UserStrike(String userId, int strikeCount, boolean banned) {
        this.userId = userId;
        this.strikeCount = strikeCount;
        this.banned = banned;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStrikeCount() {
        return strikeCount;
    }

    public void setStrikeCount(int strikeCount) {
        this.strikeCount = strikeCount;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}


