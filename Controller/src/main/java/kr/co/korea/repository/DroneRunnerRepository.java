package kr.co.korea.repository;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.runner.DroneRunner;
import kr.co.korea.runner.DroneRunnerSimpleTest;

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
                droneRunner.sendMessageOrDrone(flyingMessage);

            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }

    }

    public synchronized void sendMessageToLeader(FlyingMessage flyingMessage){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            try{
                Drone drone = droneRunner.getDrone();
                if(drone.getLeaderOrFollower().equals("L")){
                    droneRunner.sendMessageOrDrone(flyingMessage);
                }

            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }
    }

    public synchronized void sendMessageToFollowers(FlyingMessage flyingMessage){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            try{
                Drone drone = droneRunner.getDrone();
                if(drone.getLeaderOrFollower().equals("F")){
                    droneRunner.sendMessageOrDrone(flyingMessage);
                }
            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }
    }
}
