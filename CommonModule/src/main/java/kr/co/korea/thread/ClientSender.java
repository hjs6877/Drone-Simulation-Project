package kr.co.korea.thread;

import kr.co.korea.domain.Drone;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-20.
 */
public class ClientSender extends Thread {
    Socket socket;
    ObjectOutputStream oos;
    Drone drone;

    public ClientSender(Socket socket, Drone drone){
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
                 * Drone client ���� ��, Drone �̸� ����.
                 */
                oos.writeObject(drone);

                while(oos != null){
                    // TODO Drone�� �޽��� ���� ���μ��� ����.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
