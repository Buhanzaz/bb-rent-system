package dev.buhanzaz.dispatcher.impl;


import dev.buhanzaz.dispatcher.MessageDispatcher;
import dev.buhanzaz.dispatcher.UpdateDispatcher;
import dev.buhanzaz.handlers.exceptions.exception.returned.DispatcherUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component
public class UpdateDispatcherImpl implements UpdateDispatcher {
    private final MessageDispatcher messageDispatcher;

    public UpdateDispatcherImpl(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public BotApiMethod<?> dispatcher(@NotNull Update update) {
        if (update.hasMessage()) {
            final Message message = update.getMessage();
            return messageDispatcher.dispatcher(message);
        } else {
            throw new DispatcherUpdateException("Unsupported update type", update);
        }
    }
}
