package dev.buhanzaz.handlers.exceptions.exception.unreturned;

public class BotWebhookException extends RuntimeException {
    public BotWebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}
