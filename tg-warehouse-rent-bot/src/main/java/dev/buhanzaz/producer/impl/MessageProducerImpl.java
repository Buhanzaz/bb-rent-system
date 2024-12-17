package dev.buhanzaz.producer.impl;

import dev.buhanzaz.handlers.exceptions.exception.returned.CustomAmqpException;
import dev.buhanzaz.model.MessageToQueue;
import dev.buhanzaz.producer.MessageProducer;
import dev.buhanzaz.properties.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class MessageProducerImpl implements MessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    @Override
    public void produce(MessageToQueue message) {
        try {
            rabbitTemplate.convertAndSend(properties.getExchange(), properties.getRoutingJsonKey(), message);
            log.info("Message sent to queue RabbitMQ \nMessage: {}", message);
        } catch (AmqpException ex) {
            String chatId = message.getChatId();
            throw new CustomAmqpException(chatId, ex);
        }
    }
}
