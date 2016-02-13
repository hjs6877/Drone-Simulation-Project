package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.thread.ClientSender;
import kr.co.korea.thread.Flyer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class MercuryClientReceiver extends Thread {
    private Socket socket;
    private ClientSender clientSender;
    private ObjectInputStream objectInputStream;
    private Drone drone;
    private boolean flag = false;


    public MercuryClientReceiver(Socket socket, ClientSender clientSender) {
        this.socket = socket;
        this.clientSender = clientSender;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e) {}
    }

    public void run() {
//        while(objectInputStream!=null) {
//            try {
//                System.out.println(objectInputStream.readUTF());
//            } catch(IOException e) {}
//        }

        try {

            /**
             * TODO 비행을 수행하는 FlyRunner는 메시지의 상황에 맞게 시작되거나, 대기하거나, 재시작된다.
             */
            Flyer flyer = new Flyer(socket, clientSender, "");      // TODO 보내는건 무조건 sendMessage를 이용하도록 수정 필요.
            Thread flyRunner = new Thread(flyer);

            while (objectInputStream != null){
                Object object = objectInputStream.readObject();
                drone = (Drone) object;
                System.out.println("drone 이름1: " + drone.getName() + "-->" + drone.getFlyingInfo().getMessage());
                if(drone != null){
                    System.out.println("drone 이름2: " + drone.getName() + "-->" + drone.getFlyingInfo().getMessage());
                    /**
                     * TODO 쓰레드가 시작된 상황에서 이부분이 가능한지 확인 필요. 리더 교체 플래그 셋팅을 위한 중요 핵심 포인트.
                     */
                    flyer.setDrone(drone);

                    /**
                     * FLYING_START 메시지가 넘어 온다면 비행 시작.
                     */
                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.DO_FLYING_START){
                        System.out.println(drone.getName() + "이 비행을 시작합니다.");
                        flyRunner.start();
                    }

                    /**
                     * DO_FLYING_STOP 메시지가 넘어 온다면, 비행 중지. 시스템 프로세스를 죽인다.
                     */
                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.DO_FLYING_STOP){
                        System.out.println("비행을 중단합니다..");
                        objectInputStream.close();
                        socket.close();
                        System.exit(-1);
                    }

                    /**
                     * FLYING_WAIT 메시지가 넘어 온다면, 비행 대기. 쓰레드를 wait 시킨다.
                     */
                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.DO_FLYING_WAIT){
                        System.out.println("++++ 수신 메시지: 리더 선출을 위해 비행 대기합니다..");
                        flyer.waitFlight();
                    }

                    /**
                     * DO_FLYING_FINISH 메시지가 넘어 온다면, 착륙 완료 후, 비행을 중단한다. 곧, 시스템 프로세스를 죽인다.
                     */
                    if(drone.getFlyingInfo().getMessage() == FlyingMessage.DO_FLYING_FINISH){
                        System.out.println("착륙 완료. 비행을 중단합니다..");
                        objectInputStream.close();
                        socket.close();
                        System.exit(-1);
                    }



                    /**
                     * FLYING_RESUME 메시지가 넘어 온다면, 비행 재개.
                     * TODO 쓰레드를 깨우는건 여기서 깨운다.
                     */
                }else{
                    flag = true;
                }
            }

            objectInputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    } // run
}