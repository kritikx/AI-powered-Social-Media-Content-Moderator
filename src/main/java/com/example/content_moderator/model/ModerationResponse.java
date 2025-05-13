package com.example.content_moderator.model;

public class ModerationResponse {

    private boolean flagged;
    private String message;
    private String punishment;

    // Constructor
    public ModerationResponse(boolean flagged, String message, String punishment) {
        this.flagged = flagged;
        this.message = message;
        this.punishment = punishment;
    }

    // Getters and Setters
    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPunishment() {
        return punishment;
    }

    public void setPunishment(String punishment) {
        this.punishment = punishment;
    }
}

