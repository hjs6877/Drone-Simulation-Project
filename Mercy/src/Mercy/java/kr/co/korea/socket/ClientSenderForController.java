package kr.co.korea.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ClientSenderForController extends Thread {
    Socket socket;
    DataOutputStream dos;
    String name;

    public ClientSenderForController(Socket socket, String name){
        this.socket = socket;
        this.name = name;
        try {
            dos = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){

        if(dos != null){
            try {
                /**
                 * Drone client 가동 시, Drone 이름 전송.
                 */
                dos.writeUTF(name);

                while(dos != null){
                    // TODO Drone의 메시지 전송 프로세스 구현.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
