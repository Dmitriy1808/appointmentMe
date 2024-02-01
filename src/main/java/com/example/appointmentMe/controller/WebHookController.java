package com.example.appointmentMe.controller;

import com.example.appointmentMe.bot.AppointMeBot;
import com.example.appointmentMe.bot.notification.Notifier;
import com.example.appointmentMe.bot.notification.NotifyAction;
import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.schedule.impl.AppointmentCacheCleaner;
import com.example.appointmentMe.service.appointment.AppointmentCache;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@RestController(value = "/api")
public class WebHookController {

    private final AppointMeBot bot;

    @PostMapping(value = "/client")
    public BotApiMethod<?> onReceive(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
