package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.CallbackProcessor;
import com.example.appointmentMe.botapi.state.State;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Setter
public class NoFreeWorkTimeState implements BotState, CallbackProcessor {

    private State nextState = State.NO_FREE_WORK_TIME;

    @Override
    public State getState() {
        return State.NO_FREE_WORK_TIME;
    }

    @Override
    public State getNextState() {
        return nextState;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("No free work time at this day")
                .replyMarkup(getReplyMarkup())
                .build();
    }

    private ReplyKeyboard getReplyMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(
                    List.of(InlineKeyboardButton.builder()
                        .text("Choice another day")
                        .callbackData("true")
                        .build())
                )
                .build();
    }

    @Override
    public void processCallback(CallbackQuery callback) {
//        TODO change nextState
    }
}
