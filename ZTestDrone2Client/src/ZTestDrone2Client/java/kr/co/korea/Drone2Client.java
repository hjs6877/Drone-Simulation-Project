package kr.co.korea;

import java.io.IOException;
import java.net.Socket;


/**
 * Created by ideapad on 2016-01-17.
 */
public class Drone2Client {
    public static void main(String[] args){
        Drone2Client drone2Client = new Drone2Client();
        drone2Client.startForController();
        drone2Client.startForDrone1Server();
    }

    public void startForController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 7777);
            System.out.println("Controller 서버에 연결되었습니다.");

            Thread sender = new Thread(new ClientSenderForController(socket, "Drone2Client"));
            Thread receiver = new Thread(new ClientReceiverForController(socket));

            sender.start();
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startForDrone1Server(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 8888);
            System.out.println("Drone1 서버에 연결되었습니다.");

            Thread sender = new Thread(new ClientSenderForDrone1Server(socket, "Drone2Client"));
            Thread receiver = new Thread(new ClientReceiverForDrone1Server(socket));

            sender.start();
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
