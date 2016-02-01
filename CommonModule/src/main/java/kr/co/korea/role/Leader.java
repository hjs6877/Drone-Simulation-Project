package kr.co.korea.role;

import kr.co.korea.behavior.Flight;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Leader implements AerialVehicle {
    private String droneName;
    private Drone drone;
    private DroneSetting setting;

    public Leader(String droneName, Drone drone) {
        this.droneName = droneName;
        this.drone = drone;
    }

    /**
     * 장애 정보 없는 단순 비행 시작.
     *
     * @return
     */
    public FlightStatus fly() {
        Flight flight = new Flight(droneName, drone);
        FlightStatus status = flight.flyWithoutError();

        return status;
    }
}
