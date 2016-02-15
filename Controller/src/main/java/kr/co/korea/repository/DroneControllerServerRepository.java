package kr.co.korea.repository;

import kr.co.korea.DroneControllerServer;

import java.util.Vector;

/**
 * Created by ideapad on 2016-02-14.
 */
public class DroneControllerServerRepository extends Vector<DroneControllerServer> {
    public void addDroneControllerServer(DroneControllerServer droneControllerServer) {
        this.addElement(droneControllerServer);
    }
}
