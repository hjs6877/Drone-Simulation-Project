package kr.co.korea.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ControllerServer extends Thread {
    LinkedHashMap clients;
    public ControllerServer(LinkedHashMap clients){
        this.clients = clients;
    }

    /**
     * Controller Socket server 시작.
     */
    public void run(){
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("## Drone Controller 서버 start..");

            while(true){
                socket = serverSocket.accept();         // client 에서 접속할때까지 대기. 접속하면 socket 객체 반환.
//                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");

                ControllerServerReceiver receiver = new ControllerServerReceiver(socket, clients);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
