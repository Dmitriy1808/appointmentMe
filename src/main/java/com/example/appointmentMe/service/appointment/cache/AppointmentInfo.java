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
        this.setState(StateFactory.getNextStateFor(getState()));
    }

    public void setNextStateFor(State state) {
        this.setState(StateFactory.getNextStateFor(state));
    }

    public void setPrevState() {
        this.setState(StateFactory.getPrevStateFor(getState()));
    }

    public void setPrevStateFor(State state) {
        this.setState(StateFactory.getPrevStateFor(state));
    }

}
