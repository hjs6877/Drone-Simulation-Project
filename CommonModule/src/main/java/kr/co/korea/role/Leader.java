package kr.co.korea.role;

import kr.co.korea.behavior.Flight;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Leader implements AerialVehicle {
    private String droneName;
    private DroneSetting setting;

    public Leader(String droneName, DroneSetting setting) {
        this.droneName = droneName;
        this.setting = setting;
    }

    /**
     * 장애 정보 없는 단순 비행 시작.
     *
     * @return
     */
    public FlightStatus fly() {
        Flight flight = new Flight(droneName, setting);
        FlightStatus status = flight.flyWithoutError();

        return status;
    }
}
