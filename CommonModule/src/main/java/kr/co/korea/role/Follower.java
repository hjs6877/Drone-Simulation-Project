package kr.co.korea.role;

import kr.co.korea.behavior.Flight;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;
import kr.co.korea.error.ErrorType;

import java.util.Map;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Follower extends AerialVehicle {
    private String droneName;
    private Drone drone;
    private DroneSetting setting;
    private Map<Long, ErrorType> errorEventMap;

    public Follower(String droneName, Drone drone){
        this.droneName = droneName;
        this.drone = drone;
    }

    public FlightStatus fly() {
        Flight flight = new Flight(droneName, drone);
        FlightStatus status = flight.flyAsFollower();

        return status;
    }
}
