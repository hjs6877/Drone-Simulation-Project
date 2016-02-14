package kr.co.korea.repository;

import kr.co.korea.DroneController;
import kr.co.korea.DroneControllerServer;
import kr.co.korea.domain.FlyingMessage;

import java.util.Vector;

/**
 * Created by ideapad on 2016-02-14.
 */
public class DroneControllerServerRepository extends Vector<DroneControllerServer> {
    public void addDroneControllerServer(DroneControllerServer droneControllerServer) {
        this.addElement(droneControllerServer);
    }
}
