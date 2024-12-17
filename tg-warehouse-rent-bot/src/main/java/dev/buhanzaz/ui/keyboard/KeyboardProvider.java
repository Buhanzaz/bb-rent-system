package dev.buhanzaz.ui.keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface KeyboardProvider {
    SendMessage mainMenuKeyboard(Message message);

    SendMessage stepBackKeyboard(Message message);
}
