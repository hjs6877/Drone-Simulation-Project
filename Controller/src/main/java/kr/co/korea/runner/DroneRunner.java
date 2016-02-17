package kr.co.korea.runner;

import kr.co.korea.DroneController;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by ideapad on 2016-02-11.
 */
public class DroneRunner extends Thread {
    private boolean flag = false;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Drone drone;

    public DroneRunner(Socket socket) throws IOException {
        this.socket = socket;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run(){
        try{
            while(objectInputStream != null){
                Object object = objectInputStream.readObject();
                FlyingMessage flyingMessage = null;

                if(object instanceof FlyingMessage){
                    flyingMessage = (FlyingMessage) object;
                }

                if(object instanceof Drone){
                    drone = (Drone) object;
                }

                if(flyingMessage != null){



                    /**
                     *  드론 클라이언트 접속 시, 전달 되는 메시지.
                     *  - 접속 확인 용.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_READY){
//                        System.err.println(drone.getName() + " 비행 준비 완료..");
                    }

                    /**
                     *  Drone Client 로부터 전송 받은 메시지 처리를 위해 DroneControllerServerNew 메시지 전달.
                     */
                    DroneController.droneControllerServerRepository.get(0).setFlyingMessage(flyingMessage);

                }else{
                    this.flag = true;
                }
            }

//            DroneController.droneRunnerRepository.removeDroneRunner(this);


        } catch (SocketException se){
            System.err.println(se.getMessage());
            try {
                if(objectOutputStream != null) objectOutputStream.close();
                if(objectInputStream != null) objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendDrone(Drone drone) throws IOException {
        this.objectOutputStream.writeObject(drone);
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public String toString(){
        return socket.toString();
    }
}