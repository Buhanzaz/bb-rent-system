package dev.buhanzaz.producer;

import dev.buhanzaz.model.MessageToQueue;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

public interface MessageProducer {
    void produce(MessageToQueue message);
}
