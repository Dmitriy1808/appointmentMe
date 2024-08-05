package ru.appointme.bot.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NavigationAction {
    BACK("Назад");

    private final String title;
}
