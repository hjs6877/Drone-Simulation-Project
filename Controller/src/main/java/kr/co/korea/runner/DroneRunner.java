package kr.co.korea.runner;

import kr.co.korea.DroneController;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.repository.DroneRunnerRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
            while(objectInputStream != null){
                Object object = objectInputStream.readObject();
                FlyingMessage flyingMessage = null;

                if(object instanceof FlyingMessage){
                    flyingMessage = (FlyingMessage) object;
                }

                if(object instanceof Drone){
                    drone = (Drone) object;
                }

                if(flyingMessage != null){



                    /**
                     *  드론 클라이언트 접속 시, 전달 되는 메시지.
                     *  - 접속 확인 용.
                     */
                    if(flyingMessage == FlyingMessage.STATUS_FLYING_READY){
//                        System.err.println(drone.getName() + " 비행 준비 완료..");
                    }

                    /**
                     *  Drone Client 로부터 전송 받은 메시지 처리를 위해 DroneControllerServer 메시지 전달.
                     */
                    DroneController.droneControllerServerRepository.get(0).setFlyingMessage(flyingMessage);




                    /**
                     * 팔로워들로부터 목적지에 도착했다는 메시지를 전송 받았을 때,
                     * - DroneRunnerRepository에 저장된 DroneRunnerSimpleTest 갯수만큼 메시지를 받게되었을 때,
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

//                            Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();
//                            while(iterator.hasNext()){
//                                DroneRunner droneRunner = iterator.next();
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

    public void sendMessageOrDrone(FlyingMessage flyingMessage) throws IOException {
        this.objectOutputStream.writeObject(drone);
        this.objectOutputStream.writeObject(flyingMessage);
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public String toString(){
        return socket.toString();
    }
}
