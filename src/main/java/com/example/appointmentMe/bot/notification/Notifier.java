package com.example.appointmentMe.bot.notification;

import com.example.appointmentMe.model.Appointment;

public interface Notifier {

    void notify(Appointment appointment, NotifyAction action);

}
