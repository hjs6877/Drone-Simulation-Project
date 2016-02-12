package kr.co.korea.runner;

import kr.co.korea.DroneController;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            while(!this.flag){
                Object object =  objectInputStream.readObject();
                Drone drone = (Drone) object;
                if(drone != null){
                    FlyingMessage flyingMessage = drone.getFlyingInfo().getMessage();

                    /**
                     * TODO 메시지에 따라서 DroneRunnerRepository의 메시지 호출 메서드가 달라짐.
                     */
                    if(flyingMessage == FlyingMessage.FLYING_READY){
                        System.out.println(drone.getName() + " 비행 준비 완료..");
                        DroneController.droneRunnerRepository.addDroneRunner(this);
                    }

                    if(flyingMessage == FlyingMessage.REPLACE_LEADER){
                        DroneController.droneRunnerRepository.sendMessageToFollowers(FlyingMessage.FLYING_WAIT);
                    }

                    if(flyingMessage == FlyingMessage.REPLACE_LEADER){

                    }

                    if(flyingMessage == FlyingMessage.REPLACE_LEADER){

                    }

                }else{
                    this.flag = true;
                }
            }

            DroneController.droneRunnerRepository.removeDroneRunner(this);
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(FlyingMessage flyingMessage) throws IOException {
        drone.getFlyingInfo().setMessage(flyingMessage);

        this.objectOutputStream.writeObject(drone);
        this.objectOutputStream.flush();
    }

    public Drone getDrone() {
        return drone;
    }

    public String toString(){
        return socket.toString();
    }
}
