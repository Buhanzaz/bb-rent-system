package dev.buhanzaz.handlers.message.impl;

import dev.buhanzaz.handlers.exceptions.exception.returned.CabinManagerException;
import dev.buhanzaz.handlers.message.ContentHandler;
import dev.buhanzaz.model.MessageToQueue;
import dev.buhanzaz.producer.MessageProducer;
import dev.buhanzaz.state.CabinIdStateManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static dev.buhanzaz.factory.MessageToQueueFactory.createImageMessage;
import static dev.buhanzaz.utils.MessageUtils.getUserId;

@Slf4j
@Component(value = "content")
@RequiredArgsConstructor
class ContentUrlContentHandler implements ContentHandler {
    private final CabinIdStateManager cabinIdStateManager;
    private final MessageProducer messageProducer;

    @Override
    public BotApiMethod<?> handler(Message message) {
        Long cabinId = getCabinId(message);
        MessageToQueue messageToQueue = createMessageForQueue(message, cabinId);

        messageProducer.produce(messageToQueue);
        return null; //TODO Смотри в minIO Идею с возвратом состояния загрузки
    }

    //TODO Бля нужно все же обдумать как будет работать определение id
    private Long getCabinId(Message message) {
        Long cabinId = cabinIdStateManager.getCabinId(getUserId(message));

        if (cabinId == null) {
            throw new CabinManagerException(
                    "The temporarily saved cabin id associated with your id was not found.", message);
        }

        return cabinId;
    }

    private MessageToQueue createMessageForQueue(Message message, Long cabinId) {
        return createImageMessage(message, cabinId);
    }
}
