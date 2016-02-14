package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.repository.DroneRunnerRepository;
import kr.co.korea.runner.DroneRunner;
import kr.co.korea.runner.DroneRunnerSimpleTest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

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

    public synchronized void setFlyingMessage(FlyingMessage flyingMessage) {
        //  TODO 메시지에 따라서 DroneRunnerRepository의 메시지 호출 메서드가 달라짐.

        /**
         * 장애로 인해 리더 교체 메시지를 리더로부터 전송 받았을 때,
         * - 비행중인 모든 Drone들에 비행 대기 메시지를 전송한다.
         */
        if(flyingMessage == FlyingMessage.STATUS_NEED_REPLACE_LEADER){
            System.out.println("++++ 수신 메시지:  리더 교체 필요 메시지를 수신하였습니다..");
            System.out.println("++++ 송신 메시지:  리더 교체 필요 상황이 발생하여 모든 Drone 들을 비행 대기 상태로 전환합니다..");
            DroneController.droneRunnerRepository.sendMessageToAll(FlyingMessage.DO_FLYING_WAIT);
        }

        /**
         * Drone 들로부터 비행 일시 중지 상태를 알리는 메시지를 전송 받았을 때,
         * - 모든 팔로워들로부터 메시지를 전송 받은 후, 리더에게 신규 리더 선출하라는 메시지를 전송한다.
         */
        if(flyingMessage == FlyingMessage.STATUS_FLYING_WAITED){
            /**
             * TODO 모든 Drone으로부터 메시지를 전송 받았는지 확인 후, 새로운 리더 선출을 위해
             * TODO 팔로워들의 현재까지의 누적 장애 이벤트를 비교,판단.
             */
            synchronized (DroneController.droneRunnerRepository){
                DroneRunnerRepository.messageFlyingWaitedCount++;
            }

            System.out.println("##DroneRunnerRepository.messageFlyingWaitedCount: " + DroneRunnerRepository.messageFlyingWaitedCount);
            if(DroneRunnerRepository.messageFlyingWaitedCount == DroneController.droneRunnerRepository.size()){
                System.out.println("## 모든 Drone이 비행 대기 상태가 되었습니다.");

                Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();
                while(iterator.hasNext()){
                    DroneRunner droneRunner = iterator.next();
                    Drone drone = droneRunner.getDrone();
                    FlyingInfo flyingInfo = drone.getFlyingInfo();


                    System.out.println("#### Drone 이름: " + drone.getName());
                    System.out.println("#### 리더 여부: " + drone.getLeaderOrFollower());
                    System.out.println("###### 누적 업데이트 된 최종 장애 정보 출력..");
                    System.out.println("###### flyingInfo.getFinalFlightStatus() 객체: " + flyingInfo.getFinalFlightStatus());
                    System.out.println("TRIVIAL: " + flyingInfo.getFinalFlightStatus().getTrivialList().size());
                    System.out.println("MINOR: " + flyingInfo.getFinalFlightStatus().getMinorList().size());
                    System.out.println("MAJOR: " + flyingInfo.getFinalFlightStatus().getMajorList().size());
                    System.out.println("CRITICAL: " + flyingInfo.getFinalFlightStatus().getCriticalList().size());
                    System.out.println("BLOCK: " + flyingInfo.getFinalFlightStatus().getBlockList().size());

                    System.out.println("===================================================================");

                }
            }
        }
    }
}
