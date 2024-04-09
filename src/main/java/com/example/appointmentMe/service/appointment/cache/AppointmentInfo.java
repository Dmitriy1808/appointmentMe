package com.example.appointmentMe.service.appointment.cache;

import com.example.appointmentMe.bot.factory.StateFactory;
import com.example.appointmentMe.bot.state.State;
import com.example.appointmentMe.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AppointmentInfo {

    private Appointment appointment;
    private State state;

    public void setNextState() {
        StateFactory.getNextStateFor(getState()).ifPresent(this::setState);
    }

}
