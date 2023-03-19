package com.example.appointmentMe.botapi;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotStateFactory {

    private final List<BotState> allStates;

    private BotStateFactory(List<BotState> allStates) {
        this.allStates = allStates;
    }

    public BotState getBotState(State state) {
        for (BotState botState : allStates) {
            if (state.equals(botState.getState())) {
                return botState;
            }
        }

        return null;
    }

}
