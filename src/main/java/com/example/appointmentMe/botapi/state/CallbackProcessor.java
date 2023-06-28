package com.example.appointmentMe.botapi.state;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackProcessor extends BotState {

    void processCallback(CallbackQuery callback);

}
