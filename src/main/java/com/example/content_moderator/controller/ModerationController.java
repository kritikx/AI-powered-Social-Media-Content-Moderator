package com.example.content_moderator.controller;

import com.example.content_moderator.model.ModerationResponse;
import com.example.content_moderator.service.ContentModerationService;
import com.example.content_moderator.service.PunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/moderate")
public class ModerationController {

    @Autowired
    private ContentModerationService contentModerationService;

    @Autowired
    private PunishmentService punishmentService;

    @PostMapping
    public ModerationResponse moderate(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String content = request.get("content");

        if (userId == null || content == null) {
            throw new IllegalArgumentException("Missing userId or content in request.");
        }

        boolean flagged = contentModerationService.isContentFlagged(content);
        return punishmentService.applyPunishment(userId, flagged);
    }
}


