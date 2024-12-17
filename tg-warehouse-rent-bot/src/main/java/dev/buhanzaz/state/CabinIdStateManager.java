package dev.buhanzaz.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
// TODO Нужно решить как нормально удалять состояние при выходе из меню в идеале в отправленном сообщении должно
//  быть inline menu которое могло бы повторить выход к состоянию и меню без повторного ввода на будущее
// TODO Переименовать нормально методы и класс
// При презагрузке сервера в вехуках обнулять базу а может и нет, но при ошибке нужно чистить состояние
public class CabinIdStateManager {
    private final Map<Long, Long> states = new ConcurrentHashMap<>();

    public void putCabinId(Long userId, Long cabinId) {
        states.put(userId, cabinId);
    }

    public void deleteCabinId(Long userId) {
        states.remove(userId);
    }

    public boolean containsCabinId(Long userId) {
        return states.containsKey(userId);
    }

    public Long getCabinId(Long userId) {
        return states.getOrDefault(userId, null);
    }
}

