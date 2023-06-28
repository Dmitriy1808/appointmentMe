package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.CallbackProcessor;
import com.example.appointmentMe.botapi.state.State;
import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.service.AppointmentCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Component
@Slf4j
public class ChoiceDateState implements CallbackProcessor {
    private static final LocalDate LOCAL_DATE = LocalDate.now();
    private static final int DAYS_IN_WEEK = 7;
    private final AppointmentCache cache;

    public ChoiceDateState(AppointmentCache cache) { //  CORRECT STATE - choiceTime
        this.cache = cache;
    }
    @Override
    public State getState() {
        return State.CHOICE_OF_DATE;
    }

    @Override
    public State getNextState() {
        return State.CHOICE_OF_TIME;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .replyMarkup(getReplyMarkup())
                .text("Choose date for appointment? please")
                .build();
    }

    @Override
    public void processCallback(CallbackQuery callback) {
        Appointment draft = cache.getDraft(callback.getFrom().getUserName());
        String dateStr = callback.getData();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        draft.setAppointmentDate(parseAppointmentDate(dateStr, dateFormat));
        log.info("Callback accepting mock. Callback data: {}", callback.getData());
    }

    private Date parseAppointmentDate(String dateStr, SimpleDateFormat dateFormat) {
        Date result = null;
        try {
            result = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("Date parsing error: ", e);
        }

        return result;
    }

    private ReplyKeyboard getReplyMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(getWeekdayRow())
                .keyboardRow(getCalendarKeyboard())
                .build();
    }

    private List<InlineKeyboardButton> getCalendarKeyboard() {
        int firstWeekDay = getFirstWeekDay();
        List<InlineKeyboardButton> keyboard = new ArrayList<>();
        InlineKeyboardButton.InlineKeyboardButtonBuilder buttonBuilder = InlineKeyboardButton.builder();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            int currentDayValue = firstWeekDay + i;
            keyboard.add(
                    buttonBuilder
                            .text(String.valueOf(currentDayValue))
                            .callbackData(getCallbackData(currentDayValue))
                            .build()
            );
        }

        return keyboard;
    }

    private int getFirstWeekDay() {
        DayOfWeek currentWeekDay = LOCAL_DATE.getDayOfWeek();
        int currentMonthDay = LOCAL_DATE.getDayOfMonth();
        int currentWeekDayOrdinal = currentWeekDay.ordinal();
        if (currentWeekDayOrdinal > DayOfWeek.THURSDAY.ordinal()) {
            return currentMonthDay + DAYS_IN_WEEK - currentWeekDayOrdinal;
        }

        return DayOfWeek.MONDAY == currentWeekDay ?
                currentMonthDay :
                currentMonthDay - currentWeekDayOrdinal;
    }

    private String getCallbackData(int currentDay) {
        StringJoiner stringJoiner = new StringJoiner(".");
        return stringJoiner.add(String.valueOf(currentDay))
                .add(String.valueOf(LOCAL_DATE.getMonthValue()))
                .add(String.valueOf(LOCAL_DATE.getYear()))
                .toString();
    }

    private List<InlineKeyboardButton> getWeekdayRow() {
        List<InlineKeyboardButton> weekdayRow = new ArrayList<>();
        InlineKeyboardButton.InlineKeyboardButtonBuilder buttonBuilder = InlineKeyboardButton.builder();

        weekdayRow.add(buttonBuilder.text("Mon").callbackData("Mon").build());
        weekdayRow.add(buttonBuilder.text("Tue").callbackData("Tue").build());
        weekdayRow.add(buttonBuilder.text("Wen").callbackData("Wen").build());
        weekdayRow.add(buttonBuilder.text("Thu").callbackData("Thu").build());
        weekdayRow.add(buttonBuilder.text("Fri").callbackData("Fri").build());
        weekdayRow.add(buttonBuilder.text("Sat").callbackData("Sat").build());
        weekdayRow.add(buttonBuilder.text("Sun").callbackData("Sun").build());

        return weekdayRow;
    }

}

