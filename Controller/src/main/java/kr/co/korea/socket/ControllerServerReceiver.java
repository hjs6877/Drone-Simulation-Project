package kr.co.korea.socket;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.proecessor.FlightProcessor;
import kr.co.korea.util.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ControllerServerReceiver extends Thread {
    private LinkedHashMap<String, Drone> clients = null;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ControllerServerReceiver(Socket socket, LinkedHashMap<String, Drone> clients) {
        this.clients = clients;
        this.socket = socket;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Drone droneObj = null;
        String name = "";
        try {

            while(objectInputStream != null){
                droneObj = (Drone) objectInputStream.readObject();

                name = droneObj.getName();

                droneObj.setOutputStream(objectOutputStream);

                clients.put(name, droneObj);
                /**
                 * TODO Drone의 메시지를 전송 받아서 처리.
                 * 리더로부터 온 메시지와 팔로워로부터 온 메시지를 구분해서 처리한다.
                 */
                System.out.println("drone 객체 확인: " + name);
                System.out.println("리더인가? " + StringUtils.nullToWhiteSpace(droneObj.getLeaderOrFollower()).equals("L"));
                if(StringUtils.nullToWhiteSpace(droneObj.getLeaderOrFollower()).equals("L")){
                    FlyingMessage flyingMessage = droneObj.getFlyingInfo().getMessage();
                    System.out.println("발생된 메시지: " + flyingMessage);
                    if(flyingMessage == FlyingMessage.REPLACE_LEADER){
                        Iterator iterator = clients.keySet().iterator();

                        // TODO 여기가 의심스럽다. drone 명이 바뀌는것 같음. 리더는 한명인데 메시지를 다 보내고 있음.
                        while(iterator.hasNext()){
                            String droneName = (String) iterator.next();
                            Drone drone = clients.get(droneName);

                            drone.getFlyingInfo().setMessage(FlyingMessage.FLYING_RESUME);

                            objectOutputStream.writeObject(drone);
                        }
                    }
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
