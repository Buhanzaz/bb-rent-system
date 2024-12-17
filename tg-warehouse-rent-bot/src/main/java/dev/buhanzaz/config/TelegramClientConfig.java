package dev.buhanzaz.config;

import dev.buhanzaz.properties.BotConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramClientConfig {
    @Bean
    public TelegramClient telegramClient(BotConfigProperties properties) {
        return new OkHttpTelegramClient(properties.getBotToken());
    }
}
