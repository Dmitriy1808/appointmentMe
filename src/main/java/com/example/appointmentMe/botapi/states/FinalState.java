package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class FinalState implements BotState {

    @Override
    public State getState() {
        return State.FINAL_STATE;
    }

    @Override
    public State getNextState() {
        return null;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Congratulations! You're appointed.")
                .build();
    }
}
