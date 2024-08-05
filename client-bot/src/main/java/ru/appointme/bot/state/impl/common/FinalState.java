package ru.appointme.bot.state.impl.common;

import ru.appointme.bot.Utils;
import ru.appointme.bot.notification.Notifier;
import ru.appointme.bot.notification.NotifyAction;
import ru.appointme.bot.state.State;
import ru.appointme.bot.state.StateProcessor;
import ru.appointme.model.Appointment;
import ru.appointme.service.appointment.cache.AppointmentCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class FinalState implements StateProcessor {

    private static final String MESSAGE_TEMPLATE = """
            Вы записаны на %s (%s)""";

    private final AppointmentCache cache;
    private final Notifier notifier;
    private Appointment finalAppointment;

    @Override
    public State getState() {
        return State.FINAL_STATE;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        finalAppointment = cache.saveDraft(cache.getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update)).getAppointment());
        notifier.notify(finalAppointment, NotifyAction.WORKER_NOTIFY);
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(getMessageWithAppointment())
                .build();
    }

    private String getMessageWithAppointment() {
        return MESSAGE_TEMPLATE.formatted(Utils.formatSingleAppointmentForReplyMessage(finalAppointment),
                finalAppointment.getUser().getCity());
    }

}
