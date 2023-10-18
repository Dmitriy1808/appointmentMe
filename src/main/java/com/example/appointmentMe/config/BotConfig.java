package com.example.appointmentMe.config;

import com.example.appointmentMe.bot.command.Command;
import com.example.appointmentMe.bot.command.CommandProcessor;
import com.example.appointmentMe.bot.state.CallbackProcessor;
import com.example.appointmentMe.bot.state.MessageProcessor;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.bot.state.StateProcessor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Configuration
@Getter
public class BotConfig {

    @Value("${telegram.bot.name}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public Map<State, StateProcessor> stateProcessors(List<StateProcessor> processors) {
        return processors.stream().collect(Collectors.toMap(StateProcessor::getState, Function.identity()));
    }

    @Bean
    public Map<State, CallbackProcessor> callbackProcessors(List<CallbackProcessor> processors) {
        return processors.stream().collect(Collectors.toMap(CallbackProcessor::getState, Function.identity()));
    }

    @Bean
    public Map<State, MessageProcessor> messageProcessors(List<MessageProcessor> processors) {
        return processors.stream().collect(Collectors.toMap(MessageProcessor::getState, Function.identity()));
    }

    @Bean
    public Map<Command, CommandProcessor> commandProcessors(List<CommandProcessor> processors) {
        return processors.stream().collect(Collectors.toMap(CommandProcessor::getName, Function.identity()));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
