package ru.appointme.bot.state;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageProcessor extends StateProcessor {

    void processMessage(Message message);

}
