package kr.co.korea.role;

import kr.co.korea.behavior.Flight;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;
import kr.co.korea.error.ErrorType;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Follower extends AerialVehicle {
    private ObjectOutputStream objectOutputStream;
    private String droneName;
    private Drone drone;
    private DroneSetting setting;
    private Map<Long, ErrorType> errorEventMap;

    public Follower(ObjectOutputStream objectOutputStream, String droneName, Drone drone){
        this.objectOutputStream = objectOutputStream;
        this.droneName = droneName;
        this.drone = drone;
    }

    public FlightStatus fly() {
        Flight flight = new Flight(objectOutputStream, droneName, drone);
        FlightStatus status = flight.flyAsFollower();

        return status;
    }
}
