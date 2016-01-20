package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.thread.ClientReceiver;
import kr.co.korea.thread.ClientSender;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class VenusClient {
    public static void main(String[] args){
        VenusClient mercuryClient = new VenusClient();
        mercuryClient.connectToController();
    }

    public void connectToController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Venus client --> Controller에 연결되었습니다.");

            Drone drone = new Drone();
            drone.setName("venus");

            Thread sender = new Thread(new ClientSender(socket, drone));
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
