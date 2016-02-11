package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.thread.ClientReceiver;
import kr.co.korea.thread.ClientSender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClient {
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public static void main(String[] args){
        MercuryClient mercuryClient = new MercuryClient();
        mercuryClient.connectToController();
    }

    public void connectToController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Mercury client --> Controller에 연결되었습니다.");

            Drone drone = new Drone("mercury", new DroneSetting(), new FlyingInfo());

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(drone);

            ClientReceiver clientReceiver = new ClientReceiver(socket, objectOutputStream);

            Thread receiver = new Thread(clientReceiver);

            while (objectInputStream != null){
                try {
                    Object object = objectInputStream.readObject();
                    drone = (Drone) object;
                    clientReceiver.setDrone(drone);

                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.FLYING_START){
                        receiver.start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("실행 쓰레드::: " + receiver.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
