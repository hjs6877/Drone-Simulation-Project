package kr.co.korea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ServerReceiver extends Thread {
    HashMap clients = null;

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    ServerReceiver(Socket socket, HashMap clients){
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
            sendToAll("#" + name + "님이 ControllerServer에 연결 되었습니다.");


            System.out.println("현재 Controller에 접속 Drone수는 " + clients.size() + "입니다.");

            while(dis != null){
                System.out.println("메시지 전송 대기중..");
                sendToAll(dis.readUTF());  // readUTF( ) 에서 데이터를 읽을때까지 대기한다.
                System.out.println("메시지 전송..");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendToAll("#" + name + "Drone이 접속 종료하였습니다.-");

            System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]에서 접속 종료하였습니다.");
            System.out.println("현재 접속 Drone수는 " + clients.size() + "입니다.");
            clients.remove(name);
        }
    }

    public void sendToAll(String msg){
        Iterator iterator = clients.keySet().iterator();
        while(iterator.hasNext()){
            DataOutputStream dos = (DataOutputStream) clients.get(iterator.next());
            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
