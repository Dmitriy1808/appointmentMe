package ru.appointme.bot.state;

import lombok.Getter;

@Getter
public enum State {
    FILL_NAME,
    CHOICE_OF_CITY,
    EXISTING_USER_PIPELINE_INITIAL_STATE,
    EXISTING_USER_DETECTED,
    APPOINTMENT_MANAGE,
    APPOINTMENT_DECLINE,
    CHOICE_OF_DATE,
    CHOICE_OF_TIME,
    FINAL_STATE
    ;
}
