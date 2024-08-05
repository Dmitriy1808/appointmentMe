package ru.appointme.bot.state;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface CallbackProcessor extends StateProcessor {

    void processCallback(CallbackQuery callback);

    ReplyKeyboard getReplyMarkup();
}
