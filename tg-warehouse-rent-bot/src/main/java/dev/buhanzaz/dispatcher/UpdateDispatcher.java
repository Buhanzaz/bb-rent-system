package dev.buhanzaz.dispatcher;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateDispatcher {
    BotApiMethod<?> dispatcher(Update update);
}
