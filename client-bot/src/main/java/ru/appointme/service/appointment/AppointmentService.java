package ru.appointme.service.appointment;

import ru.appointme.bot.Utils;
import ru.appointme.model.Appointment;
import ru.appointme.model.User;
import ru.appointme.repository.AppointmentRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void declineAppointmentById(long id) {
        appointmentRepository.deleteById(id);
    }

    public Optional<Appointment> getLastAppointmentByUser(User user) {
        return appointmentRepository.findTopByUserOrderByCreateTimestampDesc(user);
    }

    public List<Appointment> getAllAppointmentByUser(User user) {
        return appointmentRepository.findByUserOrderByCreateTimestampDesc(user);
    }

    @Transactional
    public void deleteOldAppointments(Date expireDate) {
        appointmentRepository.deleteByCreateTimestampBefore(expireDate);
    }

    public List<Appointment> getCurrentWeekAppointmentsByUser(User user) {
//        TODO учесть перенос недели, если пользователь хочет записаться в воскресенье
        LocalDate now = LocalDate.now();
        Date start = Date.from(now.atStartOfDay()
                .toInstant(ZoneOffset.UTC));
        Date end = Date.from(LocalDate.ofInstant(DateUtils.addDays(start,
                                DayOfWeek.values().length - now.getDayOfWeek().getValue()).toInstant(),
                        Utils.DEFAULT_ZONE_ID).atTime(LocalTime.MAX)
                .toInstant(ZoneOffset.UTC));
        return appointmentRepository.findByUserAndAppointmentDateBetween(user, start, end);
    }

    public List<Appointment> getAppointmentsForCurrentDay() {
        Date start = Date.from(LocalDate.now().atStartOfDay()
                .toInstant(ZoneOffset.UTC));
        return appointmentRepository.findByAppointmentDateBetween(start, DateUtils.addDays(start, 1));
    }
}
