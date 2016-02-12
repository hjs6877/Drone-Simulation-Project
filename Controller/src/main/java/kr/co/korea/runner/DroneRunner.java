package kr.co.korea.runner;

import kr.co.korea.DroneController;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-02-11.
 */
public class DroneRunner extends Thread {
    private boolean flag = false;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Drone drone;

    public DroneRunner(Socket socket) throws IOException {
        this.socket = socket;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run(){
        try{
            while(!this.flag){
                Object object =  objectInputStream.readObject();
                Drone drone = (Drone) object;
                if(drone != null){
                    FlyingMessage flyingMessage = drone.getFlyingInfo().getMessage();


                    //  TODO 메시지에 따라서 DroneRunnerRepository의 메시지 호출 메서드가 달라짐.

                    /**
                     *  드론 클라이언트 접속 시, 전달 되는 메시지.
                     *  - 접속 확인 용.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_READY){
                        System.out.println(drone.getName() + " 비행 준비 완료..");
                        DroneController.droneRunnerRepository.addDroneRunner(this);
                    }

                    /**
                     * 컨트롤러로부터 비행 설정 완료 메시지를 전송 받았을 때,
                     * - 모든 팔로워들에게 비행 시작 메시지를 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_SET_COMPLETE){
                        DroneController.droneRunnerRepository.sendMessageToFollowers(FlyingMessage.DO_FLYING_START);
                    }

                    /**
                     * 장애로 인해 리더 교체 메시지를 리더로부터 전송 받았을 때,
                     * - 비행중인 팔로워들에게 비행 대기 메시지를 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_NEED_REPLACE_LEADER){
                        DroneController.droneRunnerRepository.sendMessageToFollowers(FlyingMessage.DO_FLYING_WAIT);
                    }

                    /**
                     * 팔로워들로부터 비행 일시 중지 상태를 알리는 메시지를 전송 받았을 때,
                     * - 모든 팔로워들로부터 메시지를 전송 받은 후, 리더에게 신규 리더 선출하라는 메시지를 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_WAITED){
                        // TODO 모든 팔로워들로부터 메시지를 전송 받았는지 확인.
                        DroneController.droneRunnerRepository.sendMessageToLeader(FlyingMessage.DO_ELECT_NEW_LEADER);
                    }

                    /**
                     * 리더로부터 새로운 리더가 선출 되었다는 메시지를 전송 받았을 때,
                     * - 모든 팔로워들에게 비행 재개 메시지를 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.DO_FLYING_RESUME){
                        DroneController.droneRunnerRepository.sendMessageToFollowers(FlyingMessage.DO_FLYING_RESUME);
                    }

                }else{
                    this.flag = true;
                }
            }

            DroneController.droneRunnerRepository.removeDroneRunner(this);
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(FlyingMessage flyingMessage) throws IOException {
        drone.getFlyingInfo().setMessage(flyingMessage);

        this.objectOutputStream.writeObject(drone);
        this.objectOutputStream.flush();
    }

    public Drone getDrone() {
        return drone;
    }

    public String toString(){
        return socket.toString();
    }
}
