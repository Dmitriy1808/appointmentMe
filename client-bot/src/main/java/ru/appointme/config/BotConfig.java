package ru.appointme.config;

import ru.appointme.bot.command.Command;
import ru.appointme.bot.command.CommandProcessor;
import ru.appointme.bot.state.CallbackProcessor;
import ru.appointme.bot.state.MessageProcessor;
import ru.appointme.bot.state.State;
import ru.appointme.bot.state.StateProcessor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

}
