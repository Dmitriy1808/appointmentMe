package com.example.appointmentMe.bot.state;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface StateProcessor {

    State getState();

    BotApiMethod<?> process(Update update);

}
