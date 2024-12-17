package dev.buhanzaz.state;

import dev.buhanzaz.handlers.exceptions.exception.unreturned.MenuStateManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
//TODO Если в будущем нужно будет использовать сложную структуру меню нужно будет перейти к FSM (Finite State Machine)
public class MenuStateManager {
    private final Map<Long, MenuState> userState = new ConcurrentHashMap<>();

    public MenuState getUserState(Long userId) {
        return userState.get(userId);
    }

    public void setUserState(Long userId, MenuState state) {
        if (userId != null) {
            if (state != null) {
                userState.put(userId, state);
                log.info("UserEntity state set to {}", state);
            } else {
                throw new IllegalArgumentException("State cannot be null");
            }
        } else {
            throw new IllegalArgumentException("UserId cannot be null");
        }
    }
}
