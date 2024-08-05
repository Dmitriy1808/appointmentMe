package ru.appointme.schedule.impl;

import ru.appointme.schedule.Cleaner;
import ru.appointme.service.appointment.cache.AppointmentCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentCacheCleaner implements Cleaner {

    private static final String ENABLE_CLEANER_PROPERTY_NAME = "cache.cleaner.enable";

    @Value(value = "${cache.cleaner.enable:}")
    private boolean cleanerEnable;

    private final AppointmentCache cache;

    public AppointmentCacheCleaner(AppointmentCache cache) {
        this.cache = cache;
    }

    @Scheduled(cron = "${cache.cleaner.cron:}")
    @Override
    public void clean() {
        if (!cleanerEnable) {
            log.info("{} turned off. Check property '{}'", this.getClass().getSimpleName(), ENABLE_CLEANER_PROPERTY_NAME);
            return;
        }

//        TODO этот метод доступен всем, можно сделать возможность запуска этого метода только из cleaner'а
        cache.clearAll();
    }
}
