package kr.co.korea.sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class TcpIpMultichatClient {
    public static void main(String args[]) {
//        if(args.length!=1) {
//            System.out.println("USAGE: java TcpIpMultichatClient 대화명");
//            System.exit(0);
//        }

        try {
            String serverIp = "127.0.0.1";
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("서버에 연결되었습니다.1");
            Thread sender   = new Thread(new ClientSenderSample(socket, "bbbb"));
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start();
        } catch(ConnectException ce) {
            ce.printStackTrace();
        } catch(Exception e) {}
    } // main

    static class ClientReceiver extends Thread {
        Socket socket;
        private ObjectInputStream objectInputStream;

        ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch(IOException e) {}
        }

        public void run() {
            while(objectInputStream!=null) {
                try {
                    System.out.println(objectInputStream.readUTF());
                } catch(IOException e) {}
            }
        } // run
    } // ClientReceiver
}
