package com.example.appointmentMe.bot.factory;

import com.example.appointmentMe.bot.state.State;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class NewClientPipelineFactory {

    public Optional<State> getNextStateFor(State state) {
        return switch (state) {
            case FILL_NAME -> Optional.of(State.CHOICE_OF_CITY);
            case CHOICE_OF_CITY, EXISTING_USER_DETECTED -> Optional.of(State.CHOICE_OF_DATE);
            case CHOICE_OF_DATE -> Optional.of(State.CHOICE_OF_TIME);
            case CHOICE_OF_TIME -> Optional.of(State.FINAL_STATE);
            default -> Optional.empty();
        };
    }

    public Optional<State> getPrevStateFor(State state) {
        return switch (state) {
            case CHOICE_OF_CITY -> Optional.of(State.FILL_NAME);
            case CHOICE_OF_DATE -> Optional.of(State.CHOICE_OF_CITY);
            case CHOICE_OF_TIME -> Optional.of(State.CHOICE_OF_DATE);
            case FINAL_STATE -> Optional.of(State.CHOICE_OF_TIME);
            default -> Optional.empty();
        };
    }

}
