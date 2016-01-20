package kr.co.korea.domain;

import java.io.Serializable;

/**
 * Created by ideapad on 2016-01-16.
 */
public class Coordination implements Serializable {
    double longitude;
    double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
