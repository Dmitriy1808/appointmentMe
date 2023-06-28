package com.example.appointmentMe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cities")
@Getter
@RequiredArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "city")
    private final String city;

    @Column(name = "timezone_offset")
    private final int timezoneOffset;

}
