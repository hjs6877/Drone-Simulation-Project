package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.thread.ClientRunner;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class VenusClient {
    private String serverIp = "127.0.0.1";
    private int serverPort = 5555;
    private Socket socket;

    public static void main(String[] args) throws IOException {
        VenusClient venusClient = new VenusClient();
        venusClient.connectToController();
    }

    public void connectToController() throws IOException {

        try {
            this.socket = new Socket(serverIp, serverPort);
            if(socket != null){
                System.out.println("Venus client --> Controller에 연결되었습니다.");
            }


            /**
             * 초기 접속 성공 메시지를 보낸다.
             */
            Drone initDrone = new Drone("venus", new DroneSetting(), new FlyingInfo());
            initDrone.getFlyingInfo().setMessage(FlyingMessage.FLYING_READY);


            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(initDrone);


            ClientRunner clientRunner = new ClientRunner(socket);
            clientRunner.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}