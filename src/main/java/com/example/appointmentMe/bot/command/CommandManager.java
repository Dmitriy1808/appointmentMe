package com.example.appointmentMe.bot.command;

import com.example.appointmentMe.exception.UnknownCommandException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Optional;

@Component
public class CommandManager {

    private final Map<Command, CommandProcessor> commandProcessors;

    public CommandManager(Map<Command, CommandProcessor> commandProcessors) {
        this.commandProcessors = commandProcessors;
    }

    public CommandProcessor getCommandProcessorFromMessage(Message message) throws UnknownCommandException {
        String commandText = message.getText();
        if (!message.isCommand()) {
            throw new UnknownCommandException("Unknown command: " + commandText);
        }

        return switch (commandText) {
            case "/start" -> commandProcessors.get(Command.START);
            case "/help" -> commandProcessors.get(Command.HELP);
            case "/about" -> commandProcessors.get(Command.ABOUT);
            case "/getlastappointment" -> commandProcessors.get(Command.GET_LAST_APPOINTMENT);
            case "/getallappointments" -> commandProcessors.get(Command.GET_ALL_APPOINTMENTS);
            default -> throw new UnknownCommandException("Unknown command: " + commandText);
        };
    }
}
