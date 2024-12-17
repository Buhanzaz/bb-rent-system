package dev.buhanzaz.handlers.exceptions.exception.returned;

import dev.buhanzaz.handlers.exceptions.ParentMessageException;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class DispatcherUpdateException extends ParentMessageException {
    private final Update telegramUpdate;

    public DispatcherUpdateException(String messageException, Update telegramUpdate) {
        super(messageException, telegramUpdate.getMessage());
        this.telegramUpdate = telegramUpdate;
    }
}
