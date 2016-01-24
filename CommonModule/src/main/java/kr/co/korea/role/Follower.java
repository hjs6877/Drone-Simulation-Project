package kr.co.korea.role;

import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Follower {
    private String droneName;
    private DroneSetting setting;

    public Follower(String droneName, DroneSetting setting){
        this.droneName = droneName;
        this.setting = setting;
    }

    public FlightStatus doFollowerProcess() {
        FlightStatus status = new FlightStatus();
        return status;
    }
}
