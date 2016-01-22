package kr.co.korea.proecessor;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;

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
        // TODO 리더와 팔로워를 구분해서 해당 클래스의 프로세스를 실행시키도록 구현.
        if(this.isLeader()){

        }else{

        }
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
