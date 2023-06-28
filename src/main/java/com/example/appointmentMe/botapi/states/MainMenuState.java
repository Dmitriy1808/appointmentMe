package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class MainMenuState implements BotState {

//    private final BotStateManager context;
//    private final BotState nextState;
//
//        this.context = context;
//        this.nextState = context.findState(State.CHOICE_OF_DATE);
//    }

    @Override
    public State getState() {
        return State.MAIN_MENU;
    }

    @Override
    public State getNextState() {
        return State.FILL_NAME;    //    CORRECT STATE - choiceCity
    }

    @Override
    public BotApiMethod<?> process(Update update) {
//        context.setCurrentState(nextState);
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Welcome to AppointmentMe bot! Do you want to appoint?)")
                .replyMarkup(getReplyKeyboardMarkup())
                .build();
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

}
