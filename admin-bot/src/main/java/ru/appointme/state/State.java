package ru.appointme.state;

public enum State {
    INITIAL_STATE("/start"),
    GET_ALL_APPOINTMENTS("/appointments"),
    ADD_WORK_TIME("/addworktime"),
    DECLINE_APPOINTMENT("/declineappointment")
    ;

    private final String command;

    State(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}
