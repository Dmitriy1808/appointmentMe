package com.example.appointmentMe.bot.factory;

import com.example.appointmentMe.bot.state.State;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class StateFactory {
    private final List<State> EXISTS_CLIENT_PIPELINE_STATES = List.of(State.EXISTING_USER_DETECTED,
            State.APPOINTMENT_MANAGE, State.APPOINTMENT_DECLINE, State.EXISTING_USER_PIPELINE_INITIAL_STATE);

    public Optional<State> getNextStateFor(State state) {
        return EXISTS_CLIENT_PIPELINE_STATES.contains(state)
                ? ExistsClientPipelineFactory.getNextStateFor(state)
                : NewClientPipelineFactory.getNextStateFor(state);
    }

    public Optional<State> getPrevStateFor(State state) {
        return EXISTS_CLIENT_PIPELINE_STATES.contains(state)
                ? ExistsClientPipelineFactory.getPrevStateFor(state)
                : NewClientPipelineFactory.getPrevStateFor(state);
    }

}
