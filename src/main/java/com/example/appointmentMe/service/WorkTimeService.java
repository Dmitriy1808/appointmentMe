package com.example.appointmentMe.service;

import com.example.appointmentMe.model.WorkTime;
import com.example.appointmentMe.repository.WorkTimeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkTimeService {

    private final WorkTimeRepository workTimeRepository;

    public WorkTimeService(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    public List<WorkTime> getAllWorkTime() {
        List<WorkTime> result = new ArrayList<>();
        workTimeRepository.findAll()
                .iterator()
                .forEachRemaining(result::add);
        return result;
    }
}
