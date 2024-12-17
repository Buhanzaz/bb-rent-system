package dev.buhanzaz.handlers.exceptions.exception.returned;

import dev.buhanzaz.handlers.exceptions.ParentMessageException;
import dev.buhanzaz.security.ErrorAuthUpdate;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramAuthSecurityException extends ParentMessageException {
    private final ErrorAuthUpdate ErrorAuthUpdate;

    public TelegramAuthSecurityException(String messageException, ErrorAuthUpdate ErrorAuthUpdate) {
        super(messageException, ErrorAuthUpdate.getMessage());
        this.ErrorAuthUpdate = ErrorAuthUpdate;
    }
}
