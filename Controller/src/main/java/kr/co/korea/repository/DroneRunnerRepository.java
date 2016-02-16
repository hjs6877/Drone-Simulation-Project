package kr.co.korea.repository;

import kr.co.korea.DroneController;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.domain.FlyingMessageType;
import kr.co.korea.runner.DroneRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by ideapad on 2016-02-11.
 */
public class DroneRunnerRepository extends Vector<DroneRunner> {
    public static int messageFlyingWaitedCount = 0;

    public synchronized void addDroneRunner(DroneRunner droneRunner) {
        this.addElement(droneRunner);
    }

    public synchronized void removeDroneRunner(DroneRunner droneRunner){
        if(this.contains(droneRunner)){
            this.remove(droneRunner);
        }
    }

    public synchronized void sendMessageToAll(FlyingMessageType flyingMessageType){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            Drone drone = droneRunner.getDrone();
            FlyingInfo flyingInfo = drone.getFlyingInfo();
            FlyingMessage flyingMessageNew = flyingInfo.getFlyingMessage();


            flyingMessageNew.setFlyingMessageType(flyingMessageType);
            flyingMessageNew.setDroneName(drone.getName());
            flyingInfo.setFlyingMessage(flyingMessageNew);
            drone.setFlyingInfo(flyingInfo);

            try{
                droneRunner.sendMessageOrDrone(flyingMessageNew);

            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }

    }

    public synchronized void sendMessageToLeader(FlyingMessageType flyingMessageType){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            try{
                Drone drone = droneRunner.getDrone();
                if(drone.getLeaderOrFollower().equals("L")){
                    FlyingInfo flyingInfo = drone.getFlyingInfo();
                    FlyingMessage flyingMessageNew = flyingInfo.getFlyingMessage();


                    flyingMessageNew.setFlyingMessageType(flyingMessageType);
                    flyingMessageNew.setDroneName(drone.getName());
                    flyingInfo.setFlyingMessage(flyingMessageNew);
                    drone.setFlyingInfo(flyingInfo);

                    droneRunner.sendMessageOrDrone(flyingMessageNew);
                }

            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }
    }

    public synchronized void sendMessageToFollowers(FlyingMessageType flyingMessageType){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            try{
                Drone drone = droneRunner.getDrone();
                if(drone.getLeaderOrFollower().equals("F")){
                    FlyingInfo flyingInfo = drone.getFlyingInfo();
                    FlyingMessage flyingMessageNew = flyingInfo.getFlyingMessage();


                    flyingMessageNew.setFlyingMessageType(flyingMessageType);
                    flyingMessageNew.setDroneName(drone.getName());
                    flyingInfo.setFlyingMessage(flyingMessageNew);
                    drone.setFlyingInfo(flyingInfo);

                    droneRunner.sendMessageOrDrone(flyingMessageNew);
                }
            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }
    }

    /**
     * 해당 메시지를 가지는 특정 팔로워에게 메시지 전송.
     *
     * @param flyingMessageTypeForMatch
     * @param flyingMessageTypeForOrder
     */
    public synchronized DroneRunner sendMessageToDrone(FlyingMessageType flyingMessageTypeForMatch, FlyingMessageType flyingMessageTypeForOrder) {
        DroneRunner matchedDroneRunner = null;

        Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();

        while(iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            Drone drone = droneRunner.getDrone();
            FlyingInfo flyingInfo = drone.getFlyingInfo();
            FlyingMessage flyingMessage = flyingInfo.getFlyingMessage();
            FlyingMessageType flyingMessageType = flyingMessage.getFlyingMessageType();

            try {
                if(flyingMessageTypeForMatch == flyingMessageType){
                    flyingMessage.setFlyingMessageType(flyingMessageTypeForOrder);
                    droneRunner.sendMessageOrDrone(flyingMessage);
                    matchedDroneRunner = droneRunner;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return matchedDroneRunner;
    }
}
