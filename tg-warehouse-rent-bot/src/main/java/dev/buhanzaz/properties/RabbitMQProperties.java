package dev.buhanzaz.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class RabbitMQProperties {
    private final String jsonQueue;
    private final String exchange;
    private final String routingJsonKey;

    public RabbitMQProperties(@Value("${rabbitmq.queue.json.name}") String jsonQueue,
                              @Value("${rabbitmq.exchange.name}") String exchange,
                              @Value("${rabbitmq.routing.json.key}") String routingJsonKey) {
        this.jsonQueue = jsonQueue;
        this.exchange = exchange;
        this.routingJsonKey = routingJsonKey;
    }
}
