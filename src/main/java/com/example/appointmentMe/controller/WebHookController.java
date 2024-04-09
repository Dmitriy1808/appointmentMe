package com.example.appointmentMe.controller;

import com.example.appointmentMe.bot.AppointMeBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@RestController
public class WebHookController {

    private final AppointMeBot bot;

    @PostMapping(value = "/client")
    public BotApiMethod<?> onReceive(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
