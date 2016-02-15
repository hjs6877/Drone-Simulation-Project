package kr.co.korea;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.repository.DroneRunnerRepository;
import kr.co.korea.runner.DroneRunner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ideapad on 2016-02-12.
 */
public class DroneControllerServer extends Thread {
    private int serverPort = 5555;
    private List<Drone> finalDroneInfoList = new ArrayList<Drone>();

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
         * 장애로 인해 리더로부터 리더 교체 필요(STATUS_NEED_REPLACE_LEADER) 메시지를  전송 받았을 때,
         * - 비행중인 모든 Drone들에 비행 대기 메시지를 전송한다.
         */
        if(flyingMessage == FlyingMessage.STATUS_NEED_REPLACE_LEADER){
            System.out.println("===================================================================");
            System.out.println("++++ 수신 메시지:  리더 교체 필요(STATUS_NEED_REPLACE_LEADER) 메시지를 수신하였습니다..");
            System.out.println("===================================================================");
            /**
             * '리더 교체 필요' 메시지를 전송한 자기 자신 이외에 추가로 1대의 Drone이 더 있어야 리더 교체를 할 수 있다.
             */
            if(DroneController.droneRunnerRepository.size() > 1){
                System.out.println("===================================================================");
                System.out.println("++++ 송신 메시지:  리더 교체 필요 상황이 발생하여 " +
                        "모든 Drone 들에게 비행 대기 명령(DO_FLYING_WAIT_FOR_REPLACE_LEADER) 메시지를 송신하였습니다..");
                System.out.println("===================================================================");
                DroneController.droneRunnerRepository.sendMessageToAll(FlyingMessage.DO_FLYING_WAIT_FOR_REPLACE_LEADER);
            }else{
                System.out.println("===================================================================");
                System.out.println("## 교체 할 리더가 없습니다. ");
                System.out.println("===================================================================");
                /**
                 * 기존 리더의 처리. 리더를 교체할 Drone이 없으므로, 비행 정보를 finalDroneInfoList에 저장하고 비행을 중지한다.
                 */
                this.doStopLeaderFlight();
            }

        }


        /**
         * Drone 들로부터 비행 일시 중지 상태를 알리는 메시지를 전송 받았을 때,
         * - 모든 팔로워들로부터 메시지를 전송 받은 후, 신규 리더를 선출한다.
         */
        if(flyingMessage == FlyingMessage.STATUS_FLYING_WAITED_FOR_REPLACE_LEADER){

            /**
             * TODO 모든 Drone으로부터 메시지를 전송 받았는지 확인 후, 새로운 리더 선출을 위해
             * TODO 팔로워들의 현재까지의 누적 장애 이벤트를 비교,판단.
             */
            synchronized (DroneController.droneRunnerRepository){
                DroneRunnerRepository.messageFlyingWaitedCount++;
            }

            System.out.println("===================================================================");
            System.out.println("++++ 수신 메시지:  " + DroneRunnerRepository.messageFlyingWaitedCount +
                    "번째 팔로워로부터 비행 대기 상태(STATUS_FLYING_WAITED_FOR_REPLACE_LEADER) 메시지를 수신하였습니다..");
            System.out.println("===================================================================");

            System.out.println("===================================================================");
            System.out.println("## DroneRunnerRepository.messageFlyingWaitedCount: " + DroneRunnerRepository.messageFlyingWaitedCount);
            System.out.println("===================================================================");

            if(DroneRunnerRepository.messageFlyingWaitedCount == DroneController.droneRunnerRepository.size()){
                System.out.println("===================================================================");
                System.out.println("## 모든 Drone이 비행 대기 상태가 되었습니다.");
                System.out.println("===================================================================");

                Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();
                DroneRunner droneRunnerNewLeader = null;
                while(iterator.hasNext()){
                    DroneRunner droneRunner = iterator.next();
                    Drone drone = droneRunner.getDrone();
                    String leaderOrFollower = drone.getLeaderOrFollower();
                    FlyingInfo flyingInfo = drone.getFlyingInfo();
                    double totalErrorPoint = flyingInfo.getFinalFlightStatus().getTotalErrorPoint();

                    System.out.println("===================================================================");
                    System.out.println("#### Drone 이름: " + drone.getName());
                    System.out.println("#### 리더 여부: " + drone.getLeaderOrFollower());
                    System.out.println("###### 누적 업데이트 된 최종 장애 정보 출력..");
                    System.out.println("TRIVIAL: " + flyingInfo.getFinalFlightStatus().getTrivialList().size());
                    System.out.println("MINOR: " + flyingInfo.getFinalFlightStatus().getMinorList().size());
                    System.out.println("MAJOR: " + flyingInfo.getFinalFlightStatus().getMajorList().size());
                    System.out.println("CRITICAL: " + flyingInfo.getFinalFlightStatus().getCriticalList().size());
                    System.out.println("BLOCK: " + flyingInfo.getFinalFlightStatus().getBlockList().size());
                    System.out.println("Total Error Point: " + flyingInfo.getFinalFlightStatus().getTotalErrorPoint());
                    System.out.println("===================================================================");

                    if(!leaderOrFollower.equals("L")){
                        if(droneRunnerNewLeader == null){
                            droneRunnerNewLeader = droneRunner;
                            continue;
                        }

                        if(totalErrorPoint < droneRunnerNewLeader.getDrone().getFlyingInfo().getFinalFlightStatus().getTotalErrorPoint()){
                            droneRunnerNewLeader = droneRunner;
                            continue;
                        }
                    }

                }

                System.out.println("===================================================================");
                System.out.println("## New Leader를 선출중입니다..");
                System.out.println("===================================================================");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("===================================================================");
                System.out.println("## New Leader가 선출되었습니다.");
                System.out.println("## New Leader: " + droneRunnerNewLeader.getDrone().getName());
                System.out.println("===================================================================");


                /**
                 * 리더 선출 프로세스가 끝났으므로 비행 대기 카운트를 초기화 한다.
                 */
                synchronized (DroneController.droneRunnerRepository){
                    DroneRunnerRepository.messageFlyingWaitedCount = 0;
                }

                /**
                 *  기존 리더의 처리
                 *  - finalDroneInfoList에 비행정보 저장.
                 *  - 비행 프로세스 종료 메시지 전송.
                 *  - DroneRunnerRepository에서 제거
                 */
                this.doStopLeaderFlight();

                /**
                 * 신규 리더의 처리
                 * - DroneRunner에서 리더 설정을 추가.
                 * - 남은 Drone들에게 신규 리더가 선출 되었음을 알린다.
                 */
                System.out.println("===================================================================");
                System.out.println("## 비행 대기했던 Drone들의 비행을 재개합니다.");
                System.out.println("===================================================================");
                System.out.println("++++ 송신 메시지: 비행 재개 명령(DO_FLYING_RESUME) 메시지를 송신하였습니다..");
                System.out.println("===================================================================");
                droneRunnerNewLeader.getDrone().setLeaderOrFollower("L");
                DroneController.droneRunnerRepository.sendMessageToAll(FlyingMessage.DO_FLYING_RESUME);
            }
        }

        /**
         * 장애로 인해 특정 팔로워로부터 비행 중지 필요(STATUS_NEED_STOP_FLYING) 메시지를 전송 받았을 때,
         * - 해당 Drone에게 비행 중지 메시지를 전송한다. TODO
         * -
         */
        if(flyingMessage == FlyingMessage.STATUS_NEED_STOP_FLYING){
            // TODO 누구로 부터 받은 메시지인지 표시 필요.
            System.out.println("===================================================================");
            System.out.println("++++ 수신 메시지:  비행 중지 필요(STATUS_NEED_STOP_FLYING) 메시지를 수신하였습니다..");
            System.out.println("===================================================================");

            System.out.println("===================================================================");
            System.out.println("++++ 송신 메시지:  비행 중지 필요 상황이 발생하여 " +
                     "비행 중지를 위한 비행 대기 명령(DO_FLYING_WAIT_FOR_STOP_FLYING) 메시지를 송신하였습니다..");
            System.out.println("===================================================================");
            DroneController.droneRunnerRepository.sendMessageToFollower(FlyingMessage.DO_FLYING_WAIT_FOR_STOP_FLYING);
        }


        /**
         * 특정 팔로워로부터 비행 일시 중지 상태를 알리는 메시지를 전송 받았을 때,
         * - 해당 팔로워의 비행을 중지한다.
         */
        if(flyingMessage == FlyingMessage.STATUS_FLYING_WAITED_FOR_STOP_FLYING){

            if(DroneRunnerRepository.messageFlyingWaitedCount == DroneController.droneRunnerRepository.size()){


                Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();
                DroneRunner droneRunnerNewLeader = null;
                while(iterator.hasNext()){
                    DroneRunner droneRunner = iterator.next();
                    Drone drone = droneRunner.getDrone();
                    String leaderOrFollower = drone.getLeaderOrFollower();
                    FlyingInfo flyingInfo = drone.getFlyingInfo();
                    double totalErrorPoint = flyingInfo.getFinalFlightStatus().getTotalErrorPoint();

                    System.out.println("===================================================================");
                    System.out.println("#### Drone 이름: " + drone.getName());
                    System.out.println("#### 리더 여부: " + drone.getLeaderOrFollower());
                    System.out.println("###### 누적 업데이트 된 최종 장애 정보 출력..");
                    System.out.println("TRIVIAL: " + flyingInfo.getFinalFlightStatus().getTrivialList().size());
                    System.out.println("MINOR: " + flyingInfo.getFinalFlightStatus().getMinorList().size());
                    System.out.println("MAJOR: " + flyingInfo.getFinalFlightStatus().getMajorList().size());
                    System.out.println("CRITICAL: " + flyingInfo.getFinalFlightStatus().getCriticalList().size());
                    System.out.println("BLOCK: " + flyingInfo.getFinalFlightStatus().getBlockList().size());
                    System.out.println("Total Error Point: " + flyingInfo.getFinalFlightStatus().getTotalErrorPoint());
                    System.out.println("===================================================================");

                    if(!leaderOrFollower.equals("L")){
                        if(droneRunnerNewLeader == null){
                            droneRunnerNewLeader = droneRunner;
                            continue;
                        }

                        if(totalErrorPoint < droneRunnerNewLeader.getDrone().getFlyingInfo().getFinalFlightStatus().getTotalErrorPoint()){
                            droneRunnerNewLeader = droneRunner;
                            continue;
                        }
                    }

                }

                /**
                 *  비행 중지를 위한 비행 대기준인 팔로워의 처리
                 *  - finalDroneInfoList에 비행정보 저장.
                 *  - 비행 프로세스 종료 메시지 전송.
                 *  - DroneRunnerRepository에서 제거
                 */
                this.doStopFollowerFlight();
            }
        }

    }

    private void doStopLeaderFlight(){
        /**
         *  기존 리더의 처리
         *  - finalDroneInfoList에 비행정보 저장.
         *  - 비행 프로세스 종료 메시지 전송.
         *  - DroneRunnerRepository에서 제거
         */
        Iterator<DroneRunner> iterator2 = DroneController.droneRunnerRepository.iterator();
        while(iterator2.hasNext()) {
            DroneRunner droneRunner = iterator2.next();
            Drone drone = droneRunner.getDrone();
            String leaderOrFollower = drone.getLeaderOrFollower();

            if(leaderOrFollower.equals("L")){
                System.out.println("===================================================================");
                System.out.println("## 기존 Leader의 비행정보를 저장하고, 비행을 중단합니다.");
                System.out.println("===================================================================");
                System.out.println("++++ 송신 메시지: " + drone.getName() + "에게 비행 중지 명령(DO_FLYING_STOP) 메시지를 송신하였습니다..");
                System.out.println("===================================================================");

                finalDroneInfoList.add(drone);
                DroneController.droneRunnerRepository.sendMessageToLeader(FlyingMessage.DO_FLYING_STOP);
                DroneController.droneRunnerRepository.removeDroneRunner(droneRunner);

                break;
            }

        }
    }

    /**
     *  특정 1대의 팔로워에 대한 비행 중지 처리
     *
     *  - finalDroneInfoList에 비행정보 저장.
     *  - 비행 프로세스 종료 메시지 전송.
     *  - DroneRunnerRepository에서 제거
     */
    private void doStopFollowerFlight(){
        /**
         *  특정 1대의 팔로워에 대한 비행 중지 처리
         *
         *  - finalDroneInfoList에 비행정보 저장.
         *  - 비행 프로세스 종료 메시지 전송.
         *  - DroneRunnerRepository에서 제거
         */
        Iterator<DroneRunner> iterator2 = DroneController.droneRunnerRepository.iterator();
        while(iterator2.hasNext()) {
            DroneRunner droneRunner = iterator2.next();
            Drone drone = droneRunner.getDrone();
            String leaderOrFollower = drone.getLeaderOrFollower();
            FlyingInfo flyingInfo = drone.getFlyingInfo();

            if(leaderOrFollower.equals("F") && (flyingInfo.getMessage() == FlyingMessage.STATUS_FLYING_WAITED_FOR_STOP_FLYING)){
                System.out.println("===================================================================");
                System.out.println("## 기존 팔로워의 비행정보를 저장하고, 비행을 중단합니다.");
                System.out.println("===================================================================");
                System.out.println("++++ 송신 메시지: " + drone.getName() + "에게 비행 중지 명령(DO_FLYING_STOP) 메시지를 송신하였습니다..");
                System.out.println("===================================================================");

                finalDroneInfoList.add(drone);
                DroneController.droneRunnerRepository.sendMessageToLeader(FlyingMessage.DO_FLYING_STOP);
                DroneController.droneRunnerRepository.removeDroneRunner(droneRunner);

                break;
            }

        }
    }

}
