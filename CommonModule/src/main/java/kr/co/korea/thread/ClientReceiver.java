package kr.co.korea.thread;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class ClientReceiver extends Thread {
    private Socket socket;
    private ClientSender clientSender;
    private ObjectInputStream objectInputStream;
    private Drone drone;

    public ClientReceiver(Socket socket, ClientSender clientSender) {
        this.socket = socket;
        this.clientSender = clientSender;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e) {}
    }

    public void run() {
        try {

            /**
             * TODO 비행을 수행하는 FlyRunner는 메시지의 상황에 맞게 시작되거나, 대기하거나, 재시작된다.
             */
            Flyer flyer = new Flyer(socket, clientSender);      // TODO 보내는건 무조건 sendMessage를 이용하도록 수정 필요.
            Thread flyRunner = new Thread(flyer);

            while (objectInputStream != null){
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
                     * TODO 리더 교체 플래그 셋팅을 위한 중요 핵심 포인트.
                     */
                    flyer.setDrone(drone);

                    /**
                     * FLYING_START 메시지가 넘어 온다면 비행 시작.
                     */
                    if(flyingMessage == FlyingMessage.DO_FLYING_START){
                        System.out.println("===================================================================");
                        System.out.println("++++ 수신 메시지:  비행 시작 명령(DO_FLYING_START) 메시지를 수신하였습니다..");
                        System.out.println("===================================================================");
                        System.out.println("## " + drone.getName() + " 이(가) 비행을 시작합니다.");
                        System.out.println("===================================================================");
                        flyRunner.start();
                    }

                    /**
                     * DO_FLYING_STOP 메시지가 넘어 온다면, 비행 중지. 시스템 프로세스를 죽인다.
                     */
                    if(flyingMessage == FlyingMessage.DO_FLYING_STOP){
                        System.out.println("===================================================================");
                        System.out.println("++++ 수신 메시지:  비행 중단 명령(DO_FLYING_STOP) 메시지를 수신하였습니다..");
                        System.out.println("===================================================================");
                        System.out.println("## 비행을 중단합니다..");
                        System.out.println("===================================================================");
                        objectInputStream.close();
                        socket.close();
                        System.exit(-1);
                    }

                    /**
                     * FLYING_WAIT 메시지가 넘어 온다면, 비행 대기. 쓰레드를 wait 시킨다.
                     * - 현재까지의 비행 정보를 DroneRunner에게 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.DO_FLYING_WAIT){
                        System.out.println("===================================================================");
                        System.out.println("++++ 수신 메시지: 비행 대기 명령(DO_FLYING_WAIT) 메시지를 수신하였습니다..");
                        System.out.println("===================================================================");

                        /** 비행 대기 명령을 할당 **/
                        flyer.DO_FLYING_WAIT = FlyingMessage.DO_FLYING_WAIT;

                        drone = flyer.getDrone();

                        clientSender.sendMessageOrDrone(drone);
                        Thread.sleep(1000);
                        clientSender.sendMessageOrDrone(FlyingMessage.STATUS_FLYING_WAITED);
                    }

                    /**
                     * FLYING_RESUME 메시지가 넘어 온다면, 비행 재개.
                     * TODO 쓰레드를 깨우는건 여기서 깨운다.
                     */
                    if(flyingMessage == FlyingMessage.DO_FLYING_RESUME){
                        System.out.println("===================================================================");
                        System.out.println("++++ 수신 메시지: 비행 재개 명령(DO_FLYING_RESUME) 메시지를 수신하였습니다..");
                        System.out.println("===================================================================");
                        System.out.println("## 비행을 재개합니다..");
                        System.out.println("===================================================================");
                        flyer.DO_FLYING_WAIT = FlyingMessage.DO_FLYING_RESUME;
                        synchronized (flyer){
                            flyer.notifyAll();
                        }
                    }

                    /**
                     * DO_FLYING_FINISH 메시지가 넘어 온다면, 착륙 완료 후, 비행을 중단한다. 시스템 프로세스를 죽인다.
                     */
                    if(flyingMessage == FlyingMessage.DO_FLYING_FINISH){
                        System.out.println("===================================================================");
                        System.out.println("++++ 수신 메시지: 비행 종료 명령 메시지를 수신하였습니다..");
                        System.out.println("===================================================================");
                        System.out.println("## 착륙 완료. 비행을 종료합니다..");
                        System.out.println("===================================================================");

                        objectInputStream.close();
                        socket.close();
                        System.exit(-1);
                    }





                }
            }

            objectInputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } // run
}
