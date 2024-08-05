package ru.appointme.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr")
@Getter
@Setter
@NoArgsConstructor
public class User {

    public static final String DEFAULT_CITY = "Москва";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "representation_name")
    private String representationName;

    @Column(name = "telegram_nick")
    private String telegramNick;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "notified")
    private boolean notified = false;

    @OneToMany(mappedBy = "user")
    private Set<Appointment> appointments = new HashSet<>();

    @Column(name = "time_zone_offset")
    private int timezoneOffset = 3;

    @Column(name = "city")
    private String city = DEFAULT_CITY;

    public User(String name, String telegramNick) {
        this.name = name;
        this.telegramNick = telegramNick;
    }

}
