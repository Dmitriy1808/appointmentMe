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
public class AppointmentsManageState implements CallbackProcessor {

    private static final String DECLINE = "DECLINE";

    private final UserService userService;
    private final AppointmentCache cache;
    private final AppointmentService appointmentService;

    @Override
    public void processCallback(CallbackQuery callback) {
        if (NavigationAction.BACK.name().equals(callback.getData())) {
            AppointmentInfo draftInfo = cache.getAppointmentDraftByNickname(callback.getFrom().getUserName());
            draftInfo.setPrevStateFor(State.EXISTING_USER_DETECTED);
        }
    }

    @Override
    public State getState() {
        return State.APPOINTMENT_MANAGE;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        User user = userService.getUserByNickname(Utils.getUsernameFromUpdate(update));
        List<Appointment> allAppointments = appointmentService.getCurrentWeekAppointmentsByUser(user);
        long chatId = Utils.getChatId(update);
        if (allAppointments.isEmpty()) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(Utils.NO_APPOINTMENTS_REPLY_MESSAGE_TEMPLATE)
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(Utils.formatAppointmentsForReplyMessage(allAppointments))
                .replyMarkup(getReplyMarkup())
                .build();
    }

    public ReplyKeyboard getReplyMarkup() {
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text(NavigationAction.BACK.getTitle())
                .callbackData(NavigationAction.BACK.name())
                .build();
        InlineKeyboardButton declineAppointmentButton = InlineKeyboardButton.builder()
                .text("Отменить запись")
                .callbackData(DECLINE)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(backButton))
                .keyboardRow(List.of(declineAppointmentButton))
                .build();
    }
}
