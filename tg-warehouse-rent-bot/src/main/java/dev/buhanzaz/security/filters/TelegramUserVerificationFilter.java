package dev.buhanzaz.security.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.buhanzaz.config.TelegramSecurityException;
import dev.buhanzaz.security.ErrorAuthType;
import dev.buhanzaz.security.ErrorAuthUpdate;
import dev.buhanzaz.security.filters.utils.CachedUpdateHttpServletRequest;
import dev.buhanzaz.security.model.User;
import dev.buhanzaz.security.services.TelegramBotUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

//TODO сохранять в будущем информацию о сторонних пользователях и попытках аутентификации

@Slf4j
public class TelegramUserVerificationFilter extends OncePerRequestFilter {
    private final TelegramBotUserService securityUserService;

    public TelegramUserVerificationFilter(TelegramBotUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @Override
    // TODO Нужно как то
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        Enumeration<String> headerNames = request.getHeaderNames();
        Update update = extractTelegramUserInfoFromRequest(request);
        Long userId = update.getMessage().getFrom().getId();
        String chatId = update.getMessage().getChatId().toString();
        String userName = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();

        // TODO валидация на null isEmpty и isBlank

        //TODO Ниже код который можно использовать как пример для отправки секретного кода!!!
        // нужно сначала проверить пользователя и если он не зарегестрирован или сменил данные то делать проверку на наличие команды
        /*String command = "/setcode 12345";
        String pattern = "/setcode (\\d+)";  // Ищем код после команды

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);*/

        User user = securityUserService.findUserById(userId);

        if (user == null) {
            // TODO нужно добавить проверку на временный аккаунт
            ErrorAuthUpdate authErrorUpdate = createErrorAuthUpdate(userId, chatId, ErrorAuthType.UNREGISTERED);

//            request.setAttribute("errorAuthUpdate", authErrorUpdate);
            filterChain.doFilter(new CachedUpdateHttpServletRequest(request, convertArrayByte(authErrorUpdate)), response);
            // TODO Нуж но поглядеть, так как кэш может утечь
            log.debug("Класс Update загружен класс-лоадером: {}", update.getClass().getClassLoader());
            log.debug("Класс ErrorAuthUpdate загружен класс-лоадером: {}", authErrorUpdate.getClass().getClassLoader());
            return;
        }

        //TODO Возможно стоит фильтр
        if (user.getTelegramChatId().equals(chatId) //TODO Нужна проверка на валидацию нужных строк
                && user.getTelegramUsername().equals(userName)
                && user.getTelegramFirstName().equals(firstName)) {
            //TODO Тут действия для передачи сведений для следующего фильтра авторизации
        } else {
            ErrorAuthUpdate authErrorUpdate = createErrorAuthUpdate(userId, chatId, ErrorAuthType.USER_INFORMATION_HAS_CHANGED);

            filterChain.doFilter(new CachedUpdateHttpServletRequest(request, convertArrayByte(authErrorUpdate)), response);
        }

        filterChain.doFilter(new CachedUpdateHttpServletRequest(request, convertArrayByte(update)), response);
    }

    private static @NotNull ErrorAuthUpdate createErrorAuthUpdate(Long userId, String chatId, ErrorAuthType errorAuthType) {
        return new ErrorAuthUpdate(userId, chatId, errorAuthType);
    }

    private Update extractTelegramUserInfoFromRequest(HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();

        try (BufferedReader reader = request.getReader()) {
            return mapper.readValue(reader, Update.class);
        } catch (IOException e) {
            throw new TelegramSecurityException("Error extract request", e); //TODO Exception
        }
    }

    private <T> byte[] convertArrayByte(T t) {
        ObjectMapper mapper = new ObjectMapper();
        String string = t.getClass().getClassLoader().toString();
        String string1 = t.getClass().toString();
        try {
            return mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); //TODO Exception
        }
    }
}


// TODo или использовать пароль в виде клавиатуры
//InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
//InlineKeyboardButton button1 = new InlineKeyboardButton("12345");
//button1.setCallbackData("secret_code:12345");
//InlineKeyboardButton button2 = new InlineKeyboardButton("67890");
//button2.setCallbackData("secret_code:67890");
//
//keyboard.setKeyboard(Arrays.asList(Arrays.asList(button1, button2)));
//
//SendMessage message = new SendMessage();
//message.setChatId(chatId);
//message.setText("Выберите ваш секретный код:");
//message.setReplyMarkup(keyboard);
//telegramBot.execute(message);

