package com.example.appointmentMe.bot;

import com.example.appointmentMe.bot.command.CommandManager;
import com.example.appointmentMe.bot.command.CommandProcessor;
import com.example.appointmentMe.bot.factory.StateFactory;
import com.example.appointmentMe.bot.factory.StateProcessorFactory;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.exception.UnknownCommandException;
import com.example.appointmentMe.service.UserService;
import com.example.appointmentMe.service.appointment.cache.AppointmentCache;
import com.example.appointmentMe.service.appointment.cache.AppointmentInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
@Getter
@Setter
@Slf4j
public class BotStateManager {

    private final StateProcessorFactory stateProcessorFactory;
    private final AppointmentCache appointmentCache;
    private final CommandManager commandManager;
    private final UserService userService;

    public BotApiMethod<?> handleRequest(Update update) {
        AppointmentInfo appointmentInfo = appointmentCache.getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update));
        State userState = appointmentInfo.getState();
        boolean hasCallbackQuery = update.hasCallbackQuery();
        if (hasCallbackQuery) {
            stateProcessorFactory.getCallbackProcessorByState(userState).processCallback(update.getCallbackQuery());
            AppointmentInfo maybeUpdatedAppointmentInfo = appointmentCache
                    .getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update));
            State currentState = maybeUpdatedAppointmentInfo.getState();
            State nextState = StateFactory.getNextStateFor(currentState)
                    .orElse(getActualInitStateByCurrentState(currentState));
            maybeUpdatedAppointmentInfo.setState(nextState);
            return stateProcessorFactory.getStateProcessorByState(nextState).process(update);
        }

        Message message = update.getMessage();
        if (message == null) {
            log.info("WTF message from update {} is NULL!", update.getUpdateId());
            StateFactory.getNextStateFor(userState).ifPresent(appointmentInfo::setState);
            return stateProcessorFactory.getStateProcessorByState(appointmentInfo.getState()).process(update);
        }

        boolean isTextMessage = !message.isCommand() && message.hasText();
        if (isTextMessage) {
            stateProcessorFactory.getMessageProcessorByState(userState).processMessage(message);
            State nextState = StateFactory.getNextStateFor(userState).orElse(getActualInitStateByCurrentState(userState));
            appointmentInfo.setState(nextState);
            return stateProcessorFactory.getStateProcessorByState(nextState).process(update);
        }

        String commandText = message.getText();
        try {
            CommandProcessor commandProcessor = commandManager.getCommandProcessorFromMessage(message);
            return commandProcessor.processCommand(update);
        } catch (UnknownCommandException e) {
            log.error("Error while processing command {}: {}", commandText, e.getLocalizedMessage());
            return SendMessage.builder()
                    .chatId(Utils.getChatId(update))
                    .text("Command '" + commandText + "' was failed, try again later")
                    .build();
        }
    }

    private State getActualInitStateByCurrentState(State currentState) {
        return null;
    }
}
