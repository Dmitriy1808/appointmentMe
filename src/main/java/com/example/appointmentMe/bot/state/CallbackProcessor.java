package com.example.appointmentMe.bot.state;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackProcessor extends StateProcessor {

    void processCallback(CallbackQuery callback);

}
