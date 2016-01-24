package kr.co.korea.proecessor;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;
import kr.co.korea.role.Follower;
import kr.co.korea.role.Leader;

import java.io.Serializable;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-21.
 */
public class FlightProcessor implements Processor, Serializable {
    private String droneName;
    private DroneSetting setting;

    public FlightProcessor(String droneName, DroneSetting setting){
        this.droneName = droneName;
        this.setting = setting;
    }

    public void doProcess(Socket socket) {
        System.out.println("droneName: " + droneName);
        System.out.println("출발지: " + setting.getDeparture());
        System.out.println("목적지: " + setting.getDestination());

        FlightStatus status = null;
        /**
         * 리더와 프로세스를 확인 한뒤 비행 진행.
         */
        if(this.isLeader()){
            Leader leader = new Leader(droneName, setting);
            status = leader.doLeaderProcess();
        }else{
            Follower follower = new Follower(droneName, setting);
            status = follower.doFollowerProcess();
        }

        // TODO 컨트롤러쪽으로 비행 상태 객체를 전송하여 다음 프로세스 진행에 대한 결정을 위임한다.
    }

    /**
     * 해당 드론 프로세스가 리더인지 확인.
     *
     * @return
     */
    private boolean isLeader(){

        Map<String, Drone> droneMap = setting.getDroneMap();

        return droneMap.get(droneName).getLeaderOrFollower().equals("L");
    }

}
