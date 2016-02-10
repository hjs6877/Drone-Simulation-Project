package kr.co.korea.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 비행중인 정보를 저장.
 * - 마지막 비행 좌표.
 * - 잔여 비행 시간
 * - 잔여 거리
 * -
 */
public class FlyingInfo implements Serializable {
    private Map<String, Coordination> lastDepartureCoordination;
    private double remainDistance;
    private long flightTime;
    private FlightStatus flightStatus;
    private FlyingMessage message;

    public Map<String, Coordination> getLastDepartureCoordination() {
        return lastDepartureCoordination;
    }

    public void setLastDepartureCoordination(Map<String, Coordination> lastDepartureCoordination) {
        this.lastDepartureCoordination = lastDepartureCoordination;
    }

    public double getRemainDistance() {
        return remainDistance;
    }

    public void setRemainDistance(double remainDistance) {
        this.remainDistance = remainDistance;
    }

    public long getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(long flightTime) {
        this.flightTime = flightTime;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(FlightStatus flightStatus) {
        this.flightStatus = flightStatus;
    }

    public FlyingMessage getMessage() {
        return message;
    }

    public void setMessage(FlyingMessage message) {
        this.message = message;
    }
}
