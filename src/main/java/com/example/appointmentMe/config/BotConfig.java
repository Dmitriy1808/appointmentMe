package com.example.appointmentMe.config;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.State;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class BotConfig {

    @Value("${telegram.bot.name}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String token;

//    @Bean
//    public Map<State, BotState> allStates() {
//        ApplicationContext ctx = new AnnotationConfigApplicationContext();
//
//        Map<State, BotState> beansMap = new HashMap<>();
//        beansMap.put(State.MAIN_MENU, (BotState) ctx.getBean(State.CHOICE_OF_CITY.name()));
//        beansMap.put(State.CHOICE_OF_CITY, (BotState) ctx.getBean(State.CHOICE_OF_DATE.name()));
//        beansMap.put(State.CHOICE_OF_DATE, (BotState) ctx.getBean(State.CHOICE_OF_TIME.name()));
//        beansMap.put(State.CHOICE_OF_TIME, (BotState) ctx.getBean(State.FILL_NAME.name()));
//        beansMap.put(State.FILL_NAME, (BotState) ctx.getBean(State.MAIN_MENU.name()));
//
//        return beansMap;
//    }
}
