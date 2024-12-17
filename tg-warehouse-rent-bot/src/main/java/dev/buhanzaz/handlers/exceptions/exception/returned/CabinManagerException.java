package dev.buhanzaz.handlers.exceptions.exception.returned;

import dev.buhanzaz.handlers.exceptions.ParentMessageException;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class CabinManagerException extends ParentMessageException {
    public CabinManagerException(String messageException, Message telegrammMessage) {
        super(messageException, telegrammMessage);
    }
}