package dev.buhanzaz.handlers.exceptions;

import dev.buhanzaz.handlers.exceptions.exception.returned.*;
import dev.buhanzaz.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static dev.buhanzaz.utils.MessageUtils.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
//TODO В ошибки которые можно вернуть пользователю передаем Message
//TODO Глянуть в org.jetbrains.annotations какие вызываются ошибки, и их нужно будет обработать в не возвращаемом клиенту сообщение обработчике
public class ErrorHandler {
    @ExceptionHandler({AmqpException.class})
    @ResponseBody
    public BotApiMethod<?> amqpExceptionHandler(final CustomAmqpException exception) {
        log.error("Get status 500 Internal server error {}", exception.getMessage(), exception);

        String chatId = exception.getChatId();
        String reason = "amqp internal server error";
        String errorText = createErrorText(INTERNAL_SERVER_ERROR, reason, exception.getMessage());

        return createMessage(chatId, errorText);
    }

    @ExceptionHandler({CabinManagerException.class, DispatcherUpdateException.class, DispatcherMessageException.class,
            HandlerException.class, })
    @ResponseBody
    public BotApiMethod<?> telegramBotExceptionHandler(final ParentMessageException exception) {
        log.error("Get status 500 Internal server error {}", exception.getMessage(), exception);

        String chatId = getStringChatId(exception.getTelegrammMessage());
        String reason = "Telegram Api internal server error";
        String errorText = createErrorText(INTERNAL_SERVER_ERROR, reason, exception.getMessage());

        return createMessage(chatId, errorText);
    }

    private String createErrorText(HttpStatus httpStatus, String reason, String errorMessage) {
        return "Произошла ошибка!!!\n" +
                "Пожалуйста сообщите администратору!!!\n" +
                "Статус ошибки: %s\n".formatted(httpStatus) +
                "Код ошибки: %s\n".formatted(httpStatus.value()) +
                "Причина ошибки: %s\n".formatted(reason) +
                "Сообщение ошибки: %s\n".formatted(errorMessage) +
                "Время создание ошибки: %s\n".formatted(System.currentTimeMillis()) +
                "Пожалуйста сделайте скриншот экрана и отправьте скриншот администратору" +
                "@gsom21"; //TODO Вынести имя админов в отдельный класс и в будущем сделать бота для контроля ошибок
    }

    private static SendMessage createMessage(String chatId, String errorText) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(errorText)
                .build();
    }
}
