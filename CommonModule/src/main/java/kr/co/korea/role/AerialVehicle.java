package kr.co.korea.role;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlightStatus;
import kr.co.korea.error.ErrorEventProvider;
import kr.co.korea.error.ErrorLevel;
import kr.co.korea.error.ErrorType;

import java.util.Map;

/**
 * Created by kjs on 2016-01-29.
 */
public abstract class AerialVehicle {

    public abstract FlightStatus fly();


}
