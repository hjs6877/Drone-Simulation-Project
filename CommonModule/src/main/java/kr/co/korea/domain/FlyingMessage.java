package kr.co.korea.domain;

import java.io.Serializable;

/**
 * Created by kjs on 2016-02-16.
 */
public class FlyingMessage implements Serializable {
    private FlyingMessageType flyingMessageType;
    private String droneName;

    public FlyingMessageType getFlyingMessageType() {
        return flyingMessageType;
    }

    public void setFlyingMessageType(FlyingMessageType flyingMessageType) {
        this.flyingMessageType = flyingMessageType;
    }

    public String getDroneName() {
        return droneName;
    }

    public void setDroneName(String droneName) {
        this.droneName = droneName;
    }
}
