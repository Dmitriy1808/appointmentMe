package com.example.appointmentMe.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "telegram_nick")
    private String telegramNick;

    @Column(name = "telegram_id")
    private Long telegramId;

    @OneToMany(mappedBy = "user")
    private Set<Appointment> appointments = new HashSet<>();

    public User() {}

    public User(String name, String telegramNick) {
        this.name = name;
        this.telegramNick = telegramNick;
    }

}
