package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.MessageProcessor;
import com.example.appointmentMe.botapi.state.State;
import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.service.AppointmentCache;
import com.example.appointmentMe.service.CityService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZoneOffset;
import java.util.Date;

@Component
public class ChoiceCityState implements MessageProcessor {
    private final AppointmentCache cache;
    private final CityService cityService;

    public ChoiceCityState(AppointmentCache cache, CityService cityService) {
        this.cache = cache;
        this.cityService = cityService;
    }

    @Override
    public State getState() {
        return State.CHOICE_OF_CITY;
    }

    @Override
    public void processMessage(Message message) {
        int timezoneOffset = (!message.hasText() || message.getText() == null) ?
                cityService.getDefaultTimezone() : cityService.getCityTimezone(message.getText());
        Appointment draft = cache.getDraft(message.getFrom().getUserName());
        Date appointmentDate = draft.getAppointmentDate();
        appointmentDate.setTime(Date.from(appointmentDate.toInstant()
                .atOffset(ZoneOffset.ofHours(timezoneOffset))
                .toInstant())
                .getTime());
        cache.saveDraft(draft);
    }

    @Override
    public State getNextState() {
        return State.FINAL_STATE;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Input your city")
                .build();
    }
}
