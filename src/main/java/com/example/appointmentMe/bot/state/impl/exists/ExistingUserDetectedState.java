package com.example.appointmentMe.bot.state.impl.exists;

import com.example.appointmentMe.bot.Utils;
import com.example.appointmentMe.bot.factory.StateFactory;
import com.example.appointmentMe.bot.state.CallbackProcessor;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.service.UserService;
import com.example.appointmentMe.service.appointment.AppointmentCache;
import com.example.appointmentMe.service.appointment.AppointmentInfo;
import com.example.appointmentMe.service.appointment.AppointmentService;
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
public class ExistingUserDetectedState implements CallbackProcessor {

    private static final String GREETING_MESSAGE_TEMPLATE = """
            Здравствуйте, %s!""";

    private final UserService userService;
    private final AppointmentCache cache;

    @Override
    public void processCallback(CallbackQuery callback) {
        if (State.CHOICE_OF_DATE.name().equals(callback.getData())) {
            AppointmentInfo draftInfo = cache.getAppointmentDraftByNickname(callback.getFrom().getUserName());
            StateFactory.getPrevStateFor(State.CHOICE_OF_DATE).ifPresent(draftInfo::setState);
            log.info("Change state from {} to {}", getState().name(), State.CHOICE_OF_DATE.name());
        }
    }

    @Override
    public State getState() {
        return State.EXISTING_USER_DETECTED;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        User user = userService.getUserByNickname(Utils.getUsernameFromUpdate(update));
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(GREETING_MESSAGE_TEMPLATE.formatted(user.getRepresentationName()))
                .replyMarkup(getReplyMarkup())
                .build();
    }

    private ReplyKeyboard getReplyMarkup() {
        InlineKeyboardButton choiceDateButton = InlineKeyboardButton.builder()
                .text("Выбрать дату")
                .callbackData(State.CHOICE_OF_DATE.name())
                .build();
        InlineKeyboardButton getAllAppointmentsButton = InlineKeyboardButton.builder()
                .text("Посмотреть все записи")
                .callbackData(State.APPOINTMENT_MANAGE.name())
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(choiceDateButton))
                .keyboardRow(List.of(getAllAppointmentsButton))
                .build();
    }
}
