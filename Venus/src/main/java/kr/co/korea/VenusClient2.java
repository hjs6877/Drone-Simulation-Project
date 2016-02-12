package kr.co.korea;

import kr.co.korea.thread.ClientRunner;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ideapad on 2016-01-17.
 */
public class VenusClient2 {


    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 5555;

        try {
            Socket socket = new Socket(serverIp, serverPort);
            if (socket != null) {
                System.out.println("Venus client --> Controller에 연결되었습니다.");
            }

            ClientRunner clientRunner = new ClientRunner(socket);
            clientRunner.start();

            /**
             * 초기 접속 성공 메시지를 보낸다.
             * TODO ClientRunner 쓰레드 실행전 ObjectOutputStream을 생성하지 않으면 쓰레드가 더이상 진행하지 않음.해결 필요
             * */
//            Drone initDrone = new Drone("venus", new DroneSetting(), new FlyingInfo());
//            initDrone.getFlyingInfo().setMessage(FlyingMessage.FLYING_READY);
//
//
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.writeObject(initDrone);


        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }


}