package com.example.appointmentMe.utils;

import javassist.NotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
@Getter
public enum Month {
    JAN,
    FEB,
    MAR,
    APR,
    MAY,
    JUN,
    JUL,
    AUG,
    SEP,
    OCT,
    NOV,
    DEC;

    public static String getByOrdinal(int ordinal) {
        String result = null;
        try {
            result = Stream.of(Month.values())
                    .filter(month -> ordinal == month.ordinal())
                    .map(Month::name)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Month with ordinal " + ordinal + " doesn't exists"));
        } catch (NotFoundException e) {
            log.error(e.getLocalizedMessage());
        }

        return result;
    }
}
