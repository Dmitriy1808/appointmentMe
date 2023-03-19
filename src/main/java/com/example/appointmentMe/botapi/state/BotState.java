package com.example.appointmentMe.botapi.state;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotState {

    State getState();

    State getNextState();

    BotApiMethod<?> process(Update update);

}
