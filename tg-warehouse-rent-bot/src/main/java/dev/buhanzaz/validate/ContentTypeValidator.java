package dev.buhanzaz.validate;

import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Set;

//TODO В будущем перетечет в отдельный утилитарный класс
public final class ContentTypeValidator {
    private ContentTypeValidator() {
    }

    private static final Set<String> mime = Set.of("image/png", "image/jpeg", "image/pjpeg", "image/gif",
            "image/webp", "image/heic", "image/heif", "image/avif");

    public static boolean isContainsMimeType(String mimeType) {
        return mime.contains(mimeType);

    }

    public static boolean isImageDocument(Message message) {
        Document document = message.getDocument();
        String mimeType = document.getMimeType();

        return isContainsMimeType(mimeType);
    }
}
