package kr.co.korea.thread;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.Order;
import kr.co.korea.proecessor.Processor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-20.
 */
public class ClientReceiver extends Thread {
    Socket socket;
    ObjectInputStream objectInputStream;
    Drone drone;

    public ClientReceiver(Socket socket, Drone drone){
        this.socket = socket;
        this.drone = drone;
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

                /**
                 * 드론의 프로세스 진행.
                 * - ExitProcessor      : 종료 프로세서
                 * - FlightProcessor    : 비행 프로세서
                 */
                if(object instanceof Processor){
                    Processor processor = (Processor) object;
                    processor.doProcess(socket);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
