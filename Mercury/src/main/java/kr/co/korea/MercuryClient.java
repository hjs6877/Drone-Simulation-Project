package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.thread.ClientReceiver;
import kr.co.korea.thread.ClientSender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClient {
    ObjectOutputStream oos;
    public static void main(String[] args){
        MercuryClient mercuryClient = new MercuryClient();
        mercuryClient.connectToController();
    }

    public void connectToController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Mercury client --> Controller에 연결되었습니다.");

            Drone drone = new Drone();
            drone.setName("mercury");

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(drone);

            Thread receiver = new Thread(new ClientReceiver(socket, drone));

            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
