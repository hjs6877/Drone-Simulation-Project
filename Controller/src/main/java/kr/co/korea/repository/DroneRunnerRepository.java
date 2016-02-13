package kr.co.korea.repository;

import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.runner.DroneRunnerSimpleTest;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by ideapad on 2016-02-11.
 */
public class DroneRunnerRepository extends Vector<DroneRunnerSimpleTest> {
    public static int messageFlyingArrivedCount = 0;

    public synchronized void addDroneRunner(DroneRunnerSimpleTest droneRunnerSimpleTest) {
        this.addElement(droneRunnerSimpleTest);
    }

    public synchronized void removeDroneRunner(DroneRunnerSimpleTest droneRunnerSimpleTest){
        if(this.contains(droneRunnerSimpleTest)){
            this.remove(droneRunnerSimpleTest);
        }
    }

    public synchronized void sendMessageToAll(FlyingMessage flyingMessage){
        Iterator<DroneRunnerSimpleTest> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunnerSimpleTest droneRunnerSimpleTest = iterator.next();
            try{
                droneRunnerSimpleTest.sendMessage(flyingMessage);

            }catch (IOException ex){
                System.out.println(droneRunnerSimpleTest.toString() + "의 메시지 전송 에러");
            }
        }

    }

    public synchronized void sendMessageToLeader(FlyingMessage flyingMessage){
        Iterator<DroneRunnerSimpleTest> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunnerSimpleTest droneRunnerSimpleTest = iterator.next();
            try{
                Drone drone = droneRunnerSimpleTest.getDrone();
                if(drone.getLeaderOrFollower().equals("L")){
                    droneRunnerSimpleTest.sendMessage(flyingMessage);
                }

            }catch (IOException ex){
                System.out.println(droneRunnerSimpleTest.toString() + "의 메시지 전송 에러");
            }
        }
    }

    public synchronized void sendMessageToFollowers(FlyingMessage flyingMessage){
        Iterator<DroneRunnerSimpleTest> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunnerSimpleTest droneRunnerSimpleTest = iterator.next();
            try{
                Drone drone = droneRunnerSimpleTest.getDrone();
                if(drone.getLeaderOrFollower().equals("F")){
                    droneRunnerSimpleTest.sendMessage(flyingMessage);
                }
            }catch (IOException ex){
                System.out.println(droneRunnerSimpleTest.toString() + "의 메시지 전송 에러");
            }
        }
    }
}
