package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.thread.ClientReceiver;
import kr.co.korea.thread.ClientReceiverSimpleTest;
import kr.co.korea.thread.ClientSender;

import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClient {
    public static void main(String args[]) {

        try {
            String serverIp = "127.0.0.1";
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Mercury --> ControllerServer에 연결...");

            ClientSender clientSender = new ClientSender(socket);
            ClientReceiver clientReceiver = new ClientReceiver(socket, clientSender);

            clientSender.start();
            clientReceiver.start();

            Drone initDrone = new Drone("mercury", new DroneSetting(), new FlyingInfo());

            clientSender.sendMessageOrDrone(initDrone);
            clientSender.sendMessageOrDrone(FlyingMessage.STATUS_FLYING_READY);
        } catch(ConnectException ce) {
            ce.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    } // main
}
