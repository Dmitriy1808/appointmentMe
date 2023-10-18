package com.example.appointmentMe.bot.factory;

import com.example.appointmentMe.bot.state.State;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class ExistsClientPipelineFactory {

    public Optional<State> getNextStateFor(State state) {
        return switch (state) {
            case EXISTING_USER_DETECTED -> Optional.of(State.APPOINTMENT_MANAGE);
            case APPOINTMENT_MANAGE -> Optional.of(State.APPOINTMENT_DECLINE);
            case EXISTING_USER_PIPELINE_INITIAL_STATE, APPOINTMENT_DECLINE -> Optional.of(State.EXISTING_USER_DETECTED);
            default -> Optional.empty();
        };
    }

    public Optional<State> getPrevStateFor(State state) {
        return switch (state) {
            case EXISTING_USER_DETECTED -> Optional.of(State.EXISTING_USER_PIPELINE_INITIAL_STATE);
            case APPOINTMENT_MANAGE, APPOINTMENT_DECLINE -> Optional.of(State.EXISTING_USER_DETECTED);
            default -> Optional.empty();
        };
    }

}
