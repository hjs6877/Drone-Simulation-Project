package kr.co.korea.thread;

import kr.co.korea.domain.Drone;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class ClientSender extends Thread{
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;



    public ClientSender(Socket socket) {
        this.socket = socket;
        try {
            System.out.println("서버에 연결되었습니다.2");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("서버에 연결되었습니다.3");
        } catch(Exception e) {}
    }

    public void run() {
        System.out.println("Client Sender-서버에 연결되었습니다.4");
        while(objectOutputStream != null){

        }
    } // run()

    public void sendMessage(Drone drone) throws IOException {
        objectOutputStream.writeObject(drone);
        objectOutputStream.flush();
    }
}
