package ru.appointme.bot.state.impl.newuser;

import ru.appointme.bot.Utils;
import ru.appointme.bot.state.MessageProcessor;
import ru.appointme.bot.state.State;
import ru.appointme.model.User;
import ru.appointme.service.UserService;
import ru.appointme.service.appointment.cache.AppointmentCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
@Slf4j
public class FillNameState implements MessageProcessor {

    private static final String NAME_REQUEST_TEXT = "Введите Ваше имя";

    private final UserService userService;
    private final AppointmentCache cache;

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
        User user = userService.getUserByNickname(clientNickname);
        if (!userService.userExists(user)) {
            user.setName(client.getFirstName());
            user.setRepresentationName(clientName);
            user.setTelegramNick(clientNickname);
            user.setTelegramId(client.getId());
        }

        cache.getAppointmentDraftByNickname(clientNickname)
                .getAppointment()
                .setUser(user);
    }

    @Override
    public BotApiMethod<?> process(Update update) {
        return SendMessage.builder()
                .chatId(Utils.getChatId(update))
                .text(NAME_REQUEST_TEXT)
                .build();
    }

}