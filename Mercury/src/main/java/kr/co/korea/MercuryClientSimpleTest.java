package kr.co.korea;

import kr.co.korea.domain.*;
import kr.co.korea.thread.ClientReceiverSimpleTest;
import kr.co.korea.thread.ClientSender;

import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClientSimpleTest {
    public static void main(String args[]) {
        try {
            String serverIp = "127.0.0.1";
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Mercury --> ControllerServer에 연결...");


            ClientSender clientSender = new ClientSender(socket);
            ClientReceiverSimpleTest clientReceiverSimpleTest = new ClientReceiverSimpleTest(socket, clientSender, "mercury");

            clientSender.start();
            clientReceiverSimpleTest.start();

            Drone initDrone = new Drone("mercury", new DroneSetting(), new FlyingInfo(new FlightStatus(), new HashMap<String, Double>()));


            clientSender.sendMessageOrDrone(FlyingMessage.STATUS_FLYING_READY);
        } catch(ConnectException ce) {
            ce.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    } // main
}
