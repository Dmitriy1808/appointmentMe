package com.example.appointmentMe.botapi;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.CallbackProcessor;
import com.example.appointmentMe.botapi.state.State;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

@Component
@Getter
@Setter
public class BotStateManager {

    private final BotStateFactory stateFactory;
    private final CallbackProcessorFactory callbackProcessorFactory;
    private BotState currentState;

    public BotStateManager(BotStateFactory stateFactory, CallbackProcessorFactory callbackProcessorFactory) {
        this.stateFactory = stateFactory;
        this.callbackProcessorFactory = callbackProcessorFactory;
    }

    @PostConstruct
    public void init() {
        this.currentState = stateFactory.getBotState(State.MAIN_MENU);
    }

    public BotApiMethod<?> handleRequest(Update update) {
        if (update.hasCallbackQuery()) {
            callbackProcessorFactory.getCallbackProcessorByState(currentState.getState())
                    .ifPresent(callbackProcessor -> callbackProcessor.processCallback(update.getCallbackQuery()));
            changeState();
            return currentState.process(update);
        }
        if (update.getMessage().isCommand() && "/mainmenu".equals(update.getMessage().getText())) {
            setCurrentState(stateFactory.getBotState(State.MAIN_MENU));
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Initial state returned")
                    .build();
        }

        changeState();
        return currentState.process(update);
    }

    private void changeState() {
        BotState newBotState = stateFactory.getBotState(currentState.getNextState());
        setCurrentState(newBotState);
    }

}
