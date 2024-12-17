package dev.buhanzaz.handlers.exceptions.exception.returned;

import dev.buhanzaz.handlers.exceptions.ParentMessageException;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class DispatcherMessageException extends ParentMessageException {
    public DispatcherMessageException(String exceptionMessage, Message telegrammMessage) {
        super(exceptionMessage, telegrammMessage);

    }
}
