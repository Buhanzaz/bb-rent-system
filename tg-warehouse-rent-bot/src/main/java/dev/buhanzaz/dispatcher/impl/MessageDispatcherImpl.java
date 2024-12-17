package dev.buhanzaz.dispatcher.impl;

import dev.buhanzaz.dispatcher.MessageDispatcher;
import dev.buhanzaz.handlers.exceptions.exception.returned.DispatcherMessageException;
import dev.buhanzaz.handlers.message.ContentHandler;
import dev.buhanzaz.state.CabinIdStateManager;
import dev.buhanzaz.state.MenuState;
import dev.buhanzaz.state.MenuStateManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static dev.buhanzaz.utils.MessageUtils.getUserId;
import static dev.buhanzaz.validate.ContentTypeValidator.isImageDocument;
import static dev.buhanzaz.validate.MenuValidated.isAddImageMenu;

@Slf4j
@Component
// TODO Нужно придумать проверку чтобы в imageStateService не было одинаковых chat_id
public class MessageDispatcherImpl implements MessageDispatcher {
    private final ContentHandler textHandler;
    private final ContentHandler contentUrlHandler;
    private final ContentHandler commandHandler;
    private final MenuStateManager menuStateManager;
    private final CabinIdStateManager cabinIdStateManager;

    public MessageDispatcherImpl(@Qualifier(value = "text") ContentHandler textHandler,
                                 @Qualifier(value = "content") ContentHandler contentUrlHandler,
                                 @Qualifier(value = "command") ContentHandler commandHandler,
                                 MenuStateManager menuStateManager,
                                 CabinIdStateManager cabinIdStateManager) {
        this.textHandler = textHandler;
        this.contentUrlHandler = contentUrlHandler;
        this.commandHandler = commandHandler;
        this.menuStateManager = menuStateManager;
        this.cabinIdStateManager = cabinIdStateManager;
    }

    public BotApiMethod<?> dispatcher(Message message) {
        log.debug("Successfully dispatched message {}", message);
        if (message.isCommand()) {
            return commandHandler.handler(message);
        }

        //TODO Нужно хранить состояние в памяти или на диске по id и имени если имя сменилось проверять и если время хранение то же удалять


        if (message.hasText()) {
            return textHandler.handler(message);
        }

        if (message.hasPhoto() || message.hasDocument() && isImageDocument(message)) {
            return processImageAddition(message);
        }

        throw new DispatcherMessageException("Exception when defining message type in manager", message);
    }

    private BotApiMethod<?> processImageAddition(Message message) {
        final Long userId = getUserId(message);
        final MenuState userState = menuStateManager.getUserState(userId);

        if (!isAddImageMenu(userState)) {
            throw new DispatcherMessageException("Menu state exception", message);
        }

        if (!cabinIdStateManager.containsCabinId(userId)) {
            throw new DispatcherMessageException("Exception cabin id was not found in temporary storage", message);
        }

        return contentUrlHandler.handler(message);
    }
}
