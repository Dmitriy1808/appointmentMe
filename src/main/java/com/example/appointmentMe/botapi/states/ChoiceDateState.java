package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.CallbackProcessor;
import com.example.appointmentMe.botapi.state.State;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ChoiceDateState implements BotState, CallbackProcessor {

    private static final LocalDate LOCAL_DATE = LocalDate.now();
    public static final int DAYS_IN_CURRENT_MONTH = LOCAL_DATE.lengthOfMonth();
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
//        TODO: make return type BotApiMethod<?> with 'EditMessageReplyMarkup' method
        log.info("Callback accepting mock. Callback data: {}", callback.getData());
    }

    private ReplyKeyboard getReplyMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(getWeekdayRow())
                .keyboard(getCalendarKeyboard())
                .keyboardRow(getControlRow())
                .build();
    }

    private List<List<InlineKeyboardButton>> getCalendarKeyboard() {
        DayOfWeek firstMonthDay = LOCAL_DATE.withDayOfMonth(1).getDayOfWeek();
        int rowsCount = getRowsCount(firstMonthDay);
        int offset = firstMonthDay.ordinal();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (int i = 0; i < rowsCount; i++) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                InlineKeyboardButton.InlineKeyboardButtonBuilder buttonBuilder = InlineKeyboardButton.builder();
                int dayOfMonth = (j + 1 - offset) + (i * DAYS_IN_WEEK);
                String buttonData = String.valueOf(dayOfMonth);
                if (dayOfMonth <= 0 || dayOfMonth > DAYS_IN_CURRENT_MONTH) {
                    buttonData = " ";
                }
                buttonsRow.add(
                        buttonBuilder
                                .text(buttonData)
                                .callbackData(buttonData)
                                .build()
                );
            }
            keyboard.add(buttonsRow);
        }

        return keyboard;
    }

    private int getRowsCount(DayOfWeek firstMonthDay) {
        int rowsCount = DAYS_IN_CURRENT_MONTH % DAYS_IN_WEEK == 0 ?
                DAYS_IN_CURRENT_MONTH / DAYS_IN_WEEK : DAYS_IN_CURRENT_MONTH / DAYS_IN_WEEK + 1;

        if (!Month.FEBRUARY.equals(LOCAL_DATE.getMonth())) {
            return rowsCount;
        }

        int daysInFebruary = 28;
        if (!DayOfWeek.MONDAY.equals(firstMonthDay) && DAYS_IN_CURRENT_MONTH == daysInFebruary) {
            rowsCount++;
        }

        return rowsCount;
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

    private List<InlineKeyboardButton> getControlRow() {
        List<InlineKeyboardButton> controlRow = new ArrayList<>();
        InlineKeyboardButton.InlineKeyboardButtonBuilder buttonBuilder = InlineKeyboardButton.builder();

        controlRow.add(buttonBuilder.text("<<<").callbackData("getPrevMonth").build());
        controlRow.add(buttonBuilder.text(LOCAL_DATE.getMonth().name() + " " + LOCAL_DATE.getYear()).callbackData("getNextMonth").build());
        controlRow.add(buttonBuilder.text(">>>").callbackData("getNextMonth").build());

        return controlRow;
    }

}
