package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.thread.ClientReceiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClient {
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public static void main(String[] args){
        MercuryClient mercuryClient = new MercuryClient();
        mercuryClient.connectToController();
    }

    public void connectToController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Mercury client --> Controller에 연결되었습니다.");

            /**
             *  TODO Drone 정보를 컨트롤러에게 던지고, 컨트롤러에서 설정 된 정보를 다시 받는다.
             *  - 컨트롤러는 Dronesetting 정보를 던지는 것이 아니 Drone 객체에 DroneSetting 객체까지 담아서 던져야 된다.
             *  - Drone 객체는 컨트롤러의 맵에 oos와 함께 저장되어야 할듯..
             */

            Drone droneObj = new Drone("mercury", new DroneSetting(), new FlyingInfo());

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(droneObj);

            ClientReceiver clientReceiver = new ClientReceiver(objectOutputStream);
            Thread receiver = new Thread(clientReceiver);

            while (objectInputStream != null){
                try {
                    Object object = objectInputStream.readObject();
                    Drone drone = (Drone) object;
                    /**
                     * TODO 쓰레드가 시작된 상황에서 이부분이 가능한지 확인 필요. 리더 교체 플래그 셋팅을 위한 중요 핵심 포인트.
                     */
                    clientReceiver.setDrone(drone);

                    System.out.println("넘어온 Flying message: " + drone.getFlyingInfo().getMessage());
                    /**
                     * FLYING_START 메시지가 넘어 온다면 비행 시작.
                     */
                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.FLYING_START){
                        receiver.start();
                    }

                    /**
                     * FLYING_STOP 메시지가 넘어 온다면, 비행 중지. 시스템 프로세스를 죽인다.
                     */
                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.FLYING_STOP){
                        System.out.println("비행을 중단합니다..");
                        System.exit(-1);
                    }


                    /**
                     * FLYING_WAIT 메시지가 넘어 온다면, 비행 대기. 쓰레드를 wait 시킨다.
                     */
                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.FLYING_WAIT){
                        System.out.println("리더 선출을 위해 비행 대기합니다..");
                        clientReceiver.waitFlight();
                    }

                    /**
                     * FLYING_RESUME 메시지가 넘어 온다면, 비행 재개.
                     * TODO 쓰레드를 깨우는건 여기서 깨운다.
                     */
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
