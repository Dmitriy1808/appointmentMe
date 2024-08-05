package ru.appointme.state.processors;

import ru.appointme.Utils;
import ru.appointme.service.AppointmentService;
import ru.appointme.state.CallbackProcessor;
import ru.appointme.state.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.appointme.Utils.DEFAULT_ZONE_ID;


@RequiredArgsConstructor
@Component
@Slf4j
public class DeclineAppointmentStateProcessor implements CallbackProcessor {
    private static final String DECLINE_APPOINTMENT_MESSAGE_TEMPLATE = "Выберите запись, которую хотите отменить:\n";
    private static final String DECLINE_APPOINTMENT_SUCCESSFUL_MESSAGE = "Выбрнная запись успешно отменена";

    private final AppointmentService appointmentService;

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(DECLINE_APPOINTMENT_MESSAGE_TEMPLATE)
                .replyMarkup(getReplyMarkup())
                .build();
    }

    private ReplyKeyboard getReplyMarkup() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        appointmentService.getCurrentWeekAppointments()
                .forEach(appointment -> {
                    String appointmentString = ZonedDateTime.ofInstant(appointment.getAppointmentDate().toInstant(), DEFAULT_ZONE_ID)
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    buttons.add(List.of(InlineKeyboardButton.builder()
                                    .text(appointmentString + " " + appointment.getUser().getName())
                                    .callbackData(String.valueOf(appointment.getId()))
                                    .build()
                            )
                    );
                });

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public State getState() {
        return State.DECLINE_APPOINTMENT;
    }

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        String appointmentId = callbackQuery.getData();
        log.info("Deleting appointment with id {}", appointmentId);
        appointmentService.declineAppointmentById(Long.parseLong(appointmentId));
        return SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text(DECLINE_APPOINTMENT_SUCCESSFUL_MESSAGE)
                .build();
    }
}
