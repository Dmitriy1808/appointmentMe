package com.example.appointmentMe.controller;

import com.example.appointmentMe.botapi.AppointMeBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController(value = "/api/v1/")
public class WebHookController {

    private final AppointMeBot bot;

    @Autowired
    public WebHookController(AppointMeBot bot) {
        this.bot = bot;
    }

    @PostMapping(value = "/")
    public BotApiMethod<?> onReceive(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
