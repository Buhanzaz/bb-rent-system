package dev.buhanzaz.config;

import dev.buhanzaz.handlers.exceptions.ParentMessageException;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramSecurityException extends RuntimeException {
    public TelegramSecurityException(String message) {
        super(message);
    }

    public TelegramSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
