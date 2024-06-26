package com.example.appointmentMe.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "work_time")
@Data
public class WorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_work_time")
    private Date startWorkTime;

    @Column(name = "end_work_time")
    private Date endWorkTime;

    @Column(name = "is_free")
    private boolean isFree;

}
