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

    public synchronized void removeAllDroneRunner(){
        this.clear();
    }

    public synchronized void sendMessageToAllClientFromController(FlyingMessage flyingMessage){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            try{

                Drone droneToClient = droneRunner.getDroneToClient();
                droneToClient.getFlyingInfo().setMessage(flyingMessage);
                droneRunner.sendDroneToClient(droneToClient);

            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }

    }

    public synchronized void sendMessageToAllClient(FlyingMessage flyingMessage){
        Iterator<DroneRunner> iterator = this.iterator();
        while (iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            try{

                Drone droneFromClient = droneRunner.getDroneFromClient();
                droneFromClient.getFlyingInfo().setMessage(flyingMessage);

                Drone droneToClient = droneFromClient;

                droneRunner.sendDroneToClient(droneToClient);

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
                Drone droneFromClient = droneRunner.getDroneFromClient();
                droneFromClient.getFlyingInfo().setMessage(flyingMessage);
                Drone droneToClient = droneFromClient;

                if(droneFromClient.getLeaderOrFollower().equals("L")){
                    droneRunner.sendDroneToClient(droneToClient);
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
                Drone droneFromClient = droneRunner.getDroneFromClient();
                droneFromClient.getFlyingInfo().setMessage(flyingMessage);
                Drone droneToClient = droneFromClient;

                if(droneFromClient.getLeaderOrFollower().equals("F")){
                    droneRunner.sendDroneToClient(droneToClient);
                }
            }catch (IOException ex){
                System.out.println(droneRunner.toString() + "의 메시지 전송 에러");
            }
        }
    }

    /**
     * 해당 메시지를 가지는 특정 Drone 에게 메시지 전송.
     *
     * @param flyingMessageForMatch
     * @param flyingMessageForOrder
     */
    public synchronized DroneRunner sendMessageToDrone(FlyingMessage flyingMessageForMatch, FlyingMessage flyingMessageForOrder) {
        DroneRunner matchedDroneRunner = null;

        Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();

        while(iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            Drone droneFromClient = droneRunner.getDroneFromClient();


            FlyingInfo flyingInfo = droneFromClient.getFlyingInfo();

            try {
                // TODO 비행 종료 시, 이 부분이 매치 되지 않는 부분을 해결해야 함. DroneControllerServer 241 line과 연관 됨.
                if(flyingMessageForMatch == flyingInfo.getMessage()){
                    droneFromClient.getFlyingInfo().setMessage(flyingMessageForOrder);
                    Drone droneToClient = droneFromClient;
                    droneRunner.sendDroneToClient(droneToClient);
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