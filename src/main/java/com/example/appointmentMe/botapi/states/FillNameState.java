package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class FillNameState implements BotState {
    private static final String CONCLUSION_MESSAGE_MOCK = "It's conclusion message! Forgot it";

//    private final BotStateManager context;
//    private final BotState startState;
//
//    public FillNameState(BotStateManager context) {
//        this.context = context;
//        this.startState = context.findState(State.MAIN_MENU);
//    }

    @Override
    public State getState() {
        return State.FILL_NAME;
    }

    @Override
    public State getNextState() {
        return State.MAIN_MENU;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        log.info("Customer name: {}", update.getMessage().getText());
//        context.setCurrentState(startState);
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(CONCLUSION_MESSAGE_MOCK)
                .build();
    }

}
