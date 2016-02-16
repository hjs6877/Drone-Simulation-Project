package kr.co.korea.thread;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class ClientReceiverSimpleTest extends Thread {
    private Socket socket;
    private ClientSender clientSender;
    private ObjectInputStream objectInputStream;
    private Drone drone;
    private boolean flag = false;
    private String name;

    public ClientReceiverSimpleTest(Socket socket, ClientSender clientSender, String name) {
        this.socket = socket;
        this.clientSender = clientSender;
        this.name = name;

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
            FlyerSimpleTest flyerSimpleTest = new FlyerSimpleTest(socket, clientSender, name);      // TODO 보내는건 무조건 sendMessage를 이용하도록 수정 필요.
            Thread flyRunner = new Thread(flyerSimpleTest);

            while (objectInputStream != null){
                Object object = objectInputStream.readObject();
                FlyingMessageType flyingMessageType = null;

                if(object instanceof FlyingMessageType){
                    flyingMessageType = (FlyingMessageType) object;
                }

                if(flyingMessageType != null){
                    System.out.println("Flying Message::: " + flyingMessageType);

                    /**
                     * FLYING_START 메시지가 넘어 온다면 비행 시작.
                     */
                    if(flyingMessageType == FlyingMessageType.DO_FLYING_START){
                        System.out.println("ㅇㅇ 이 비행을 시작합니다.");

                        flyRunner.start();
                    }

                    /**
                     * FLYING_WAIT 메시지가 넘어 온다면, 비행 대기. 쓰레드를 wait 시킨다.
                     */
                    if(flyingMessageType == FlyingMessageType.DO_FLYING_WAIT_FOR_REPLACE_LEADER){

                        System.out.println("++++ 수신 메시지: 리더 선출을 위해 비행 대기합니다..");
                        flyerSimpleTest.waitMessage = "wait";
                        try {
                            Thread.sleep(5000);
                            flyerSimpleTest.waitMessage = "";
                            synchronized (flyerSimpleTest){
                                flyerSimpleTest.notifyAll();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

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
