package dev.buhanzaz.config;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class User {
    private Long id;
    private Long telegramUserId;
    private String telegramChatId;
    private String telegramUsername;
    private String telegramFirstName;
}
