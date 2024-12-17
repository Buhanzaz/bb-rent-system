package dev.buhanzaz.handlers.message;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface ContentHandler {
    BotApiMethod<?> handler(Message message);
}
