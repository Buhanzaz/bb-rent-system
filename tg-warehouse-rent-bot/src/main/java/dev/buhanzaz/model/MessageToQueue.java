package dev.buhanzaz.model;

import lombok.Getter;
import lombok.Value;

import java.io.Serializable;

@Value
@Getter
//TODO Вынести в отдельный утилитарный модуль
public class MessageToQueue implements Serializable {
    Long userId;
    String chatId;
    Long cabinId;
    String fileId;
    Long unixCreatedTime;

    public MessageToQueue(Long userId, String chatId, String fileId, Long cabinId) {
        this.userId = userId;
        this.chatId = chatId;
        this.cabinId = cabinId;
        this.fileId = fileId;
        this.unixCreatedTime = System.currentTimeMillis();// TODO В будущем добавить часовые пояса
    }
}
