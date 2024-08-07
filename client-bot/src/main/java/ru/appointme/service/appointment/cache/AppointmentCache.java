package ru.appointme.service.appointment.cache;

import ru.appointme.bot.state.State;
import ru.appointme.model.Appointment;
import ru.appointme.model.User;
import ru.appointme.repository.AppointmentRepository;
import ru.appointme.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class AppointmentCache {
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private static final ConcurrentHashMap<String, AppointmentInfo> DRAFT_CACHE = new ConcurrentHashMap<>();

    public AppointmentInfo getAppointmentDraftByNickname(String clientNickname) {
        AppointmentInfo draft = DRAFT_CACHE.get(clientNickname);
        if (draft == null) {
            Appointment appointmentDraft = new Appointment();
            User user = userService.getUserByNickname(clientNickname);
            appointmentDraft.setUser(user);
            appointmentDraft.setCreateTimestamp(Date.from(Instant.now()));
            State actualState = getActualStateForUser(user);
            draft = new AppointmentInfo(appointmentDraft, actualState);
            DRAFT_CACHE.put(clientNickname, draft);
        }

        return draft;
    }

    private State getActualStateForUser(User user) {
        return userService.userExists(user)
                ? State.EXISTING_USER_DETECTED
                : State.FILL_NAME;
    }

    public boolean appointmentDraftExists(String clientNickname) {
        return DRAFT_CACHE.containsKey(clientNickname);
    }

    @Transactional
    public Appointment saveDraft(Appointment appointmentDraft) {
        User user = appointmentDraft.getUser();
        clearOne(user.getTelegramNick());
        userService.save(user);
        return appointmentRepository.save(appointmentDraft);
    }

    private void clearOne(String clientNickname) {
        DRAFT_CACHE.remove(clientNickname);
    }

    public void clearAll() {
        DRAFT_CACHE.clear();
    }

    public long getSize() {
        return DRAFT_CACHE.size();
    }
}
