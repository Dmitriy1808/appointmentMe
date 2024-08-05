package ru.appointme.bot.factory;

import ru.appointme.bot.state.State;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExistsClientPipelineFactory {

    public State getNextStateFor(State state) {
        return switch (state) {
            case EXISTING_USER_DETECTED -> State.APPOINTMENT_MANAGE;
            case APPOINTMENT_MANAGE -> State.APPOINTMENT_DECLINE;
            case EXISTING_USER_PIPELINE_INITIAL_STATE, APPOINTMENT_DECLINE -> State.EXISTING_USER_DETECTED;
            default -> State.EXISTING_USER_DETECTED;
        };
    }

    public State getPrevStateFor(State state) {
        return switch (state) {
            case EXISTING_USER_DETECTED -> State.EXISTING_USER_PIPELINE_INITIAL_STATE;
            case APPOINTMENT_MANAGE, APPOINTMENT_DECLINE -> State.EXISTING_USER_DETECTED;
            default -> State.EXISTING_USER_DETECTED;
        };
    }

}
