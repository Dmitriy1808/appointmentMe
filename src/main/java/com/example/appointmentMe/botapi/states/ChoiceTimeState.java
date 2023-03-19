package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import com.example.appointmentMe.service.AppointmentCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ChoiceTimeState implements BotState {

    private final AppointmentCache cache;

    public ChoiceTimeState(AppointmentCache cache) {
        this.cache = cache;
    }

    @Override
    public State getState() {
        return State.CHOICE_OF_TIME;
    }

    @Override
    public State getNextState() {
        return State.FILL_NAME;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return testSendingInternal(update);
    }

    private BotApiMethod<?> testSendingInternal(Update update) {
        return SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text("ALL GOOD")
                .build();
    }

}
