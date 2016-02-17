package kr.co.korea.repository;

import kr.co.korea.DroneController;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.runner.DroneRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by ideapad on 2016-02-11.
 */
public class DroneRunnerRepository extends Vector<DroneRunner> {
    public static int messageFlyingArrivedCount = 0;
    public static int messageFlyingWaitedCount = 0;

    public synchronized void addDroneRunner(DroneRunner droneRunner) {
        this.addElement(droneRunner);
    }

    public synchronized void removeDroneRunner(DroneRunner droneRunner){
        if(this.contains(droneRunner)){
            this.remove(droneRunner);
        }
    }

    public synchronized void sendMessageToAll(FlyingMessage flyingMessage){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            try{
                Drone drone = droneRunner.getDrone();
                drone.getFlyingInfo().setMessage(flyingMessage);
                droneRunner.setDrone(drone);

                droneRunner.sendDrone(drone);

            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }

    }

    public synchronized void sendMessageToLeader(FlyingMessage flyingMessage){
//        Iterator<DroneRunner> iterator = this.iterator();
//        while (iterator.hasNext()){
//            DroneRunner droneRunner = iterator.next();
//            try{
//                Drone drone = droneRunner.getDrone();
//                if(drone.getLeaderOrFollower().equals("L")){
//                    droneRunner.sendMessageOrDrone(flyingMessage);
//                }
//
//            }catch (IOException ex){
//                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
//            }
//        }
    }

    public synchronized void sendMessageToFollowers(FlyingMessage flyingMessage){
//        Iterator<DroneRunner> iterator = this.iterator();
//        while (iterator.hasNext()){
//            DroneRunner droneRunner = iterator.next();
//            try{
//                Drone drone = droneRunner.getDrone();
//                if(drone.getLeaderOrFollower().equals("F")){
//                    droneRunner.sendMessageOrDrone(flyingMessage);
//                }
//            }catch (IOException ex){
//                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
//            }
//        }
    }

    /**
     * 해당 메시지를 가지는 특정 Drone 에게 메시지 전송.
     *
     * @param flyingMessageForMatch
     * @param flyingMessageForOrder
     */
    public synchronized DroneRunner sendMessageToDrone(FlyingMessage flyingMessageForMatch, FlyingMessage flyingMessageForOrder) {
//        DroneRunner matchedDroneRunner = null;
//
//        Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();
//
//        while(iterator.hasNext()){
//            DroneRunner droneRunner = iterator.next();
//            Drone drone = droneRunner.getDrone();
//            String leaderOrFollower = drone.getLeaderOrFollower();
//            FlyingInfo flyingInfo = drone.getFlyingInfo();
//
//            try {
//                // TODO 비행 종료 시, 이 부분이 매치 되지 않는 부분을 해결해야 함. DroneControllerServer 241 line과 연관 됨.
//                if(flyingMessageForMatch == flyingInfo.getMessage()){
//                    droneRunner.sendMessageOrDrone(flyingMessageForOrder);
//                    matchedDroneRunner = droneRunner;
//                    break;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return null; //matchedDroneRunner;
    }
}