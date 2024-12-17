package dev.buhanzaz.factory;

import dev.buhanzaz.handlers.exceptions.exception.unreturned.PhotoSizeException;
import dev.buhanzaz.model.MessageToQueue;
import dev.buhanzaz.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Comparator;
import java.util.List;

@Slf4j
public final class MessageToQueueFactory {
    public static MessageToQueue createImageMessage(Message message, Long cabinId) {
        String chatId = MessageUtils.getStringChatId(message);
        Long userId = MessageUtils.getUserId(message);
        return new MessageToQueue(userId, chatId, getFileId(message), cabinId);
    }

    private static String getFileId(Message message) {
        String fileId;

        if (message.hasPhoto()) {
            List<PhotoSize> photoSizesList = message.getPhoto();
            fileId = getPhotoFileId(photoSizesList);
            log.debug("Message has photo id: {}", fileId);
        } else if (message.hasDocument()) {
            fileId = message.getDocument().getFileId();
            log.debug("Message has document id: {}", fileId);
        } else {
            log.error("Message has no photo");
            throw new IllegalArgumentException("Message has no photo");
        }

        return fileId;
    }

    private static String getPhotoFileId(List<PhotoSize> photoSizesList) {
        return photoSizesList
                .stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElseThrow(()-> new IllegalArgumentException("Error when getting the maximum photo size"))
                .getFileId();
    }
}
