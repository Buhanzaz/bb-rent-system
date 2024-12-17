package dev.buhanzaz.handlers.exceptions.exception.returned;

import lombok.Getter;
import org.springframework.amqp.AmqpException;

@Getter
public class CustomAmqpException extends RuntimeException {
    private final String chatId;

    public CustomAmqpException(String chatId, AmqpException exception) {
        super(exception);
        this.chatId = chatId;
    }
}
