package kr.co.korea;

import kr.co.korea.runner.DroneRunner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ideapad on 2016-02-12.
 */
public class DroneControllerServer extends Thread {
    private int serverPort = 5555;

    public void run(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);

            if(serverSocket != null){
                System.out.println("## Drone Controller 서버 start..");
            }

            while(true){
                Socket socket = serverSocket.accept();

                DroneRunner droneRunner = new DroneRunner(socket);

                DroneController.droneRunnerRepository.addDroneRunner(droneRunner);

                droneRunner.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
