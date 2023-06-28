package com.example.appointmentMe.botapi.state;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageProcessor extends BotState {

    void processMessage(Message message);

}
