package kr.co.korea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ServerReceiver extends Thread {
    HashMap clientsDrone = null;

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    ServerReceiver(Socket socket, HashMap clientsDrone){
        this.clientsDrone = clientsDrone;
        Collections.synchronizedMap(clientsDrone);

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
            clientsDrone.put(name, dos);
            sendToAll("#" + name + "님이 DroneServer에 연결 되었습니다.");


            System.out.println("현재 DroneServer에 접속 Drone수는 " + clientsDrone.size() + "입니다.");

            while(dis != null){
                sendToAll(dis.readUTF());  // readUTF( ) 에서 데이터를 읽을때까지 대기한다.
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendToAll("#" + name + "Drone이 접속 종료하였습니다.-");

            System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]에서 접속 종료하였습니다.");
            System.out.println("현재 DroneServer에 접속한 Drone수는 " + clientsDrone.size() + "입니다.");
            clientsDrone.remove(name);
        }
    }

    public void sendToAll(String msg){
        Iterator iterator = clientsDrone.keySet().iterator();
        while(iterator.hasNext()){
            DataOutputStream dos = (DataOutputStream) clientsDrone.get(iterator.next());
            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
