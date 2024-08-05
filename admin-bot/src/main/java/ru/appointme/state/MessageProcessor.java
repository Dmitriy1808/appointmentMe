package ru.appointme.state;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageProcessor extends StateProcessor {

    BotApiMethod<?> processMessage(Message message);

}
