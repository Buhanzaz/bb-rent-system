package dev.buhanzaz.config;

import dev.buhanzaz.properties.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final RabbitMQProperties properties;

    // Spring bean for queue (store json messages)
    @Bean
    public Queue jsonQueue() {
        return new Queue(properties.getJsonQueue(), true);
    }

    // Spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(properties.getExchange(), true, false);
    }

    // Binding between json queue and exchange using routing key
    @Bean
    public Binding jsonBinding() {
        return BindingBuilder
                .bind(jsonQueue())
                .to(exchange())
                .with(properties.getRoutingJsonKey());
    }

    // Spring Bean for json converter
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    // Spring Bean for AmqpTemplate configuration
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        RetryTemplate retryTemplate = new RetryTemplate();
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setRetryTemplate(retryTemplate);
        return rabbitTemplate;
    }
}
