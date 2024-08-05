package ru.appointme.model;

import jakarta.persistence.*;
import lombok.Data;

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
