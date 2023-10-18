package com.example.appointmentMe.bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandProcessor {

    BotApiMethod<?> processCommand(Update update);

    Command getName();

}
