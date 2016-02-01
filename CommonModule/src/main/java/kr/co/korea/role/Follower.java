package kr.co.korea.role;

import kr.co.korea.behavior.Flight;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Follower implements AerialVehicle {
    private String droneName;
    private Drone drone;
    private DroneSetting setting;

    public Follower(String droneName, Drone drone){
        this.droneName = droneName;
        this.drone = drone;
    }

    public FlightStatus fly() {
        Flight flight = new Flight(droneName, drone);
        FlightStatus status = flight.flyWithoutError();

        return status;
    }
}
