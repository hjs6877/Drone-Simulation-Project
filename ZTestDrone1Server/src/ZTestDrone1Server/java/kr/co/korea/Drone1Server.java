package kr.co.korea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by ideapad on 2016-01-16.
 */
public class Drone1Server {
    HashMap clientsDrone = null;

    Drone1Server(){
        clientsDrone = new HashMap();
        Collections.synchronizedMap(clientsDrone);
    }

    public static void main(String[] args){
        Drone1Server drone1Server = new Drone1Server();
        drone1Server.startForController();
        drone1Server.startForDroneServer();
    }

    public void startForController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socketClient = new Socket(serverIp, 7777);
            System.out.println("Controller 서버에 연결되었습니다.");

            Thread sender = new Thread(new ClientSenderForController(socketClient, "Drone1Server"));
            Thread receiver = new Thread(new ClientReceiverForController(socketClient));

            sender.start();
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startForDroneServer(){
        ServerSocket serverSocketForDrone = null;
        Socket socket = null;

        try {
            serverSocketForDrone = new ServerSocket(8888);
            System.out.println("Drone 서버가 시작되었습니다.");

            while(true){
                socket = serverSocketForDrone.accept();         // client 에서 접속할때까지 대기. 접속하면 socket 객체 반환.
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");

                ServerReceiver receiver = new ServerReceiver(socket, clientsDrone);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
