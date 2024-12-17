package dev.buhanzaz.utils;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public final class MessageUtils {
    private MessageUtils() {
    }

    public static String getStringChatId(Message message) {
        Long chatId = message.getChatId();

        if (chatId == null) {
            throw new IllegalArgumentException("ChatId cannot be null"); //TODO нужно в не проверяемый securityHandler засунуть
        }

        return chatId.toString();
    }

    public static Long getUserId(Message message) {
        return message.getFrom().getId();
    }

    public static String getText(Message message) {
        String text = message.getText();

        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null"); //TODO Возвращать
        }

        return text;
    }

    public static String getUserName(Message message) {
        String userName = message.getFrom().getUserName();

        if (userName == null) {
            throw new IllegalArgumentException("UserName cannot be null"); //TODO Возвращать
        }

        return userName;
    }
}
