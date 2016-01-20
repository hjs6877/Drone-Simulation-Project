package kr.co.korea.thread;

import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.Order;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-20.
 */
public class ClientReceiver extends Thread {
    Socket socket;
    ObjectInputStream objectInputStream;

    public ClientReceiver(Socket socket){
        this.socket = socket;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (objectInputStream != null){
            try {
                Object object = objectInputStream.readObject();
                if(object instanceof Order){
                    String msg = ((Order) object).getProcessOrder();
                    if(msg.equals("exit")){
                        System.out.println("Controller부터 exit 명령을 전달받았습니다. 접속을 종료합니다.");
                        System.exit(-1);
                    }
                }else if(object instanceof DroneSetting){
                    System.out.println("Controller부터 DroneSetting 정보를 전달 받았습니다.");
                    DroneSetting setting = (DroneSetting) object;
                    System.out.println("출발지: " + setting.getDeparture());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
