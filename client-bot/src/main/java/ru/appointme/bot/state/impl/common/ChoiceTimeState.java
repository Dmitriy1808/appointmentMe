package ru.appointme.bot.state.impl.common;

import ru.appointme.bot.Utils;
import ru.appointme.bot.state.CallbackProcessor;
import ru.appointme.bot.state.State;
import ru.appointme.model.Appointment;
import ru.appointme.model.WorkTime;
import ru.appointme.service.WorkTimeService;
import ru.appointme.service.appointment.cache.AppointmentCache;
import ru.appointme.service.appointment.cache.AppointmentInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChoiceTimeState implements CallbackProcessor {
    private static final String CHOOSE_TIME_MESSAGE = "Выберите время";
    private static final String NO_FREE_TIME_MESSAGE = "На эту дату время приема отсутствует";
    private static final String WORK_TIME_RANGE_TEMPLATE = "%s - %s (%s)";
    private static final String WORK_TIME_TEMPLATE = "%s:%s";
    private static boolean NO_FREE_TIME = false;
    private static final Calendar CALENDAR = Calendar.getInstance();

//    TODO это костыль, нужен дополнительный компонент, который будет хранить в себе доп парметры
    private static List<WorkTime> WORK_TIME_LIST;
    private static int CLIENT_TIMEZONE_OFFSET;
    private static String CLIENT_CITY;

    private final AppointmentCache cache;
    private final WorkTimeService workTimeService;
    @Value("${bot.owner.url:}")
    private String botOwnerUrl;

    @Override
    public State getState() {
        return State.CHOICE_OF_TIME;
    }

    @Override
    public void processCallback(CallbackQuery callback) {
        AppointmentInfo draftInfo = cache.getAppointmentDraftByNickname(callback.getFrom().getUserName());
        if (NO_FREE_TIME) {
            draftInfo.setPrevStateFor(State.CHOICE_OF_DATE);
            log.info("No free time, change state from {} to {}", getState().name(), State.CHOICE_OF_DATE.name());
            return;
        }

//        TODO помечать время как занятое
        Appointment draft = draftInfo.getAppointment();
        Date additionalDate = getPreparedAdditionalDateFromTelegramUser(callback);
        draft.setAppointmentDate(additionalDate);
        log.info("Time choice successfully. Appointment draft - {}", draft);
    }

    private Date getPreparedAdditionalDateFromTelegramUser(CallbackQuery callback) {
        Date additionalDate = null;
        try {
            additionalDate = DateUtils.parseDate(callback.getData(), "yyyy-MM-dd HH:mm:ss.S");
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage());
        }

        return additionalDate;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        Appointment draft = cache.getAppointmentDraftByNickname(Utils.getUsernameFromUpdate(update)).getAppointment();
        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder().chatId(Utils.getChatId(update));
        WORK_TIME_LIST = workTimeService.getFreeWorkTimeByStartDate(draft.getAppointmentDate());
        CLIENT_TIMEZONE_OFFSET = draft.getUser().getTimezoneOffset();
        CLIENT_CITY = draft.getUser().getCity();
        NO_FREE_TIME = checkDayOffset(draft, WORK_TIME_LIST);
        if (NO_FREE_TIME) {
            return sendMessageBuilder
                    .replyMarkup(getNoFreeTimeReplyMarkup())
                    .text(NO_FREE_TIME_MESSAGE)
                    .build();
        }

        return sendMessageBuilder
                .replyMarkup(getReplyMarkup())
                .text(CHOOSE_TIME_MESSAGE)
                .build();
    }

    private boolean checkDayOffset(Appointment draft, List<WorkTime> workTime) {
        return workTime.stream()
                .map(time -> ZonedDateTime.ofInstant(time.getStartWorkTime().toInstant(), Utils.DEFAULT_ZONE_ID)
                        .minusHours(Utils.DEFAULT_TIMEZONE_OFFSET)
                        .plusHours(draft.getUser().getTimezoneOffset()))
                .filter(time -> time.getDayOfMonth() == LocalDate.ofInstant(
                        draft.getAppointmentDate().toInstant(), Utils.DEFAULT_ZONE_ID).getDayOfMonth())
                .filter(time -> time.getDayOfMonth() > LocalDate.ofInstant(
                        Instant.now(), Utils.DEFAULT_ZONE_ID).getDayOfMonth())
                .map(time -> time.format(DateTimeFormatter.ofPattern(Utils.TIME_WITHOUT_DATE_PATTERN)))
                .collect(Collectors.toSet())
                .isEmpty();
    }

    private ReplyKeyboard getNoFreeTimeReplyMarkup() {
        InlineKeyboardButton chatWithWorkerButton = InlineKeyboardButton.builder()
                .text("Написать специалисту")
                .url(botOwnerUrl)
                .build();
        InlineKeyboardButton choiceDateButton = InlineKeyboardButton.builder()
                .text("Выбрать дату")
                .callbackData("some data")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(chatWithWorkerButton, choiceDateButton))
                .build();
    }

//    TODO В ночное время формат времени 3:0 - 4:0
    @Override
    public ReplyKeyboard getReplyMarkup() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();
        WORK_TIME_LIST.forEach(time -> {
            int timezoneOffset = CLIENT_TIMEZONE_OFFSET;
            String timeRepresentation = WORK_TIME_RANGE_TEMPLATE.formatted(
                    getFormattedWorkTime(time.getStartWorkTime(), timezoneOffset),
                    getFormattedWorkTime(time.getEndWorkTime(), timezoneOffset),
                    CLIENT_CITY);
            keyboardBuilder.keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(timeRepresentation)
                    .callbackData(time.getStartWorkTime().toString())
                    .build()));
        });

        return keyboardBuilder.build();
    }

    private String getFormattedWorkTime(Date time, int timezoneOffset) {
        Date formattedDate = Date.from(Instant.parse(
                    ZonedDateTime.ofInstant(time.toInstant(), Utils.DEFAULT_ZONE_ID)
                            .minusHours(Utils.DEFAULT_TIMEZONE_OFFSET)
                            .plusHours(timezoneOffset)
                            .format(DateTimeFormatter.ISO_INSTANT)));
        CALENDAR.setTime(formattedDate);
        return WORK_TIME_TEMPLATE.formatted(getFormattedTimeUnit(CALENDAR.get(Calendar.HOUR_OF_DAY)),
                getFormattedTimeUnit(CALENDAR.get(Calendar.MINUTE)));
    }

    private String getFormattedTimeUnit(int timeUnit) {
        String timeUnitRepresentation = String.valueOf(timeUnit);
        return timeUnitRepresentation.length() == 1
                ? "0".concat(timeUnitRepresentation)
                : timeUnitRepresentation;
    }
}
