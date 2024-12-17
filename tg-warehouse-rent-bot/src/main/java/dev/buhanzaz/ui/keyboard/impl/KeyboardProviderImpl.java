package dev.buhanzaz.ui.keyboard.impl;

import dev.buhanzaz.state.MenuState;
import dev.buhanzaz.state.MenuStateManager;
import dev.buhanzaz.ui.keyboard.KeyboardProvider;
import dev.buhanzaz.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static dev.buhanzaz.utils.MessageUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
class KeyboardProviderImpl implements KeyboardProvider {
    private final MenuStateManager menuStateManager;

    @Override
    public @NotNull SendMessage mainMenuKeyboard(Message message) {
        SendMessage sendMessage = new SendMessage(getStringChatId(message), "Выберете пункт меню.");
        List<KeyboardRow> keyboard = getStartKeyboardRows();

        menuStateManager.setUserState(getUserId(message), MenuState.MAIN_MENU);

        return getKeyboardSendMessage(keyboard, sendMessage);
    }

    @Override
    public @NotNull SendMessage stepBackKeyboard(Message message) {
        SendMessage sendMessage = new SendMessage(getStringChatId(message), "");
        List<KeyboardRow> keyboard = getStepBackKeyboard();

        return getKeyboardSendMessage(keyboard, sendMessage);
    }

    private List<KeyboardRow> getStepBackKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Назад");
        keyboard.add(row);

        return keyboard;
    }

    private SendMessage getKeyboardSendMessage(List<KeyboardRow> keyboard, SendMessage message) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }


    private List<KeyboardRow> getStartKeyboardRows() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Добавить фото");
        row.add("Найти бытовку по номеру");
        // Add the first row to the keyboard
        keyboard.add(row);
        // Create another keyboard row

        row = new KeyboardRow();
        // Set each button for the second line
        row.add("Изменить фото бытовки");
        row.add("Удалить фото бытовки");
        // Add the second row to the keyboard
        return keyboard;
    }
}
