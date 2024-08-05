package ru.appointme.state.processors;

import ru.appointme.Utils;
import ru.appointme.state.State;
import ru.appointme.state.StateProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class InitialStateProcessor implements StateProcessor {

    private static final String INITIAL_STATE_MESSAGE = "Установлено начальное состояние";

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(INITIAL_STATE_MESSAGE)
//                .parseMode("MarkdownV2")
                .build();
    }

    @Override
    public State getState() {
        return State.INITIAL_STATE;
    }
}
