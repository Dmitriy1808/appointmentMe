package com.example.appointmentMe.bot;

import com.example.appointmentMe.model.Appointment;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class Utils {
    public final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");
    public final int DEFAULT_TIMEZONE_OFFSET = 7;
    public final String NO_APPOINTMENTS_REPLY_MESSAGE_TEMPLATE = "Запаиси отсутствуют";
    public static final String FULL_DATE_PATTERN = "dd.MM.yyyy HH:mm";
    public static final String TIME_WITHOUT_DATE_PATTERN = "HH:mm";

    public long getUserTelegramIdFromUpdate(Update update) {
        return update.getMessage() == null
                ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
    }

    public long getChatId(Update update) {
        if (update.getEditedMessage() != null) {
            return update.getEditedMessage().getChatId();
        }

        return update.getMessage() == null
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
    }

    public String getUsernameFromUpdate(Update update) {
        if (update.getEditedMessage() != null) {
            return update.getEditedMessage().getFrom().getUserName();
        }

        return update.getMessage() == null
                ? update.getCallbackQuery().getFrom().getUserName()
                : update.getMessage().getFrom().getUserName();
    }

    public String formatSingleAppointmentForReplyMessage(Appointment appointment) {
        return formatAppointmentsForReplyMessage(List.of(appointment));
    }

    public String formatAppointmentsForReplyMessage(List<Appointment> appointments) {
        StringBuilder appointmentsForReplyMessage = new StringBuilder();
        appointments.stream()
                .map(appointment -> ZonedDateTime.ofInstant(appointment.getAppointmentDate().toInstant(), DEFAULT_ZONE_ID)
                        .minusHours(DEFAULT_TIMEZONE_OFFSET)
                        .plusHours(appointment.getUser().getTimezoneOffset())
                        .format(DateTimeFormatter.ofPattern(FULL_DATE_PATTERN)))
                .forEach(appointment -> appointmentsForReplyMessage.append(appointment).append("\n"));

        return appointmentsForReplyMessage.toString();
    }

}
