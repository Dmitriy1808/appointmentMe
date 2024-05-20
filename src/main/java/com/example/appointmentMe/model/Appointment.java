package com.example.appointmentMe.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appointment")
@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "create_timestamp")
    private Date createTimestamp;

}
