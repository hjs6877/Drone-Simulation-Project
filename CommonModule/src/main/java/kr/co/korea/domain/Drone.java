package kr.co.korea.domain;

import java.io.Serializable;

/**
 * Created by kjs on 2016-01-15.
 */
public class Drone implements Serializable {
    private String name;
    private String leaderOrFollower;

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
}
