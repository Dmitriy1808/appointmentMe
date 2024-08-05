package ru.appointme.repository;

import ru.appointme.model.Appointment;
import ru.appointme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findTopByUserOrderByCreateTimestampDesc(User user);
    List<Appointment> findByUserOrderByCreateTimestampDesc(User user);
    List<Appointment> findByUserAndAppointmentDateBetween(User user, Date start, Date end);
    List<Appointment> findByAppointmentDateBetween(Date start, Date end);
    void deleteByCreateTimestampBefore(Date date);
}
