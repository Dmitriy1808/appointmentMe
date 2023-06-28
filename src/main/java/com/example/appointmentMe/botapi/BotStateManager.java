package com.example.appointmentMe.botapi;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Getter
@Setter
public class BotStateManager {

    private final BotStateFactory stateFactory;
    private final CallbackProcessorFactory callbackProcessorFactory;
    private final MessageProcessorFactory messageProcessorFactory;
    private BotState currentState;

    public BotStateManager(BotStateFactory stateFactory, CallbackProcessorFactory callbackProcessorFactory, MessageProcessorFactory messageProcessorFactory) {
        this.stateFactory = stateFactory;
        this.callbackProcessorFactory = callbackProcessorFactory;
        this.messageProcessorFactory = messageProcessorFactory;
    }

    @PostConstruct
    public void init() {
        this.currentState = stateFactory.getBotState(State.MAIN_MENU);
    }

    public BotApiMethod<?> handleRequest(Update update) {
        boolean hasCallbackQuery = update.hasCallbackQuery();
        if (hasCallbackQuery) {
            callbackProcessorFactory.getCallbackProcessorByState(currentState.getState())
                    .ifPresent(callbackProcessor -> callbackProcessor.processCallback(update.getCallbackQuery()));
            changeState();
            return currentState.process(update);
        }

        boolean isCommand = update.getMessage().isCommand();
        if (!isCommand && update.getMessage().hasText()) {
            messageProcessorFactory.getMessageProcessorByState(currentState.getState())
                    .ifPresent(callbackProcessor -> callbackProcessor.processMessage(update.getMessage()));
            changeState();
            return currentState.process(update);
        }

        String commandText = update.getMessage().getText();
        if (isCommand && ("/mainmenu".equals(commandText) || "/start".equals(commandText))) {
            setCurrentState(stateFactory.getBotState(State.MAIN_MENU));
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .replyMarkup(getReplyKeyboardMarkup())
                    .text("Initial state returned")
                    .build();
        }

        changeState();
        return currentState.process(update);
    }

    private ReplyKeyboard getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setOneTimeKeyboard(true);
        replyKeyboard.setResizeKeyboard(true);

        KeyboardRow appointButton = new KeyboardRow();
        appointButton.add(new KeyboardButton("Make appointment"));

        replyKeyboard.setKeyboard(List.of(appointButton));
        return replyKeyboard;
    }

    private void changeState() {
        BotState newBotState = stateFactory.getBotState(currentState.getNextState());
        setCurrentState(newBotState);
    }

}
