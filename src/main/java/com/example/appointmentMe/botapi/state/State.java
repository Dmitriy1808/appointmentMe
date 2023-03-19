package com.example.appointmentMe.botapi.state;

import lombok.Getter;

@Getter
public enum State {
    MAIN_MENU,
    CHOICE_OF_DATE,
    CHOICE_OF_CITY,
    CHOICE_OF_TIME,
    FILL_NAME
}
