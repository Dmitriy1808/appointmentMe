package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import com.example.appointmentMe.service.AppointmentCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ChoiceCityState implements BotState {

    private final AppointmentCache cache;

    public ChoiceCityState(AppointmentCache cache) {
        this.cache = cache;
    }

    @Override
    public State getState() {
        return State.CHOICE_OF_CITY;
    }

    @Override
    public State getNextState() {
        return State.MAIN_MENU;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return null;
    }
}
