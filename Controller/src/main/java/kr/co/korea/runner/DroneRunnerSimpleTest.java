package kr.co.korea.runner;

import kr.co.korea.DroneControllerSimpleTest;
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
public class DroneRunnerSimpleTest extends Thread {
    private boolean flag = false;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Drone drone;

    public DroneRunnerSimpleTest(Socket socket) throws IOException {
        this.socket = socket;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run(){
        try{
            while(objectInputStream != null){   // TODO ObjectInputStream이 null인걸로 판단하도록 수정.
                Object object = objectInputStream.readObject();
                FlyingMessage flyingMessage = null;

                if(object instanceof FlyingMessage){
                    flyingMessage = (FlyingMessage) object;
                }

                if(flyingMessage != null){

                    System.out.println("Flying Message::: " + flyingMessage);

                    /**
                     *  드론 클라이언트 접속 시, 전달 되는 메시지.
                     *  - 접속 확인 용.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_READY){
                        System.err.println(" 비행 준비 완료..");
                    }


                    /**
                     * 장애로 인해 리더 교체 메시지를 리더로부터 전송 받았을 때,
                     * - 비행중인 팔로워들에게 비행 대기 메시지를 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_NEED_REPLACE_LEADER){
                        System.out.println("++++ 송신 메시지:  리더 교체 필요 상황이 발생하여 팔로워들을 비행 대기 상태로 전환합니다..");
//                        DroneControllerSimpleTest.droneRunnerRepository.sendMessageToFollowers(FlyingMessage.DO_FLYING_WAIT);
                        DroneControllerSimpleTest.droneRunnerRepositorySimpleTest.sendMessageToAll(FlyingMessage.DO_FLYING_WAIT_FOR_REPLACE_LEADER);
                    }

                }
            }

            DroneControllerSimpleTest.droneRunnerRepositorySimpleTest.removeDroneRunner(this);


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

    public void sendMessage(FlyingMessage flyingMessage) throws IOException {

        this.objectOutputStream.writeObject(flyingMessage);
    }

    public Drone getDrone() {
        return drone;
    }

    public String toString(){
        return socket.toString();
    }
}
