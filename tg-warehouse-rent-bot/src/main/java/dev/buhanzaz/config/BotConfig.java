package dev.buhanzaz.config;

import dev.buhanzaz.dispatcher.UpdateDispatcher;
import dev.buhanzaz.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Configuration
@RequiredArgsConstructor
public class BotConfig {
    private final UpdateDispatcher updateDispatcher;
    private final WebhookService webhookService;


    @Bean
    public SpringTelegramWebhookBot webhookBot() {
        return new SpringTelegramWebhookBot(
                webhookService.getWebhookPath(),
                updateDispatcher::dispatcher,
                webhookService::registerWebhook,
                webhookService::deleteWebhook);
    }
}
