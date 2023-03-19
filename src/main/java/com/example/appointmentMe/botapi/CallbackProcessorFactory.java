package com.example.appointmentMe.botapi;

import com.example.appointmentMe.botapi.state.CallbackProcessor;
import com.example.appointmentMe.botapi.state.State;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CallbackProcessorFactory {

    private final List<CallbackProcessor> processors;

    private CallbackProcessorFactory(List<CallbackProcessor> processors) {
        this.processors = processors;
    }

    public Optional<CallbackProcessor> getCallbackProcessorByState(State state) {
        for (CallbackProcessor processor : processors) {
            if (state.equals(processor.getState())) {
                return Optional.of(processor);
            }
        }

        return Optional.empty();
    }

}
