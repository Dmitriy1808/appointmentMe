package com.example.appointmentMe.botapi.state;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackProcessor {

    State getState();

    void processCallback(CallbackQuery callback);

}
