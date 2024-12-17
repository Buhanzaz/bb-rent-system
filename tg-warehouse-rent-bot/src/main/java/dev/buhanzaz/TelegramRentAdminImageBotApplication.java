package dev.buhanzaz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramRentAdminImageBotApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TelegramRentAdminImageBotApplication.class);
        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run(args);
    }
}
