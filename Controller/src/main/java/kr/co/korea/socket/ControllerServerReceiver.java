package kr.co.korea.socket;

import kr.co.korea.domain.Drone;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ControllerServerReceiver extends Thread {
    private Map<String, Drone> clients = null;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ControllerServerReceiver(Socket socket, Map<String, Drone> clients) {
        this.clients = clients;
        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Drone drone = null;
        String name = "";
        try {
            drone = (Drone) ois.readObject();
            name = drone.getName();

            drone.setOutputStream(oos);

            clients.put(name, drone);

            while(ois != null){
                /**
                 * TODO Drone의 메시지를 전송 받아서 처리.
                 */
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("#" + name + "Drone이 접속 종료하였습니다.-");
            System.out.println("현재 접속 Drone수는 " + clients.size() + "입니다.");
            clients.remove(name);
        }
    }
}
