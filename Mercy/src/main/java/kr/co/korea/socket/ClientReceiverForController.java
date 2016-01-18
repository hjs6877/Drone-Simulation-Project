package kr.co.korea.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ClientReceiverForController extends Thread {
    Socket socket;
    DataInputStream dis;

    public ClientReceiverForController(Socket socket){
        this.socket = socket;

        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (dis != null){
            /**
             * TODO Controller로부터 받은 메시지 처리 구현.
             */
            try {
                String msg = dis.readUTF();
                if(msg.equals("exit")){
                    System.out.println("Controller부터 exit 명령을 전달받았습니다. 접속을 종료합니다.");
                    System.exit(-1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
