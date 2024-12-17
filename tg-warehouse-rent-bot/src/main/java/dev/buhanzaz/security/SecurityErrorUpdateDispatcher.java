package dev.buhanzaz.security;

import dev.buhanzaz.handlers.exceptions.exception.returned.DispatcherMessageException;
import dev.buhanzaz.handlers.exceptions.exception.returned.TelegramAuthSecurityException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

import static dev.buhanzaz.security.ErrorAuthType.UNREGISTERED;
import static dev.buhanzaz.security.ErrorAuthType.USER_INFORMATION_HAS_CHANGED;

@Component("security")
public class SecurityErrorUpdateDispatcher {

//    public BotApiMethod<?> dispatcher(ErrorAuthUpdate update) {
//        if (update.hasMessage() && update.getMessage().isCommand()) {
//            String errorCommand = update.getMessage().getText();
//
//            return switch (errorCommand) {
//                case "/auth_error_change_user_info" -> SendMessage.builder()
//                        .chatId(update.getMessage().getChatId())
//                        .replyMarkup(new ReplyKeyboardRemove(true))
//                        .text("Вы не зарегистрированы, введите секретный ключ от администратора!")
//                        .build();
//                case "/auth_error_not_found_user_id" -> SendMessage.builder()
//                        .chatId(update.getMessage().getChatId())
//                        .replyMarkup(new ReplyKeyboardRemove(true))
//                        .text("Вы не зарегистрированы, для входа введите секретный ключ от администратора!")
//                        .build();
//                default -> null;
//            };
//        }
//
//        return null;
//    }

    public BotApiMethod<?> dispatcher(ErrorAuthUpdate update) {
        switch (update.getErrorAuthType()) {
            case UNREGISTERED -> throw new TelegramAuthSecurityException(UNREGISTERED.getMessage(), update);
            case USER_INFORMATION_HAS_CHANGED -> throw new TelegramAuthSecurityException(
                    USER_INFORMATION_HAS_CHANGED.getMessage(), update);
            default ->
                    throw new DispatcherMessageException("Ошибка при обработке Update в dispatcher", update.getMessage());
        }
    }
}
