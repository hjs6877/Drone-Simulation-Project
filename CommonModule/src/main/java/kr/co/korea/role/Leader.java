package kr.co.korea.role;

import kr.co.korea.behavior.Flight;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;
import kr.co.korea.error.ErrorEventProvider;
import kr.co.korea.error.ErrorLevel;
import kr.co.korea.error.ErrorType;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Leader extends AerialVehicle {
    private String droneName;
    private Drone drone;
    private ObjectOutputStream objectOutputStream;

    public Leader(ObjectOutputStream objectOutputStream, String droneName, Drone drone) {
        this.objectOutputStream = objectOutputStream;
        this.droneName = droneName;
        this.drone = drone;
    }


    /**
     * 장애 정보 없는 단순 비행 시작.
     *
     * @return
     */
    public FlightStatus fly() {
        Flight flight = new Flight(objectOutputStream, droneName, drone);
        FlightStatus status = flight.flyAsLeader();

        return status;
    }

}
