package com.example.appointmentMe.bot.factory;

import com.example.appointmentMe.bot.state.CallbackProcessor;
import com.example.appointmentMe.bot.state.MessageProcessor;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.bot.state.StateProcessor;
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
