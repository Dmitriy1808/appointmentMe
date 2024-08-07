package ru.appointme.bot.notification.impl;

import ru.appointme.bot.notification.Notifier;
import ru.appointme.bot.notification.NotifyAction;
import ru.appointme.bot.notification.UserParams;
import ru.appointme.model.Appointment;
import ru.appointme.model.User;
import ru.appointme.service.UserService;
import ru.appointme.service.appointment.AppointmentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
@Primary
public class DefaultNotifier implements Notifier {
    private static final String CLIENT_NOTIFICATION_TEMPLATE = """
            Напоминаем Вам про запись %s""";
    private static final String WORKER_NOTIFICATION_TEMPLATE = """
            WORKER_NOTIFICATION_TEMPLATE""";
    private static final int HOURS_IN_DAY = 24;

    @Value(value = "${notify.clients.enable:}")
    private boolean notifyingEnabled;
    @Value(value = "${telegram.bot.token:}")
    private String botToken;
    @Value(value = "${bot.owner.id:}")
    private long botOwnerId;
    private String botOwnerNotifyUrl = "https://api.telegram.org/bot%s/sendMessage";

    private final RestTemplate restTemplate;
    private final AppointmentService appointmentService;
    private final UserService userService;
    @PostConstruct
    public void init() {
        botOwnerNotifyUrl = botOwnerNotifyUrl.formatted(botToken);
    }

    @Scheduled(cron = "${notify.clients.cron}")
    public void notifyClients() {
        if (!notifyingEnabled) {
            log.info("Scheduled notifying disabled");
            return;
        }

        appointmentService.getAppointmentsForCurrentDay()
                .stream()
                .filter(appointment ->
                        TimeUnit.MILLISECONDS.toHours(Math.abs(appointment.getAppointmentDate().getTime() - new Date().getTime())) < HOURS_IN_DAY)
                .filter(appointment -> !appointment.getUser().isNotified())
                .forEach(appointment -> notify(appointment, NotifyAction.CLIENT_AND_WORKER_NOTIFY));
    }

    @Override
    public void notify(Appointment appointment, NotifyAction action) {
        String notificationText = getNotificationText(appointment, action);
        User user = appointment.getUser();
        List<Long> notifyingUserIds = NotifyAction.CLIENT_AND_WORKER_NOTIFY.equals(action)
                ? List.of(botOwnerId, user.getTelegramId())
                : List.of(botOwnerId);

        notifyingUserIds.forEach(notifyingUserId -> sendNotification(notifyingUserId, notificationText));
        user.setNotified(true);
    }

    //    TODO Написать шаблоны для разынх типов уведомлений
    private String getNotificationText(Appointment appointment, NotifyAction action) {
        return NotifyAction.CLIENT_AND_WORKER_NOTIFY.equals(action)
                ? CLIENT_NOTIFICATION_TEMPLATE.formatted(appointment.getAppointmentDate().toString())
                : WORKER_NOTIFICATION_TEMPLATE;
    }

//    TODO нужно проработать обработку ошибок, чтобы корректно проставлять признак уведомленности пользователя
    private void sendNotification(Long notifyingUserId, String notificationText) {
        ResponseEntity<String> response = restTemplate.exchange(botOwnerNotifyUrl,
                HttpMethod.POST,
                new HttpEntity<>(new UserParams(notifyingUserId, notificationText)),
                String.class);

        if (!response.hasBody()) {
            log.error("Notification sending failed. Empty response from Telegram");
        } else if (!HttpStatus.OK.equals(response.getStatusCode())) {
            log.error("Notification sending failed. Response from Telegram: {}", response.getStatusCodeValue());
        }
    }
}
