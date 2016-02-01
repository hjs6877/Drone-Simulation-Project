package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.thread.ClientReceiver;
import kr.co.korea.thread.ClientSender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClient {
    ObjectOutputStream oos;
    public static void main(String[] args){
        MercuryClient mercuryClient = new MercuryClient();
        mercuryClient.connectToController();
    }

    public void connectToController(){
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 5555);
            System.out.println("Mercury client --> Controller에 연결되었습니다.");

            /**
             *  TODO Drone 정보를 컨트롤러에게 던지고, 컨트롤러에서 설정 된 정보를 다시 받는다.
             *  - 컨트롤러는 Dronesetting 정보를 던지는 것이 아니 Drone 객체에 DroneSetting 객체까지 담아서 던져야 된다.
             *  - Drone 객체는 컨트롤러의 맵에 oos와 함께 저장되어야 할듯..
             */

            Drone drone = new Drone("mercury", new DroneSetting(), new FlyingInfo());

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(drone);

            Thread receiver = new Thread(new ClientReceiver(socket, drone));

            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
