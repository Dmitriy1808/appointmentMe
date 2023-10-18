package com.example.appointmentMe.schedule.impl;

import com.example.appointmentMe.schedule.Cleaner;
import com.example.appointmentMe.service.appointment.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class AppointmentDbCleaner implements Cleaner {

    private static final String ENABLE_CLEANER_PROPERTY_NAME = "appointments.table.cleaner.enable";
    @Value(value = "${appointments.table.cleaner.enable:}")
    private boolean cleanerEnable;
    @Value(value = "${appointments.table.cleaner.expiration.days:}")
    private int expireDateInDays;

    private final AppointmentService appointmentService;

    public AppointmentDbCleaner(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Scheduled(cron = "${appointments.table.cleaner.cron:}")
    @Override
    public void clean() {
        if (!cleanerEnable) {
            log.debug("{} turned off. Check property '{}'", this.getClass().getSimpleName(), ENABLE_CLEANER_PROPERTY_NAME);
            return;
        }

        appointmentService.deleteOldAppointments(DateUtils.addDays(new Date(), Math.negateExact(expireDateInDays)));
    }
}
