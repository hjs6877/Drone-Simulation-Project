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
    private Drone droneFromClient;
    private Drone droneToClient;

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
                droneFromClient = (Drone) object;
                flyingMessage = droneFromClient.getFlyingInfo().getMessage();


                if(flyingMessage != null){

                    /**
                     *  Drone Client 로부터 전송 받은 메시지 처리를 위해 DroneControllerServerNew 메시지 전달.
                     */
                    DroneController.droneControllerServerRepository.get(0).setDroneFromClient(droneFromClient);

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

    public synchronized void sendDroneToClient(Drone drone) throws IOException {
        this.objectOutputStream.reset();
        this.objectOutputStream.writeObject(drone);
        objectOutputStream.flush();
    }

    public Drone getDroneFromClient() {
        return droneFromClient;
    }

    public void setDroneFromClient(Drone droneFromClient) {
        this.droneFromClient = droneFromClient;
    }

    public Drone getDroneToClient() {
        return droneToClient;
    }

    public void setDroneToClient(Drone droneToClient) {
        this.droneToClient = droneToClient;
    }

    public String toString(){
        return socket.toString();
    }
}