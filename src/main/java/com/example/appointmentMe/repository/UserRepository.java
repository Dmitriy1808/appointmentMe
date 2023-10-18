package com.example.appointmentMe.repository;

import com.example.appointmentMe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramNick(String nick);

    @Query("select u.city from User u where u.telegramNick = :nick")
    Optional<String> findCityByTelegramNick(@Param("nick") String nick);

//    @Query("select u.appointment")
//    Optional<Date> findLastAppointment(@Param("nick") String nick);

}
