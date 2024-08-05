package ru.appointme.bot.command.impl;

import ru.appointme.bot.Utils;
import ru.appointme.bot.command.Command;
import ru.appointme.bot.command.CommandProcessor;
import ru.appointme.bot.state.State;
import ru.appointme.bot.state.StateProcessor;
import ru.appointme.model.User;
import ru.appointme.service.UserService;
import ru.appointme.service.appointment.cache.AppointmentCache;
import ru.appointme.service.appointment.cache.AppointmentInfo;
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
