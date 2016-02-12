package kr.co.korea;

import kr.co.korea.thread.ClientRunner;

import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class VenusClient {
    public static void main(String args[]) {
        try {
            String serverIp = "127.0.0.1";
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("서버에 연결되었습니다.1");
            Thread clientRunner   = new Thread(new ClientRunner(socket));
//            Thread receiver = new Thread(new ClientReceiver(socket));

            clientRunner.start();
//            receiver.start();
        } catch(ConnectException ce) {
            ce.printStackTrace();
        } catch(Exception e) {}
    } // main
}
