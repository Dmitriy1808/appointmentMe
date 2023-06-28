package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.CallbackProcessor;
import com.example.appointmentMe.botapi.state.State;
import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.model.WorkTime;
import com.example.appointmentMe.service.AppointmentCache;
import com.example.appointmentMe.service.WorkTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ChoiceTimeState implements CallbackProcessor {
    private static final String CHOOSE_TIME_MESSAGE = "Choose appointment time";
    private static final SimpleDateFormat TIME_PATTERN = new SimpleDateFormat("HH:mm");
    public static final int MILLIS_IN_HOUR = 3600000;
    private final AppointmentCache cache;
    private final WorkTimeService workTimeService;
    private State nextState = State.CHOICE_OF_CITY;
    public ChoiceTimeState(AppointmentCache cache, WorkTimeService workTimeService) {
        this.cache = cache;
        this.workTimeService = workTimeService;
    }

    @Override
    public State getState() {
        return State.CHOICE_OF_TIME;
    }

    @Override
    public void processCallback(CallbackQuery callback) {
        Appointment draft = cache.getDraft(callback.getFrom().getUserName());
        Date additionalDate = Date.from(draft.getAppointmentDate().toInstant()
                .plusNanos(Instant.parse(callback.getData()).getNano()));
        draft.setAppointmentDate(additionalDate);
        log.info("Time choice successfully. Appointment draft - {}", draft);
    }

    @Override
    public State getNextState() {
        return nextState;
    }

    private void setNextState(State nextState) {
        this.nextState = nextState;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return testSendingInternal(update);
    }

    private BotApiMethod<?> testSendingInternal(Update update) {
        BotApiMethod<?> sendMessageMethod = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(CHOOSE_TIME_MESSAGE)
                .build();
        return SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .replyMarkup(getReplyMarkup())
                .text(CHOOSE_TIME_MESSAGE)
                .build();
    }

    private ReplyKeyboard getReplyMarkup() {
        ReplyKeyboard keyboard = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> markup = getTimeKeyboard();
        if (!markup.isEmpty()) {
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(getTimeKeyboard())
                .build();
    }

    private List<List<InlineKeyboardButton>> getTimeKeyboard() {
        List<WorkTime> workTimes = workTimeService.getAllWorkTime();
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        if (workTimes.isEmpty()) {
            setNextState(State.NO_FREE_WORK_TIME);
            return markup;
        }
        int rowsCount = workTimes.size() / 2;
        int colsCount = 2;
        for (int i = 0; i < rowsCount; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < colsCount; j++) {
                WorkTime workTime = workTimes.get(j);
                String time = workTime.getStartWorkTime() + "-" + workTime.getEndWorkTime();
                row.add(
                        InlineKeyboardButton.builder()
                                .text(time)
                                .callbackData(workTime.getStartWorkTime().toString())
                                .build()
                );
            }
            markup.add(row);
        }

        return markup;
    }

}
