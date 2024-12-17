package dev.buhanzaz.config;

import dev.buhanzaz.dispatcher.UpdateDispatcher;
import dev.buhanzaz.security.ErrorAuthUpdate;
import dev.buhanzaz.security.SecurityErrorUpdateDispatcher;
import dev.buhanzaz.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Configuration
@RequiredArgsConstructor
public class BotConfig {
    private final SecurityErrorUpdateDispatcher securityErrorUpdateDispatcher;
    private final UpdateDispatcher updateDispatcher;
    private final WebhookService webhookService;


    @Bean
    public SpringTelegramWebhookBot webhookBot() {
        return new SpringTelegramWebhookBot(
                webhookService.getWebhookPath(),
                update -> {
                    String string = update.getClass().toString();
                    ClassLoader classLoader = update.getClass().getClassLoader();

                    if (update instanceof ErrorAuthUpdate) {
                        return securityErrorUpdateDispatcher.dispatcher((ErrorAuthUpdate) update);
                    } else {
                        return updateDispatcher.dispatcher(update);
                    }
                },//TODO Создать прослойку которая будет определять валидный update или нет security
                webhookService::registerWebhook,
                webhookService::deleteWebhook);
    }
}
