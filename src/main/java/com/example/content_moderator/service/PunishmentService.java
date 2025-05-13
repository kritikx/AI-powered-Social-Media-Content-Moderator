package com.example.content_moderator.service;

import com.example.content_moderator.model.ModerationResponse;
import com.example.content_moderator.model.UserStrike;
import com.example.content_moderator.repository.UserStrikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PunishmentService {

    @Autowired
    private UserStrikeRepository userStrikeRepository;

    public ModerationResponse applyPunishment(String userId, boolean flagged) {
        if (!flagged) {
            return new ModerationResponse(false, "Content is safe.", "None");
        }

        // User flagged: Apply strike system
        Optional<UserStrike> optionalStrike = userStrikeRepository.findById(userId);
        int strikes = optionalStrike.map(UserStrike::getStrikeCount).orElse(0);

        strikes++; // Add strike

        String punishment = "Warning";
        String message = "Unsafe content detected. Strike " + strikes + "/5.";

        boolean banned = false;

        if (strikes >= 5) {
            punishment = "Permanent Ban";
            message = "User permanently banned after 5 offenses.";
            banned = true; // Ban user permanently
        } else if (strikes >= 3) {
            punishment = "Temporary Ban";
            message = "User temporarily banned after 3 offenses.";
            banned = true; // Ban user temporarily
        }

        // Save updated strike count and ban status
        UserStrike userStrike = new UserStrike(userId, strikes, banned);
        userStrikeRepository.save(userStrike);

        return new ModerationResponse(true, message, punishment);
    }
}
