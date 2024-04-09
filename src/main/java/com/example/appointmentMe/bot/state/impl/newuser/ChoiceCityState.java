package com.example.appointmentMe.bot.state.impl.newuser;

import com.example.appointmentMe.bot.Utils;
import com.example.appointmentMe.bot.state.MessageProcessor;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.service.appointment.cache.AppointmentCache;
import com.example.appointmentMe.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class ChoiceCityState implements MessageProcessor {

    private static final String CHOICE_CITY_TEXT_TEMPLATE = "Введите Ваш город";
    private final AppointmentCache cache;
    private final CityService cityService;

    @Override
    public State getState() {
        return State.CHOICE_OF_CITY;
    }

    @Override
    public void processMessage(Message message) {
        int timezoneOffset = (!message.hasText() || message.getText() == null)
                ? cityService.getDefaultTimezoneOffset()
                : cityService.getCityTimezoneOffset(message.getText());

        Appointment draft = cache.getAppointmentDraftByNickname(message.getFrom().getUserName()).getAppointment();
        User user = draft.getUser();
        user.setTimezoneOffset(timezoneOffset);
        user.setCity(cityService.getCityByTimezoneOffsetAndDirtyCityName(timezoneOffset, message.getText()));
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(CHOICE_CITY_TEXT_TEMPLATE)
                .build();
    }

}
