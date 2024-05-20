package com.example.appointmentMe.bot.state.impl.common;

import com.example.appointmentMe.bot.Utils;
import com.example.appointmentMe.bot.state.CallbackProcessor;
import com.example.appointmentMe.bot.state.State;
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

import java.time.*;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChoiceDateState implements CallbackProcessor {
    private static final LocalDate LOCAL_DATE = LocalDate.now();
    private static final int DAYS_IN_WEEK = DayOfWeek.values().length;
    private static final String CHOOSE_DATE_FOR_APPOINTMENT_TEXT_TEMPLATE = "Выберите, пожалуйста, дату, на которую хотите записаться";
    private final AppointmentCache cache;

    @Override
    public State getState() {
        return State.CHOICE_OF_DATE;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .replyMarkup(getReplyMarkup())
                .text(CHOOSE_DATE_FOR_APPOINTMENT_TEXT_TEMPLATE)
                .build();
    }

    @Override
    public void processCallback(CallbackQuery callback) {
        AppointmentInfo draftInfo = cache.getAppointmentDraftByNickname(callback.getFrom().getUserName());
        boolean isMissclickToWeekdayButton = Arrays.stream(DayOfWeek.values())
                .anyMatch(dayOfWeek -> callback.getData().equals(dayOfWeek.name()));
        if (isMissclickToWeekdayButton) {
            draftInfo.setPrevState();
            log.info("Missclick");
            return;
        }

        String dateStr = callback.getData();
        Date formattedDate = Date.from(LocalDateTime.parse(dateStr).toInstant(ZoneOffset.UTC));
        draftInfo.getAppointment().setAppointmentDate(formattedDate);
        log.info("Callback data: {}", callback.getData());
    }

    public ReplyKeyboard getReplyMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(getWeekdayRow())
                .keyboardRow(getCalendarKeyboard())
                .build();
    }

    private List<InlineKeyboardButton> getCalendarKeyboard() {
        List<InlineKeyboardButton> keyboard = new ArrayList<>();
        InlineKeyboardButton.InlineKeyboardButtonBuilder buttonBuilder = InlineKeyboardButton.builder();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            int currentDayValue = getCurrentDayValue(i);
            keyboard.add(
                    buttonBuilder
                            .text(String.valueOf(currentDayValue))
                            .callbackData(getCallbackData(currentDayValue))
                            .build()
            );
        }

        return keyboard;
    }

    private int getCurrentDayValue(int offset) {
        int firstWeekDay = getFirstWeekDay();
        int currentMonthMaxDay = LOCAL_DATE.getMonth().maxLength();
        int dayCount = firstWeekDay + offset;
        return dayCount > currentMonthMaxDay
                ? dayCount - currentMonthMaxDay
                : dayCount;
    }

    private int getFirstWeekDay() {
        DayOfWeek currentWeekDay = LOCAL_DATE.getDayOfWeek();
        int currentMonthDay = LOCAL_DATE.getDayOfMonth();
        int currentWeekDayOrdinal = currentWeekDay.ordinal();
        if (currentWeekDayOrdinal > DayOfWeek.THURSDAY.ordinal()) {
            return currentMonthDay + DAYS_IN_WEEK - currentWeekDayOrdinal;
        }

        int previousMonthLength = Month.of(LOCAL_DATE.getMonthValue() - 1).maxLength();
        int nextMonthDay = currentMonthDay - currentWeekDayOrdinal;
        if (nextMonthDay < 1) {
            nextMonthDay = previousMonthLength - Math.abs(nextMonthDay);
        }

        return DayOfWeek.MONDAY == currentWeekDay
                ? currentMonthDay
                : nextMonthDay;
    }

    private String getCallbackData(int currentDay) {
        StringJoiner stringJoiner = new StringJoiner("-");
        String currentDayRepresentation = currentDay < 10 ? "0".concat(String.valueOf(currentDay)) : String.valueOf(currentDay);
        int monthValue = LOCAL_DATE.getMonthValue();
        String monthRepresentation = monthValue < 10 ? "0".concat(String.valueOf(monthValue)) : String.valueOf(monthValue);
        String datePart = stringJoiner.add(String.valueOf(LOCAL_DATE.getYear()))
                .add(monthRepresentation)
                .add(currentDayRepresentation)
                .toString();

        return datePart + "T" + LocalTime.of(0, 0, 0).toString();
    }

    private List<InlineKeyboardButton> getWeekdayRow() {
        List<InlineKeyboardButton> weekdayRow = new ArrayList<>();
        InlineKeyboardButton.InlineKeyboardButtonBuilder buttonBuilder = InlineKeyboardButton.builder();

        weekdayRow.add(buttonBuilder.text("Mon").callbackData(DayOfWeek.MONDAY.name()).build());
        weekdayRow.add(buttonBuilder.text("Tue").callbackData(DayOfWeek.TUESDAY.name()).build());
        weekdayRow.add(buttonBuilder.text("Wen").callbackData(DayOfWeek.WEDNESDAY.name()).build());
        weekdayRow.add(buttonBuilder.text("Thu").callbackData(DayOfWeek.THURSDAY.name()).build());
        weekdayRow.add(buttonBuilder.text("Fri").callbackData(DayOfWeek.FRIDAY.name()).build());
        weekdayRow.add(buttonBuilder.text("Sat").callbackData(DayOfWeek.SATURDAY.name()).build());
        weekdayRow.add(buttonBuilder.text("Sun").callbackData(DayOfWeek.SUNDAY.name()).build());

        return weekdayRow;
    }

}

