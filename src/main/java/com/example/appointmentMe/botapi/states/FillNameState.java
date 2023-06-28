package com.example.appointmentMe.botapi.states;

import com.example.appointmentMe.botapi.state.BotState;
import com.example.appointmentMe.botapi.state.MessageProcessor;
import com.example.appointmentMe.botapi.state.State;
import com.example.appointmentMe.model.User;
import com.example.appointmentMe.repository.UserRepository;
import com.example.appointmentMe.service.AppointmentCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class FillNameState implements MessageProcessor {

    private static final String NAME_REQUEST_TEXT = "Введите свое имя";
    private final UserRepository userRepository;
    private final AppointmentCache cache;
    public FillNameState(UserRepository userRepository, AppointmentCache cache) {
        this.userRepository = userRepository;
        this.cache = cache;
    }

    @Override
    public State getState() {
        return State.FILL_NAME;
    }

    @Override
    public void processMessage(Message message) {
        org.telegram.telegrambots.meta.api.objects.User client = message.getFrom();
        String clientName;
        if (message.hasText() && StringUtils.isNotBlank(message.getText())) {
            clientName = message.getText();
        } else {
            clientName = client.getFirstName();
        }
        log.info("Client name: {}", clientName);
        String clientNickname = client.getUserName();
        User user = userRepository.findByTelegramNick(clientNickname).orElse(new User());
        if (!userRepository.exists(Example.of(user))) {
            user.setName(client.getFirstName());
            user.setTelegramNick(clientNickname);
            user.setTelegramId(client.getId());
            userRepository.save(user);
        }

        cache.getDraft(clientNickname);
    }

    @Override
    public State getNextState() {
        return State.CHOICE_OF_DATE;
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(NAME_REQUEST_TEXT)
                .build();
    }

}