package dev.buhanzaz.service;

public interface WebhookService {
    String getWebhookPath();
    void registerWebhook();
    void deleteWebhook();
}
