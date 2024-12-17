package dev.buhanzaz.handlers.message.impl;

import dev.buhanzaz.handlers.exceptions.exception.returned.HandlerException;
import dev.buhanzaz.handlers.message.ContentHandler;
import dev.buhanzaz.state.CabinIdStateManager;
import dev.buhanzaz.state.MenuState;
import dev.buhanzaz.state.MenuStateManager;
import dev.buhanzaz.ui.keyboard.KeyboardProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.regex.Pattern;

import static dev.buhanzaz.state.MenuState.ADD_IMAGE_MENU;
import static dev.buhanzaz.state.MenuState.SEARCH_IMAGE_MENU;
import static dev.buhanzaz.utils.MessageUtils.*;

@Slf4j
@Component(value = "text")
@RequiredArgsConstructor
//TODO Все клавиши и команды вынести в отдельный класс констант
//TODO При загрузке фотографии, нужно указать кнопку следующая , чтобы стирать id бытовки
class TextContentHandler implements ContentHandler {
    private static final String CABIN_ID_REGEX = "^\\d{6,8}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$";
    private final KeyboardProvider keyboardProvider;
    private final CabinIdStateManager cabinIdStateManager;
    private final MenuStateManager menuStateManager;

    @Override
    public BotApiMethod<?> handler(Message message) {
        String text = message.getText();

        return switch (text) {
            case "Добавить фото" -> addImageTextCommand(message);
            case "Найти бытовку по номеру" -> searchImageTextCommand(message);
            case "Назад" -> backTextCommand(message);
            default -> userTextHandler(message);
        };
    }

    private @NotNull SendMessage addImageTextCommand(Message message) {
        Long userId = getUserId(message);
        SendMessage sendMessage = getSendMessageHelperNumberCabinId(message);

        menuStateManager.setUserState(userId, ADD_IMAGE_MENU);

        // TODO Нужно вернуть клавиатуру с отменой или inline меню в сообщение для отмены в сообщение которое отправит пользователь
        return sendMessage;
    }

    private @NotNull SendMessage searchImageTextCommand(Message message) {
        Long userId = getUserId(message);
        SendMessage sendMessage = getSendMessageHelperNumberCabinId(message);

        menuStateManager.setUserState(userId, SEARCH_IMAGE_MENU);
        return sendMessage;
    }

    private @NotNull SendMessage backTextCommand(Message message) {
        Long userId = getUserId(message);

        //TODO Интересно мы же используем put и по идее он заменят значения
        //cabinIdStateManager.deleteCabinId(userId); //TODO Без этого метода не будет удаляться id бытовки для поиска или добавления фото
        return keyboardProvider.mainMenuKeyboard(message);
    }

    private @NotNull SendMessage getSendMessageHelperNumberCabinId(Message message) {
        SendMessage sendMessage = keyboardProvider.stepBackKeyboard(message);
        sendMessage.setText("Введите номер бытовки");
        return sendMessage;
    }

    private @Nullable SendMessage userTextHandler(Message message) {
        Long userId = getUserId(message);
        String userText = getText(message);

        MenuState userState = menuStateManager.getUserState(userId);

        switch (userState) {
            case ADD_IMAGE_MENU -> { //TODO Вынести обработку и отправку сообщений в отдельный класс
                if (Pattern.matches(CABIN_ID_REGEX, userText)) {
                    // TODO Проверять в базе данных наличие бытовки если нет уточнить новая или нет, так же подумать что хранить user id что возможно бред
                    cabinIdStateManager.putCabinId(userId, Long.valueOf(userText));

                    return new SendMessage(getStringChatId(message),
                            "Отправляйте фото.\nЖелательно отправлять фото в виде файлов");
                }
            }

            case SEARCH_IMAGE_MENU -> {
                if (Pattern.matches(CABIN_ID_REGEX, userText)) {
                    //TODO Проверять в базе данных наличие бытовки если нет уточнить новая или нет, так же подумать что хранить user id что возможно бред
                    //TODO Вообще нужно добавить сюда еще и добавление в MenuStateManager
                    return null; //TODO поиск в процессе
                }
            }
        }

        throw new HandlerException("Unknown text command: %s".formatted(message.getText()), message);
    }
}