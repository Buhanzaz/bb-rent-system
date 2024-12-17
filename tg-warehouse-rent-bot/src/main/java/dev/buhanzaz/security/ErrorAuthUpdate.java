package dev.buhanzaz.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;


@Getter
@AllArgsConstructor// TODO это делается для хранение ошибок на сервере чтобы можно было глянуть что отправляли
public class ErrorAuthUpdate extends Update {
    private final Long userId;
    private final String chatId;
    private final ErrorAuthType errorAuthType;
}
