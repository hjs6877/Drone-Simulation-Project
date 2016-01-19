package kr.co.korea.socket;

import kr.co.korea.domain.Drone;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ClientSenderForController extends Thread {
    Socket socket;
    ObjectOutputStream oos;
    Drone drone;

    public ClientSenderForController(Socket socket, Drone drone){
        this.socket = socket;
        this.drone = drone;

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){

        if(oos != null){
            try {
                /**
                 * Drone client 가동 시, Drone 이름 전송.
                 */
                oos.writeObject(drone);

                while(oos != null){
                    // TODO Drone의 메시지 전송 프로세스 구현.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
