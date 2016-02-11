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
public class VenusClientMain {
    private String serverIp = "127.0.0.1;";
    private int serverPort = 5555;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean flag = false;


    public static void main(String[] args) throws IOException {
        VenusClientMain venusClientMain = new VenusClientMain();
        venusClientMain.connectToController();
    }

    public void connectToController(){

        try {
            this.socket = new Socket(serverIp, serverPort);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

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