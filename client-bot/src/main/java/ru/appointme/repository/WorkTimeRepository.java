package ru.appointme.repository;

import ru.appointme.model.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {

    List<WorkTime> findByIsFreeTrueAndStartWorkTimeBetween(Date startDate, Date endDate);

}
