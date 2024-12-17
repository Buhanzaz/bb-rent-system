package dev.buhanzaz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.BufferedReader;
import java.io.IOException;

//TODO сохранять в будущем информацию о сторонних пользователях и попытках аутентификации

public class TelegramUserVerificationFilter extends OncePerRequestFilter {
    private final TelegramBotUserService securityUserService;
    private final TelegramClient telegramClient;

    public TelegramUserVerificationFilter(TelegramBotUserService securityUserService, TelegramClient telegramClient) {
        this.securityUserService = securityUserService;
        this.telegramClient = telegramClient;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        Update update = extractTelegramUserInfoFromRequest(request);
        Long userId = update.getMessage().getFrom().getId();
        String chatId = update.getMessage().getChatId().toString();
        String userName = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        // TODO валидация на null isEmpty и isBlank
        User user = securityUserService.findUserById(userId);

        if (user == null) {
            String authenticationErrorMessage = "Вы не зарегистрированы";

            sendAuthenticationErrorMessage(chatId, authenticationErrorMessage);
        }

        //TODO Возможно стоит фильтр
//        if (user.getTelegramChatId().equals(chatId) //TODO Нужна проверка на валидацию нужных строк
//                && user.getTelegramUsername().equals(userName)
//                && user.getTelegramFirstName().equals(firstName)) {
//            //TODO Тут действия для передачи сведений для следующего фильтра авторизации
//        } else {
//            String authenticationErrorMessage = "Вы изменили свои учетные данные в телеграмм!\n" +
//                    "Введите секретный пароль для обновление данных";
//
//            sendAuthenticationErrorMessage(chatId, authenticationErrorMessage);
//        }
        response.setStatus(200);
        filterChain.doFilter(request, response);
    }

    private void sendAuthenticationErrorMessage(String chatId, String message) {
        try {
            SendMessage authenticationErrorMessage = new SendMessage(chatId,
                    "Вы изменили свои учетные данные в телеграмм!\n" +
                            "Введите секретный пароль для обновление данных");
            telegramClient.execute(authenticationErrorMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //TODO Обработка ошибок
        }
    }

    private Update extractTelegramUserInfoFromRequest(HttpServletRequest request) {
        if (request == null) {
            throw new TelegramSecurityException("Request not be null");
        }

        ObjectMapper mapper = new ObjectMapper();

        try (BufferedReader reader = request.getReader()) {
            return mapper.readValue(reader, Update.class);
        } catch (IOException e) {
            throw new TelegramSecurityException("Error extract request", e);
        }
    }
}

