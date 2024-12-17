//package dev.buhanzaz.config;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.apache.catalina.Session;
//import org.springframework.security.config.annotation.web.session.SessionSecurityMarker;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Класс представляет собой фильтр для определения входит ли данный юзер в категорию разрешенных пользователей.
// * Для доступа в приложение нужно ввести кодовое слово, которое в будущем будет находиться в зарегистрированном ак-те,
// * данной экосистемы.
// */
//@RequiredArgsConstructor
//public class TelegramSessionFilter extends OncePerRequestFilter {
//    private final TelegramSessionManager sessionManager;
//    private final ObjectMapper mapper;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final Map<String, String> userInfo = extractTelegramUserInfoFromRequest(request);
//
//        String userId = userInfo.get("userId");
//
//        if(sessionManager.isSessionActive(userId)) {
//            sessionManager.updateSession(userId);
//        } else {
//            request.setAttribute("sessionInvalid", true);
//            request.setAttribute("userId", userId);
//        }
//
//        filterChain.doFilter(request, response);
//        return null;
//    }
//
//    private Map<String, String> extractTelegramUserInfoFromRequest(final HttpServletRequest request) {
//        if (request == null) {
//            throw new TelegramSecurityException("Request not be null");
//        }
//
//        JsonNode rootNode;
//        Map<String, String> userInfo = new HashMap<>();
//
//        try (BufferedReader reader = request.getReader()) {
//            rootNode = mapper.readTree(reader);
//        } catch (IOException e) {
//            throw new TelegramSecurityException("Error extract request", e);
//        }
//
//        //TODO сохранять в будущем информацию о сторонних пользователях и попытках аутентификации
//        if (rootNode != null
//                && rootNode.has("update")
//                && rootNode.get("update").has("message")
//                && rootNode.get("update").get("message").has("chat")
//                && rootNode.get("update").get("message").get("chat").has("id")
//                && rootNode.get("update").get("message").has("user")
//                && rootNode.get("update").get("message").get("user").has("id")
//                && rootNode.get("update").get("message").get("user").has("firstName")
//                && rootNode.get("update").get("message").get("user").has("userName")
//        ) {
//            String chatId = rootNode.get("update").get("message").get("chat").get("id").textValue();
//            String userId = rootNode.get("update").get("message").get("user").get("id").textValue();
//            String firstName = rootNode.get("update").get("message").get("user").get("firstName").textValue();
//            String userName = rootNode.get("update").get("message").get("user").get("userName").textValue();
//
//            userInfo.put("userId", userId);
//            userInfo.put("chatId", chatId);
//            userInfo.put("userName", userName);
//            userInfo.put("firstName", firstName);
//            return userInfo;
//        }
//
//        throw new TelegramSecurityException("No relevant field found.");
//    }
//}
