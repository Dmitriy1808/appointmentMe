package com.example.appointmentMe.bot;

import com.example.appointmentMe.bot.command.CommandManager;
import com.example.appointmentMe.bot.command.CommandProcessor;
import com.example.appointmentMe.bot.factory.StateFactory;
import com.example.appointmentMe.bot.factory.StateProcessorFactory;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.exception.UnknownCommandException;
import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.model.WorkTime;
import com.example.appointmentMe.repository.WorkTimeRepository;
import com.example.appointmentMe.service.UserService;
import com.example.appointmentMe.service.appointment.AppointmentCache;
import com.example.appointmentMe.service.appointment.AppointmentInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;

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
    @Autowired
    private WorkTimeRepository workTimeRepository;

    @PostConstruct
    public void init() throws ParseException {
//        WorkTime validWorkTime = new WorkTime();
//        Date startWorkTime = DateUtils.parseDate("2023-09-14 05:34:56", "yyyy-MM-dd HH:mm:ss");
//        Date endWorkTime = DateUtils.parseDate("2023-09-14 06:34:56", "yyyy-MM-dd HH:mm:ss");
//        validWorkTime.setStartWorkTime(startWorkTime);
//        validWorkTime.setEndWorkTime(endWorkTime);
//        validWorkTime.setFree(true);
//        workTimeRepository.save(validWorkTime);
    }

    public BotApiMethod<?> handleRequest(Update update) {
        AppointmentInfo appointmentInfo = appointmentCache.getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update));
        State userState = appointmentInfo.getState();
        boolean hasCallbackQuery = update.hasCallbackQuery();
        if (hasCallbackQuery) {
            stateProcessorFactory.getCallbackProcessorByState(userState).processCallback(update.getCallbackQuery());
            AppointmentInfo maybeUpdatedAppointmentInfo = appointmentCache.getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update));
            State currentState = maybeUpdatedAppointmentInfo.getState();
            State nextState = StateFactory.getNextStateFor(maybeUpdatedAppointmentInfo.getState())
                    .orElse(getActualInitStateByCurrentState(currentState));
            maybeUpdatedAppointmentInfo.setState(nextState);
            return stateProcessorFactory.getStateProcessorByState(nextState).process(update);
        }

        Message message = update.getMessage();
        if (message != null && !message.isCommand() && message.hasText()) {
            stateProcessorFactory.getMessageProcessorByState(userState).processMessage(update.getMessage());
            State nextState = StateFactory.getNextStateFor(userState).orElse(getActualInitStateByCurrentState(userState));
            appointmentInfo.setState(nextState);
            return stateProcessorFactory.getStateProcessorByState(nextState).process(update);
        }

        String commandText = message == null ? StringUtils.EMPTY : message.getText();
        if (message != null) {
            try {
                CommandProcessor commandProcessor = commandManager.getCommandProcessorFromMessage(message);
                return commandProcessor.processCommand(update);
            } catch (UnknownCommandException e) {
                log.error("Error while processing command {}: {}", commandText, e.getLocalizedMessage());
                return SendMessage.builder()
                        .chatId(Utils.getChatId(update))
                        .text("Command " + commandText + " was failed, try again later")
                        .build();
            }
        }

        StateFactory.getNextStateFor(userState).ifPresent(appointmentInfo::setState);
        return stateProcessorFactory.getStateProcessorByState(appointmentInfo.getState()).process(update);
    }

    private AppointmentInfo getAppointmentInfo(Update update) {
        User userFromDatabase = userService.getUserByNickname(Utils.getUsernameFromUpdate(update));
        return userService.userExists(userFromDatabase)
                ? new AppointmentInfo(new Appointment(), State.EXISTING_USER_DETECTED)
                : appointmentCache.getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update));
    }

    private State getActualInitStateByCurrentState(State currentState) {
        return null;
    }
}
