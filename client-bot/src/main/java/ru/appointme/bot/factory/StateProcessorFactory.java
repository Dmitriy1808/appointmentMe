package ru.appointme.bot.factory;

import ru.appointme.bot.state.CallbackProcessor;
import ru.appointme.bot.state.MessageProcessor;
import ru.appointme.bot.state.State;
import ru.appointme.bot.state.StateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class StateProcessorFactory {

    private final Map<State, CallbackProcessor> callbackProcessors;
    private final Map<State, MessageProcessor> messageProcessors;
    private final Map<State, StateProcessor> stateProcessors;

    public StateProcessor getStateProcessorByState(State state) {
        return stateProcessors.get(state);
    }

    public CallbackProcessor getCallbackProcessorByState(State state) {
        return callbackProcessors.get(state);
    }

    public MessageProcessor getMessageProcessorByState(State state) {
        return messageProcessors.get(state);
    }

}
