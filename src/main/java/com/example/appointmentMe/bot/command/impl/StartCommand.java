package com.example.appointmentMe.bot.command.impl;

import com.example.appointmentMe.bot.Utils;
import com.example.appointmentMe.bot.command.Command;
import com.example.appointmentMe.bot.command.CommandProcessor;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.bot.state.StateProcessor;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.service.UserService;
import com.example.appointmentMe.service.appointment.cache.AppointmentCache;
import com.example.appointmentMe.service.appointment.cache.AppointmentInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class StartCommand implements CommandProcessor {

    private final Map<State, StateProcessor> stateProcessors;
    private final AppointmentCache cache;
    private final UserService userService;

    @Override
    public BotApiMethod<?> processCommand(Update update) {
        AppointmentInfo draft = cache.getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update));
        log.debug("Change state from {} to {}", draft.getState(), State.FILL_NAME);
        State startState = getActualState(draft.getAppointment().getUser());
        if (!startState.equals(draft.getState())) {
            draft.setState(startState);
        }

        return stateProcessors.get(startState).process(update);
    }

    private State getActualState(User user) {
        return userService.userExists(user)
                ? State.EXISTING_USER_DETECTED
                : State.FILL_NAME;
    }

    @Override
    public Command getName() {
        return Command.START;
    }
}
