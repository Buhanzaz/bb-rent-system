package dev.buhanzaz.service.impl;

import dev.buhanzaz.handlers.exceptions.exception.unreturned.BotWebhookException;
import dev.buhanzaz.properties.BotConfigProperties;
import dev.buhanzaz.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
class WebhookServiceImpl implements WebhookService {
    private final TelegramClient telegramClient;
    private final BotConfigProperties properties;

    public String getWebhookPath() {
        return properties.getWebhookPath();
    }

    public void registerWebhook() {
        final String fullBotUrl = "%s/%s".formatted(properties.getWebhookDomain(), properties.getWebhookPath());

        try {
            SetWebhook webhook = SetWebhook.builder()
                    .url(fullBotUrl)
                    .build();

            telegramClient.execute(webhook);
            log.info("Webhook registered successfully");
        } catch (TelegramApiException e) {
            log.error("Error registering webhook", e);
            throw new BotWebhookException("Error registering webhook", e);
        }
    }

    public void deleteWebhook() {
        try {
            telegramClient.execute(new DeleteWebhook());
            log.info("Webhook delete successfully");
        } catch (TelegramApiException e) {
            log.error("Error delete webhook", e);
            throw new BotWebhookException("Error delete webhook", e);
        }
    }
}
