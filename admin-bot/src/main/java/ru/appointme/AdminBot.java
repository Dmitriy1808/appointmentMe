package ru.appointme;

import ru.appointme.config.BotConfig;
import ru.appointme.state.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Slf4j
public class AdminBot extends TelegramWebhookBot {

    private final BotConfig config;
    private final BotManager botManager;

    public AdminBot(BotConfig config, BotManager botManager) {
        super();
        this.config = config;
        this.botManager = botManager;
        setupMenuWithCommands();
    }

    private void setupMenuWithCommands() {
        SetMyCommands commands = SetMyCommands.builder()
                .commands(List.of(
                        new BotCommand(State.INITIAL_STATE.getCommand(), "Returns main menu state"),
                        new BotCommand(State.GET_ALL_APPOINTMENTS.getCommand(), "Gets all scheduled appointments"),
                        new BotCommand(State.DECLINE_APPOINTMENT.getCommand(), "Select appointment for decline"),
                        new BotCommand(State.ADD_WORK_TIME.getCommand(), "Add work time"))
                )
                .languageCode("EN")
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
        return botManager.handleRequest(update);
    }

    @Override
    public String getBotPath() {
        return "/admin";
    }
}
