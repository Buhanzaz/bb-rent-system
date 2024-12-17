package dev.buhanzaz.handlers.message.impl;

import dev.buhanzaz.handlers.exceptions.exception.returned.HandlerException;
import dev.buhanzaz.handlers.message.ContentHandler;
import dev.buhanzaz.ui.keyboard.KeyboardProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component(value = "command")
@RequiredArgsConstructor
class CommandContentHandler implements ContentHandler {
    private final KeyboardProvider keyboardProvider;

    @Override
    //TODO Сделать доп команды например регистрация или помощь, а в идеале команда /help /admin и /admin_help Для вызова админа
    public BotApiMethod<?> handler(Message message) {
        return switch (message.getText()) {
            case "/start" -> keyboardProvider.mainMenuKeyboard(message);
            case "/admin" -> null; // TODO смотри выше
            case "/admin_help" -> null; // TODO смотри выше
            case "/help" -> null; // TODO смотри выше
            case "/info" -> null;
            case "/reloaded" -> null;
            default -> throw new HandlerException("Unknown command: %s".formatted(message.getText()), message);
        };
    }
}
