package kr.co.korea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ideapad on 2016-01-16.
 */
public class ControllerServer{
    HashMap clients = null;

    ControllerServer(){
        clients = new HashMap();
        Collections.synchronizedMap(clients);
    }
    public static void main(String[] args){
        new ControllerServer().start();
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Controller 서버가 시작되었습니다.");

            while(true){
                socket = serverSocket.accept();         // client 에서 접속할때까지 대기. 접속하면 socket 객체 반환.
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");

                ServerReceiver receiver = new ServerReceiver(socket, clients);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
