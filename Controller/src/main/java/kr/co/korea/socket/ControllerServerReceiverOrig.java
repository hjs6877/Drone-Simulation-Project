package kr.co.korea.socket;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ControllerServerReceiverOrig extends Thread {
    private Map<String, Drone> clients = null;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ControllerServerReceiverOrig(Socket socket, Map<String, Drone> clients) {
        this.clients = clients;
        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Drone drone = null;
        String name = "";
        try {
            while(ois != null){
                drone = (Drone) ois.readObject();
                name = drone.getName();

//                drone.setOutputStream(oos);

                clients.put(name, drone);
                /**
                 * 리더 교체 메시지를 받았을때의 처리.
                 * - 기존 리더의 경우, 비행 중지 메시지를 전송한다.
                 * - 팔로워들에게는 비행 대기 메시지를 전송한다.
                 */
                if(drone.getFlyingInfo().getMessage() == FlyingMessage.STATUS_ELECTED_NEW_LEADER){
                    /**
                     * - 리더에게는 FLYING_STOP 메시지 전송.
                     * - 팔로워들에게는 FLYING_WAIT 메시지 전송.
                     */
                    Iterator iterator = clients.keySet().iterator();


                    while(iterator.hasNext()){
                        String droneName = (String) iterator.next();
                        Drone droneObj = clients.get(droneName);

                        System.out.println(droneObj.getName() + "은 리더인가 팔로워인가:  " + droneObj.getLeaderOrFollower());

                        if(droneObj.getLeaderOrFollower().equals("L")){
                            System.out.println(droneName + "에게 리더 비행 중단 메시지 전송");
                            System.out.println(droneObj.getName() + " 객체2: " + droneObj);
                            System.out.println(droneObj.getName() + " 객체 메시지2-1: " + droneObj.getFlyingInfo().getMessage());
                            droneObj.getFlyingInfo().setMessage(FlyingMessage.DO_FLYING_STOP);

//                            ObjectOutputStream objectOutputStream = droneObj.getOutputStream();
//                            objectOutputStream.writeObject(droneObj);
                        }else{
                            System.out.println(droneName + "에게 팔로워 비행 대기 메시지 전송");
                            System.out.println(droneObj.getName() + " 객체2: " + droneObj);
                            System.out.println(droneObj.getName() + " 객체 메시지2-1: " + droneObj.getFlyingInfo().getMessage());
                            droneObj.getFlyingInfo().setMessage(FlyingMessage.DO_FLYING_WAIT);
                            System.out.println(droneObj.getName() + " 객체 메시지2-2: " + droneObj.getFlyingInfo().getMessage());
//                            ObjectOutputStream objectOutputStream = droneObj.getOutputStream();
//                            objectOutputStream.writeObject(droneObj);   // TODO 왜 여기서 FLYING_WAIT이 안넘어갈까?
                        }

                    }
                }

                /**
                 * 팔로워들로부터 비행 대기중이라는 메시지를 받았을때의 처리.
                 * - 어떤 팔로워가 리더가 될지 계산.
                 * - 새로운 리더가 계산 되면 해당 drone 객체의 리더 플래그를 "L"로 변경해준다.
                 * - 각각의 클라이언트들에게 FLYING_RESUME 메시지를 포함해서 drone 객체 전송.
                 */
                if(drone.getFlyingInfo().getMessage() == FlyingMessage.STATUS_FLYING_WAITED){


                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("#" + name + "Drone이 접속 종료하였습니다.");
            System.out.println("현재 접속 Drone수는 " + clients.size() + "입니다.");
            clients.remove(name);
        }
    }
}