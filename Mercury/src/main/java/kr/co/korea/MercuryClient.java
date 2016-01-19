package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.socket.ClientReceiverForController;
import kr.co.korea.socket.ClientSenderForController;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClient {
    public static void main(String[] args){
        MercuryClient mercuryClient = new MercuryClient();
        mercuryClient.startForController();
    }

    public void startForController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Mercury client --> Controller에 연결되었습니다.");

            Drone drone = new Drone();
            drone.setName("mercury");
            Thread sender = new Thread(new ClientSenderForController(socket, drone));
            Thread receiver = new Thread(new ClientReceiverForController(socket));

            sender.start();
            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
