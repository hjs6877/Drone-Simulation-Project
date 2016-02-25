package kr.co.korea.domain;

import kr.co.korea.error.ErrorType;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by kjs on 2016-01-15.
 */
public class Drone implements Serializable {
    private String name;
    private String leaderOrFollower;
    private DroneSetting droneSetting;
    private TreeMap<Long, ErrorType> errorEvent;
    private FlyingInfo flyingInfo;

    public Drone(String name, DroneSetting droneSetting, FlyingInfo flyingInfo) {
        this.name = name;
        this.droneSetting = droneSetting;
        this.flyingInfo = flyingInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaderOrFollower() {
        return leaderOrFollower;
    }

    public void setLeaderOrFollower(String leaderOrFollower) {
        this.leaderOrFollower = leaderOrFollower;
    }

    public DroneSetting getDroneSetting() {
        return droneSetting;
    }

    public void setDroneSetting(DroneSetting droneSetting) {
        this.droneSetting = droneSetting;
    }

    public FlyingInfo getFlyingInfo() {
        return flyingInfo;
    }

    public void setFlyingInfo(FlyingInfo flyingInfo) {
        this.flyingInfo = flyingInfo;
    }

    public TreeMap<Long, ErrorType>  getErrorEvent() {
        return errorEvent;
    }

    public void setErrorEvent(TreeMap<Long, ErrorType>  errorEvent) {
        this.errorEvent = errorEvent;
    }
}
