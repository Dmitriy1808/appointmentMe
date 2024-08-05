package ru.appointme.state.processors;

import ru.appointme.Utils;
import ru.appointme.model.Appointment;
import ru.appointme.service.AppointmentService;
import ru.appointme.state.State;
import ru.appointme.state.StateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GetAllAppointmentsStateProcessor implements StateProcessor {

    private final AppointmentService appointmentService;

    @Override
    public BotApiMethod<?> process(Update update) {
        List<Appointment> allAppointments = appointmentService.getCurrentWeekAppointments();
        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder().chatId(Utils.getChatId(update));
        if (allAppointments.isEmpty()) {
            return sendMessageBuilder
                    .text(Utils.NO_APPOINTMENTS_REPLY_MESSAGE_TEMPLATE)
                    .build();
        }

        return sendMessageBuilder
                .text(Utils.formatAppointmentsForReplyMessage(allAppointments))
                .build();
    }

    @Override
    public State getState() {
        return State.GET_ALL_APPOINTMENTS;
    }

}
