package kr.co.korea.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by kjs on 2016-01-15.
 */
public class DroneSetting implements Serializable {
    private int numberOfDrone;
    private Map<String, Drone> droneMap;
    private int formationType;
    private String departure;
    private String destination;
    private Map<String, Coordination> departureCoordination;
    private Map<String, Coordination> destinationCoordination;
    private int speed;
    private double distance;
    private double flightTime;



    public int getNumberOfDrone() {
        return numberOfDrone;
    }

    public void setNumberOfDrone(int numberOfDrone) {
        this.numberOfDrone = numberOfDrone;
    }

    public Map<String, Drone> getDroneMap() {
        return droneMap;
    }

    public void setDroneMap(Map<String, Drone> droneMap) {
        this.droneMap = droneMap;
    }

    public int getFormationType() {
        return formationType;
    }

    public void setFormationType(int formationType) {
        this.formationType = formationType;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Map<String, Coordination> getDepartureCoordination() {
        return departureCoordination;
    }

    public void setDepartureCoordination(Map<String, Coordination> departureCoordination) {
        this.departureCoordination = departureCoordination;
    }

    public Map<String, Coordination> getDestinationCoordination() {
        return destinationCoordination;
    }

    public void setDestinationCoordination(Map<String, Coordination> destinationCoordination) {
        this.destinationCoordination = destinationCoordination;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    public double getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(double flightTime) {
        this.flightTime = flightTime;
    }
}
