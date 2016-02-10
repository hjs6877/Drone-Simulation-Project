package kr.co.korea.proecessor;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;
import kr.co.korea.role.AerialVehicle;
import kr.co.korea.role.Follower;
import kr.co.korea.role.Leader;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-21.
 */
public class FlightProcessor implements Processor, Serializable {
    private String droneName;
    private Drone drone;
    private DroneSetting setting;

    public FlightProcessor(String droneName, Drone drone){
        this.droneName = droneName;
        this.drone = drone;
    }

    public void doProcess(ObjectOutputStream objectOutputStream) {

        FlightStatus status = null;
        /**
         * 리더와 프로세스를 확인 한뒤 비행 진행.
         */
        if(this.isLeader()){
            AerialVehicle leader = new Leader(objectOutputStream, droneName, drone);
            status = leader.fly();
        }else{
            AerialVehicle follower = new Follower(objectOutputStream, droneName, drone);
            status = follower.fly();
        }

        // TODO 컨트롤러쪽으로 비행 상태 객체를 전송하여 다음 프로세스 진행에 대한 결정을 위임한다.
    }

    /**
     * 해당 드론 프로세스가 리더인지 확인.
     *
     * @return
     */
    private boolean isLeader(){

        return drone.getLeaderOrFollower().equals("L");
    }

}
