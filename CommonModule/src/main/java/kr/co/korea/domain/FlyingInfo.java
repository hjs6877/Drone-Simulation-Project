package kr.co.korea.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 비행중인 정보를 저장.
 * - 최종 메시지
 * - 발생한 장애 현황
 * - 최종 비행 좌표
 * - 총 비행 시간
 * - 잔여 거리
 *
 *
 */
public class FlyingInfo implements Serializable {
    private FlyingMessage flyingMessage = new FlyingMessage();
    private FlightStatus finalFlightStatus = new FlightStatus();
    private Map<String, Double> finalCoordination = new HashMap<String, Double>();
    private long finalFlightTime;
    private double remainDistance;

    public Map<String, Double> getFinalCoordination() {
        return finalCoordination;
    }

    public void setFinalCoordination(Map<String, Double> finalCoordination) {
        this.finalCoordination = finalCoordination;
    }

    public long getFinalFlightTime() {
        return finalFlightTime;
    }

    public void setFinalFlightTime(long finalFlightTime) {
        this.finalFlightTime = finalFlightTime;
    }

    public double getRemainDistance() {
        return remainDistance;
    }

    public void setRemainDistance(double remainDistance) {
        this.remainDistance = remainDistance;
    }

    public FlightStatus getFinalFlightStatus() {
        return finalFlightStatus;
    }

    public void setFinalFlightStatus(FlightStatus finalFlightStatus) {
        this.finalFlightStatus = finalFlightStatus;
    }

    public FlyingMessage getFlyingMessage() {
        return flyingMessage;
    }

    public void setFlyingMessage(FlyingMessage flyingMessage) {
        this.flyingMessage = flyingMessage;
    }
}
