package dev.buhanzaz.handlers.exceptions;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Getter
public class ParentMessageException extends RuntimeException {
    private final Message telegrammMessage;

    public ParentMessageException(String messageException, Message telegrammMessage) {
        super(messageException);
        this.telegrammMessage = telegrammMessage;
    }
}
