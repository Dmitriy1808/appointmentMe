package com.example.appointmentMe.bot;

import com.example.appointmentMe.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Slf4j
@Component
public class AppointMeBot extends TelegramWebhookBot {

    private final BotConfig config;
    private final BotStateManager stateManager;

    public AppointMeBot(BotConfig config, BotStateManager stateManager) {
        super();
        this.config = config;
        this.stateManager = stateManager;
        SetMyCommands.builder()
                .command(
                        new BotCommand("/mainmenu", "returns main menu state")
                )
                .build();
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
        return null;
    }
}
