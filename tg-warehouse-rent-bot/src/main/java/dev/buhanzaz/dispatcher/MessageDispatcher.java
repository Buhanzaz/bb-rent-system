package dev.buhanzaz.dispatcher;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface MessageDispatcher {
    BotApiMethod<?> dispatcher(Message message);
}
