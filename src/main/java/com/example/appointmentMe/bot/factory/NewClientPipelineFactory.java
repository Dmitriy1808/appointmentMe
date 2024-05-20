package com.example.appointmentMe.bot.factory;

import com.example.appointmentMe.bot.state.State;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NewClientPipelineFactory {

    public State getNextStateFor(State state) {
        return switch (state) {
            case FILL_NAME -> State.CHOICE_OF_CITY;
            case CHOICE_OF_CITY, EXISTING_USER_DETECTED -> State.CHOICE_OF_DATE;
            case CHOICE_OF_DATE -> State.CHOICE_OF_TIME;
            case CHOICE_OF_TIME -> State.FINAL_STATE;
            default -> State.FILL_NAME;
        };
    }

    public State getPrevStateFor(State state) {
        return switch (state) {
            case CHOICE_OF_CITY -> State.FILL_NAME;
            case CHOICE_OF_DATE -> State.CHOICE_OF_CITY;
            case CHOICE_OF_TIME -> State.CHOICE_OF_DATE;
            case FINAL_STATE -> State.CHOICE_OF_TIME;
            default -> State.FILL_NAME;
        };
    }

}
