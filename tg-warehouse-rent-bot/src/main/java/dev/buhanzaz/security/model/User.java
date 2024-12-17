package dev.buhanzaz.security.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    private Long telegramUserId;
    private String telegramChatId;
    private String telegramUsername;
    private String telegramFirstName;
}
