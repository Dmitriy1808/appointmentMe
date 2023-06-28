package com.example.appointmentMe.botapi;

import com.example.appointmentMe.botapi.state.MessageProcessor;
import com.example.appointmentMe.botapi.state.State;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MessageProcessorFactory {
    private final List<MessageProcessor> processors;

    private MessageProcessorFactory(List<MessageProcessor> processors) {
        this.processors = processors;
    }

    public Optional<MessageProcessor> getMessageProcessorByState(State state) {
        for (MessageProcessor processor : processors) {
            if (state.equals(processor.getState())) {
                return Optional.of(processor);
            }
        }

        return Optional.empty();
    }
}
