package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.thread.ClientReceiver;
import kr.co.korea.thread.ClientSender;

import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class VenusClient {
    public static void main(String args[]) {
        try {
            String serverIp = "127.0.0.1";
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Venus --> ControllerServer에 연결...");

            ClientSender clientSender = new ClientSender(socket);
            ClientReceiver clientReceiver = new ClientReceiver(socket, clientSender);

            clientSender.start();
            clientReceiver.start();

            Drone initDrone = new Drone("venus", new DroneSetting(), new FlyingInfo());
            initDrone.getFlyingInfo().setMessage(FlyingMessage.STATUS_FLYING_READY);
            clientSender.sendDroneToController(initDrone);
        } catch(ConnectException ce) {
            ce.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    } // main
}
