package com.example.appointmentMe;

import com.example.appointmentMe.repository.CityRepository;
import com.example.appointmentMe.service.CityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimeZoneTest {
    private static final int UTC2 = 1;
    private static final int UTC3 = 52;
    private static final int UTC4 = 6;
    private static final int UTC5 = 13;
    private static final int UTC6 = 1;
    private static final int UTC7 = 9;
    private static final int UTC8 = 4;
    private static final int UTC9 = 3;
    private static final int UTC10 = 3;
    private static final List<String> CITIES = List.of("Калининград", "Москва", "Санкт-Петербург", "Нижний Новгород", "Казань",
            "Ростов-на-Дону", "Воронеж", "Краснодар", "Саратов", "Ярославль", "Махачкала", "Рязань", "Набережные Челны", "Пенза", "Липецк",
            "Киров", "Чебоксары", "Тула", "Курск", "Ставрополь", "Сочи", "Тверь", "Иваново", "Брянск", "Белгород", "Владимир", "Архангельск",
            "Калуга", "Смоленск", "Череповец", "Саранск", "Орёл", "Вологда", "Владикавказ", "Грозный", "Мурманск", "Тамбов", "Петрозаводск",
            "Кострома", "Новороссийск", "Йошкар-Ола", "Таганрог", "Сыктывкар", "Нальчик", "Нижнекамск", "Шахты", "Дзержинск", "Старый Оскол",
            "Великий Новгород", "Псков", "Минеральные Воды", "Севастополь", "Симферопль", "Самара", "Волгоград", "Тольятти", "Ижевск", "Ульяновск",
            "Астрахань", "Екатеринбург", "Челябинск", "Уфа", "Пермь", "Тюмень", "Оренбург", "Магнитогорск", "Сургут", "Нижний Тагил", "Курган",
            "Стерлитамак", "Нижневартовск", "Орск", "Омск", "Новосибирск", "Красноярск", "Барнаул", "Томск", "Кемерово", "Новокузнецк", "Бийск",
            "Прокопьевск", "Норильск", "Иркутск", "Улан-Удэ", "Братск", "Ангарск", "Чита", "Якутск", "Благовещенск", "Хабаровск", "Владивосток",
            "Комсомольск-на-Амуре");
    private static final String SQL_TEMPLATE = "INSERT INTO cities VALUES (%d, '%s', '%s');";
    @Autowired
    private CityService cityService;
    private List<String> RESULT = new ArrayList<>();

    @Test
    public void test() {
        List<String> result = new ArrayList<>();
        int n = 0;
        int i = help(0, UTC2, "UTC+2");
        i = help(i, UTC3, "UTC+3");
        i = help(i, UTC4, "UTC+4");
        i = help(i, UTC5, "UTC+5");
        i = help(i, UTC6, "UTC+6");
        i = help(i, UTC7, "UTC+7");
        i = help(i, UTC8, "UTC+8");
        i = help(i, UTC9, "UTC+9");
        help(i, UTC10, "UTC+10");

        for (String sqlQuery : RESULT) {
            System.out.println(sqlQuery);
        }
        
    }

    private int help(int index, int utcLimit, String utc) {
        for (int i = index; i < utcLimit + index; i++) {
            RESULT.add(String.format(SQL_TEMPLATE, i + 1, CITIES.get(i), utc));
        }

        return utcLimit + index;
    }

    @Test
    public void test1() {
        String[] availableIDs = TimeZone.getAvailableIDs();

        System.out.println(cityService.getCityTimezone("Воронеж"));
//        for(String id : availableIDs) {
//            System.out.println("Timezone = " + id);
//        }
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        System.out.println("System UTC: " + Clock.systemUTC());
//        System.out.println("System default zone: " + Clock.systemDefaultZone());
//        System.out.println(Instant.now());
//        System.out.println(Date.from(Instant.now()));

//        ZoneOffset.getAvailableZoneIds().forEach(System.out::println);
//        System.out.println(ZoneOffset.ofHours(-18));
    }

}
