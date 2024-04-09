package com.example.appointmentMe.bot;

import com.example.appointmentMe.bot.notification.UserParams;
import com.example.appointmentMe.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class AppointMeBot extends TelegramWebhookBot {
    private String setMyCommandsUrl = "https://api.telegram.org/bot%s/setMyCommands=%s?language_code=%s";

    private final BotConfig config;
    private final BotStateManager stateManager;
    private final RestTemplate restTemplate;

    public AppointMeBot(BotConfig config, BotStateManager stateManager, RestTemplate restTemplate) {
        super();
        this.config = config;
        this.stateManager = stateManager;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        setupMenuWithCommands();
    }

    private void setupMenuWithCommands() {
        SetMyCommands commands = SetMyCommands.builder()
                .commands(List.of(
                        new BotCommand("/start", "Returns main menu state"),
                        new BotCommand("/help", "Show help information"),
                        new BotCommand("/about", "About bot"),
                        new BotCommand("/lastappointment", "Gets last scheduled appointment"),
                        new BotCommand("/allappointments", "Gets appointment for all time"))
                )
                .languageCode("en")
                .build();
        try {
            this.execute(commands);
        } catch (TelegramApiException e) {
            log.error("Failed to setup help commands menu {}", e.getLocalizedMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return stateManager.handleRequest(update);
    }

    @Override
    public String getBotPath() {
        return "/client";
    }
}
