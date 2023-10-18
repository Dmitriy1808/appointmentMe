package com.example.appointmentMe.bot.command.impl;

import com.example.appointmentMe.bot.Utils;
import com.example.appointmentMe.bot.command.Command;
import com.example.appointmentMe.bot.command.CommandProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand implements CommandProcessor {

    private static final String HELP_COMMAND_MESSAGE_TEMPLATE = "HELP_COMMAND_MESSAGE_TEMPLATE";

    @Override
    public BotApiMethod<?> processCommand(Update update) {
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(HELP_COMMAND_MESSAGE_TEMPLATE)
                .build();
    }

    @Override
    public Command getName() {
        return Command.HELP;
    }
}
