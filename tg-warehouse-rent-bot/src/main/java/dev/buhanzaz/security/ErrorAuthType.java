package dev.buhanzaz.security;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ErrorAuthType {
    UNREGISTERED ("Вы не зарегистрированы, введите секретный ключ."),
    USER_INFORMATION_HAS_CHANGED ("Вы изменили свои учетные данные в телеграмм!\n" +
            "Введите секретный пароль для обновление данных");

    private final String message;

    ErrorAuthType(String message) {
        this.message = message;
    }
}
