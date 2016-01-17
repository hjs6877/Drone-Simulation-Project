package kr.co.korea.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ControllerServerReceiver extends Thread {
    private LinkedHashMap clients = null;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ControllerServerReceiver(Socket socket, LinkedHashMap clients) {
        this.clients = clients;
        this.socket = socket;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        String name = "";

        try {
            name = dis.readUTF();
            clients.put(name, dos);

            while(dis != null){
                /**
                 * TODO Drone의 메시지를 전송 받아서 처리.
                 */
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("#" + name + "Drone이 접속 종료하였습니다.-");
            System.out.println("현재 접속 Drone수는 " + clients.size() + "입니다.");
            clients.remove(name);
        }
    }
}
