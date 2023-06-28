package com.example.appointmentMe.service;

import com.example.appointmentMe.model.Appointment;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.repository.AppointmentRepository;
import com.example.appointmentMe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class AppointmentCache {
    private static final String DEFAULT_TIMEZONE = "Etc/UTC";

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private static final ConcurrentHashMap<String, Appointment> DRAFT_CACHE = new ConcurrentHashMap<>();

    public AppointmentCache(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public Appointment getDraft(String clientNickname) {
        Appointment draft = DRAFT_CACHE.get(clientNickname);
        if (draft == null) {
            draft = new Appointment();
            User user = userRepository.findByTelegramNick(clientNickname).orElse(new User());
            draft.setUser(user);
            draft.setDraft(true);
            draft.setCreateTimestamp(Date.from(Instant.now()));
            DRAFT_CACHE.put(clientNickname, draft);
        }

        return draft;
    }

    public void saveDraft(Appointment appointmentDraft) {
        appointmentRepository.save(appointmentDraft);
        clearOne(appointmentDraft.getUser().getTelegramNick());
    }

    private void clearOne(String clientNickname) {
        DRAFT_CACHE.remove(clientNickname);
    }
}
