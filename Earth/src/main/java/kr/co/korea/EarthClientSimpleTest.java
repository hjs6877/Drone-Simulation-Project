package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.thread.ClientReceiverSimpleTest;
import kr.co.korea.thread.ClientSender;

import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by ideapad on 2016-02-17.
 */
public class EarthClientSimpleTest {
    public static void main(String args[]) {
        try {
            String serverIp = "127.0.0.1";
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Earth --> ControllerServer에 연결...");


            ClientSender clientSender = new ClientSender(socket);
            ClientReceiverSimpleTest clientReceiverSimpleTest = new ClientReceiverSimpleTest(socket, clientSender, "mercury");

            clientSender.start();
            clientReceiverSimpleTest.start();

            Drone initDrone = new Drone("earth", new DroneSetting(), new FlyingInfo());


            clientSender.sendMessageOrDrone(FlyingMessage.STATUS_FLYING_READY);
        } catch(ConnectException ce) {
            ce.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    } // main
}
