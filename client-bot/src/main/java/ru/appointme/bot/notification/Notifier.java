package ru.appointme.bot.notification;

import ru.appointme.model.Appointment;

public interface Notifier {

    void notify(Appointment appointment, NotifyAction action);

}
