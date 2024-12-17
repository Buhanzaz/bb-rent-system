package dev.buhanzaz.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public final class BotConfigProperties {
    private final String webhookDomain;
    private final String webhookPath;
    private final String botToken;
    private final String apiTelegramUrl;

    public BotConfigProperties(@Value("${telegram.bot.webhook.domain}") String webhookDomain,
                               @Value("${telegram.bot.webhook.path}") String webhookPath,
                               @Value("${telegram.bot.token}") String botToken,
                               @Value("${telegram.api.url}") String apiTelegramUrl) {
        this.webhookDomain = webhookDomain;
        this.webhookPath = webhookPath;
        this.botToken = botToken;
        this.apiTelegramUrl = apiTelegramUrl;
    }
}
