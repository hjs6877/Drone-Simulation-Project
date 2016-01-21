package kr.co.korea.thread;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.Order;
import kr.co.korea.proecessor.Processor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-20.
 */
public class ClientReceiver extends Thread {
    Socket socket;
    ObjectInputStream objectInputStream;
    Drone drone;
    DroneSetting setting;

    public ClientReceiver(Socket socket, Drone drone){
        this.socket = socket;
        this.drone = drone;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (objectInputStream != null){
            try {
                Object object = objectInputStream.readObject();

                /**
                 * 드론의 프로세스 진행.
                 * - ExitProcessor      : 종료 프로세서
                 * - FlightProcessor    : 비행 프로세서
                 */
                if(object instanceof Processor){
                    Processor processor = (Processor) object;
                    processor.doProcess();
                }else if(object instanceof DroneSetting){
                    System.out.println("Controller부터 DroneSetting 정보를 전달 받았습니다.");
                    setting = (DroneSetting) object;

                    boolean isLeader = this.isLeader(setting);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 해당 드론 프로세스가 리더인지 확인.
     *
     * @param setting
     * @return
     */
    private boolean isLeader(DroneSetting setting){
        String droneName = drone.getName();
        Map<String, Drone> droneMap = setting.getDroneMap();

        return droneMap.get(droneName).getLeaderOrFollower().equals("L");
    }

}
