package ru.appointme.bot.command.impl;

import ru.appointme.bot.Utils;
import ru.appointme.bot.command.Command;
import ru.appointme.bot.command.CommandProcessor;
import ru.appointme.model.Appointment;
import ru.appointme.model.User;
import ru.appointme.service.UserService;
import ru.appointme.service.appointment.AppointmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class GetLastAppointmentCommand implements CommandProcessor {
    private static final String REPLY_MESSAGE_TEMPLATE = "Вы записаны на %s в %s";

    private final UserService userService;
    private final AppointmentService appointmentService;

    public GetLastAppointmentCommand(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @Override
    public BotApiMethod<?> processCommand(Update update) {
        User user = userService.getUserByNickname(Utils.getUsernameFromUpdate(update));
        Optional<Appointment> lastAppointmentOptional = appointmentService.getLastAppointmentByUser(user)
                .filter(appointment -> appointment.getAppointmentDate() != null);
        long chatId = Utils.getChatId(update);
        if (lastAppointmentOptional.isEmpty()) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(Utils.NO_APPOINTMENTS_REPLY_MESSAGE_TEMPLATE)
                    .build();
        }

        Appointment lastAppointment = lastAppointmentOptional.get();
        String[] formattedDateParts = Utils.formatSingleAppointmentForReplyMessage(lastAppointment)
                .split(StringUtils.SPACE);

        return SendMessage.builder()
                .chatId(chatId)
                .text(REPLY_MESSAGE_TEMPLATE.formatted(formattedDateParts[0], formattedDateParts[1]))
                .build();
    }

    @Override
    public Command getName() {
        return Command.GET_LAST_APPOINTMENT;
    }
}
