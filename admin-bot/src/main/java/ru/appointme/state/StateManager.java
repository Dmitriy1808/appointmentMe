package ru.appointme.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class StateManager {

    private final Map<State, StateProcessor> stateProcessors;
    private final Map<State, MessageProcessor> messageProcessors;
    private final Map<State, CallbackProcessor> callbackProcessors;

    public StateProcessor getStateProcessor(State state) {
        return stateProcessors.get(state);
    }

    public MessageProcessor getMessageProcessor(State state) {
        return messageProcessors.get(state);
    }

    public CallbackProcessor getCallbackProcessor(State state) {
        return callbackProcessors.get(state);
    }
}
