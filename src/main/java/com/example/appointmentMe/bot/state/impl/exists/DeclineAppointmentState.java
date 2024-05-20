package com.example.appointmentMe.bot.state.impl.exists;

import com.example.appointmentMe.bot.Utils;
import com.example.appointmentMe.bot.state.CallbackProcessor;
import com.example.appointmentMe.bot.state.NavigationAction;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.service.UserService;
import com.example.appointmentMe.service.appointment.AppointmentService;
import com.example.appointmentMe.service.appointment.cache.AppointmentCache;
import com.example.appointmentMe.service.appointment.cache.AppointmentInfo;
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

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class DeclineAppointmentState implements CallbackProcessor {

    private static final String DECLINE_APPOINTMENT_MESSAGE_TEXT = "Записи, доступные для удаления";
    private static User USER_FROM_UPDATE;

    private final UserService userService;
    private final AppointmentCache cache;
    private final AppointmentService appointmentService;

    @Override
    public void processCallback(CallbackQuery callback) {
        AppointmentInfo draftInfo = cache.getAppointmentDraftByNickname(callback.getFrom().getUserName());
        if (NavigationAction.BACK.name().equals(callback.getData())) {
            draftInfo.setPrevStateFor(State.EXISTING_USER_DETECTED);
            return;
        }

        appointmentService.declineAppointmentById(Long.parseLong(callback.getData()));
        draftInfo.setPrevState();
    }

    @Override
    public State getState() {
        return State.APPOINTMENT_DECLINE;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        USER_FROM_UPDATE = userService.getUserByNickname(Utils.getUsernameFromUpdate(update));
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(DECLINE_APPOINTMENT_MESSAGE_TEXT)
                .replyMarkup(getReplyMarkup())
                .build();
    }

    @Override
    public ReplyKeyboard getReplyMarkup() {
        List<Appointment> declineCandidates = appointmentService.getCurrentWeekAppointmentsByUser(USER_FROM_UPDATE);
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder resultKeyboardBuilder = InlineKeyboardMarkup.builder();
        for (Appointment appointment : declineCandidates) {
            String appointmentContent = Utils.formatSingleAppointmentForReplyMessage(appointment);
            InlineKeyboardButton declineAppointmentButton = InlineKeyboardButton.builder()
                    .text(appointmentContent)
                    .callbackData(String.valueOf(appointment.getId()))
                    .build();
            resultKeyboardBuilder.keyboardRow(List.of(declineAppointmentButton));
        }

        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text(NavigationAction.BACK.getTitle())
                .callbackData(NavigationAction.BACK.name())
                .build();
        resultKeyboardBuilder.keyboardRow(List.of(backButton));

        return resultKeyboardBuilder.build();
    }
}
