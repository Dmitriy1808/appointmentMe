package com.example.appointmentMe.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appointment")
@Data
@ToString
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "appointment_date")
    private Date appointmentDate;
    @Column(name = "draft")
    private boolean isDraft;
    @Column(name = "create_timestamp")
    private Date createTimestamp;

}
