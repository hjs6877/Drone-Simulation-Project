package kr.co.korea.runner;

import kr.co.korea.DroneController;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingInfo;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.repository.DroneRunnerRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ideapad on 2016-02-11.
 */
public class DroneRunnerOrig extends Thread {
    private boolean flag = false;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Drone drone;

    public DroneRunnerOrig(Socket socket) throws IOException {
        this.socket = socket;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run(){
        try{
            while(objectInputStream != null){   // TODO ObjectInputStream이 null인걸로 판단하도록 수정.
                Object object =  objectInputStream.readObject();
                drone = (Drone) object;
                if(drone != null){
                    FlyingMessage flyingMessage = drone.getFlyingInfo().getMessage();


                    //  TODO 메시지에 따라서 DroneRunnerRepository의 메시지 호출 메서드가 달라짐.

                    /**
                     *  드론 클라이언트 접속 시, 전달 되는 메시지.
                     *  - 접속 확인 용.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_READY){
//                        System.err.println(drone.getName() + " 비행 준비 완료..");
                    }


                    /**
                     * 장애로 인해 리더 교체 메시지를 리더로부터 전송 받았을 때,
                     * - 비행중인 팔로워들에게 비행 대기 메시지를 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_NEED_REPLACE_LEADER){
                        System.out.println("++++ 송신 메시지:  리더 교체 필요 상황이 발생하여 팔로워들을 비행 대기 상태로 전환합니다..");
//                        DroneController.droneRunnerRepository.sendMessageToFollowers(FlyingMessage.DO_FLYING_WAIT);
                        DroneController.droneRunnerRepository.sendMessageToAll(FlyingMessage.DO_FLYING_WAIT);
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
                    if(flyingMessage == FlyingMessage.STATUS_ELECTED_NEW_LEADER){
                        DroneController.droneRunnerRepository.sendMessageToFollowers(FlyingMessage.DO_FLYING_RESUME);
                    }

                    /**
                     * 팔로워들로부터 목적지에 도착했다는 메시지를 전송 받았을 때,
                     * - DroneRunnerRepository에 저장된 DroneRunner 갯수만큼 메시지를 받게되었을 때,
                     *      - DroneRunner로 부터 drone 객체를 가져와서 비행 결과에 대한 통계를 낸다.
                     *      - 통계 출력이 끝나면 모든 Drone 들에게 비행 종료 메시지를 전송한다.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_ARRIVED){
                        DroneRunnerRepository.messageFlyingArrivedCount++;
                        System.out.println(drone.getName() + "이(가) 목적지에 도착했습니다.");
                        if(DroneRunnerRepository.messageFlyingArrivedCount == DroneController.droneRunnerRepository.size()){
                            /**
                             *  TODO 비행 결과에 대한 통계를 낸다.
                             *  - 최종 비행 성공 Drone 댓 수.
                             *  - 리더 교체 횟수. TODO
                             *  - 최종 비행에 성공한 Drone의 최종 비행 정보.
                             *      - Drone 이름.
                             *      - 최종 비행 메시지.
                             *      - 최종 비행 좌표.
                             *      - 최종 비행 시간.
                             *      - 비행 잔여 거리.
                             */

                            System.out.println("#### 최종 비행 성공 Drone 댓 수: " + DroneController.droneRunnerRepository.size() + "대");
                            System.out.println("#### 최종 비행 정보 ####");

//                            Iterator<DroneRunnerOrig> iterator = DroneController.droneRunnerRepository.iterator();
//                            while(iterator.hasNext()){
//                                DroneRunnerOrig droneRunner = iterator.next();
//                                Drone drone = droneRunner.getDrone();
//                                FlyingInfo flyingInfo = drone.getFlyingInfo();
//                                FlyingMessage finalflyingMessage = flyingInfo.getMessage();
//                                Map<String, Double> coordinationMapAtArraivedSeconds = flyingInfo.getFinalCoordination();
//                                double longitude = coordinationMapAtArraivedSeconds.get("longitude");
//                                double latitude = coordinationMapAtArraivedSeconds.get("latitude");
//                                long finalFlightTime = flyingInfo.getFinalFlightTime();
//                                double remainDistance = flyingInfo.getRemainDistance();
//
//                                System.out.println("#### Drone 이름: " + drone.getName());
//                                System.out.println("#### 최종 비행 메시지: " + finalflyingMessage);
//                                System.out.println("#### 최종 비행 좌표: " + longitude + ", " + latitude);
//                                System.out.println("#### 최종 비행 시간: " + finalFlightTime);
//                                System.out.println("#### 최종 잔여 비행 거리: " + remainDistance);
//                                System.out.println("==========================================");
//                            }

                            DroneController.droneRunnerRepository.sendMessageToAll(FlyingMessage.DO_FLYING_FINISH);
                        }

                    }

                }else{
                    this.flag = true;
                }
            }

//            DroneController.droneRunnerRepository.removeDroneRunner(this);


        } catch (SocketException se){
            System.err.println(se.getMessage());
            try {
                if(objectOutputStream != null) objectOutputStream.close();
                if(objectInputStream != null) objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(FlyingMessage flyingMessage) throws IOException {
        drone.getFlyingInfo().setMessage(flyingMessage);
        System.out.println("drone 이름1: " + drone.getName() + "-->" + drone.getFlyingInfo().getMessage());
        this.objectOutputStream.writeObject(drone);
    }

    public Drone getDrone() {
        return drone;
    }

    public String toString(){
        return socket.toString();
    }
}
