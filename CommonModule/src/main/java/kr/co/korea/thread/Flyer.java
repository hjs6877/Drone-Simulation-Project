package kr.co.korea.thread;

import kr.co.korea.domain.*;
import kr.co.korea.error.ErrorType;
import kr.co.korea.util.MathUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ideapad on 2016-01-20.
 */
public class Flyer extends Thread {
    private Socket socket;
    Drone drone;
    TreeMap<Long, ErrorType> errorEventMap;
    DroneSetting setting;
    FlightStatus flightStatus;

    public Flyer(Socket socket) throws IOException {
        flightStatus = new FlightStatus();

        this.socket = socket;
    }

    public void run() {
        /**
         * 비행 시작..
         */
        this.fly();
    }


    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Drone getDrone() {
        return drone;
    }

    /**
     * 해당 드론 프로세스가 리더인지 확인.
     *
     * @return
     */
    private boolean isLeader(){

        return drone.getLeaderOrFollower().equals("L");
    }

    public FlightStatus fly(){
        setting = drone.getDroneSetting();
        errorEventMap = drone.getErrorEvent();

        String departure = setting.getDeparture();
        String destination = setting.getDestination();
        double departureLongitude = setting.getDepartureCoordination().get(departure).getLongitude();
        double departureLatitude = setting.getDepartureCoordination().get(departure).getLatitude();
        double destinationLongitude = setting.getDestinationCoordination().get(destination).getLongitude();
        double destinationLatitude = setting.getDestinationCoordination().get(destination).getLatitude();
        Map<Long, ErrorType> errorEventMap = drone.getErrorEvent();

        long flightTime = setting.getFlightTime();

        String leaderOrFollower = drone.getLeaderOrFollower();

        System.out.println("droneName: " + leaderOrFollower);
        System.out.println("리더 여부: " + drone.getLeaderOrFollower());
        System.out.println("출발지: " + setting.getDeparture());
        System.out.println("목적지: " + setting.getDestination());
        System.out.println("비행시간: " + setting.getFlightTime());
        System.out.println("장애 이벤트: " + errorEventMap);

        FlightStatus status = new FlightStatus();

        int countDown = 10;

        System.out.println("######### Take off and now hovering..");
        try {
            Thread.sleep(3000);

            System.out.println(countDown + "초 후에 비행을 시작합니다..");

            for(long i=countDown; i>0; i--){
                System.out.println(i);
                Thread.sleep(1000);
            }

            int startTime = 1;

            /**
             * 실제 비행하는 부분. 리더일때와 팔로워 일때의 프로세스가 달라야 함.
             * 리더인지를 체크해서 별도 로직을 적용해야 함.
             * // TODO 로깅 필요.
             */
            for(long i=startTime; i<=flightTime; i++){
                Thread.sleep(1000);
                System.out.println("###### " + i + "초 비행");

                Map<String, Double> coordinationMapAtSeconds = MathUtils.calculateCoordinateAtSeconds(departureLongitude, departureLatitude,
                        destinationLongitude, destinationLatitude, flightTime, startTime);

                double longitude = coordinationMapAtSeconds.get("longitude");
                double latitude = coordinationMapAtSeconds.get("latitude");

                /**
                 * 장애 이벤트가 해당 비행 시간(초)에 존재한다면(발생한다면)
                 * - FlightStatus의 발생한 장애 이벤트별 리스트에 저장.
                 * - 장애 이벤트별 업데이트
                 *      ㄴ 장애 이벤트 리스트 별로 발생 빈도수를 확인 한후, 일정 횟수 이상 되면
                 *      상위 장애를 1회 추가 한 후, 해당 장애 clear.
                 * - CRITICAL 또는 BLOCK 장애가 발생했는지 장애 리스트의 빈도수를 확인해서 리더교체가 필요한 장애 상태인지를 확인. TODO
                 *
                 * - 리더교체가 필요한 상태라면(CRITICAL 2회, BLOCK 1회 라면) 리더 교체 프로세스를 시작. TODO
                 *      ㄴ FlyingInfo 객체에 해당 시점까지의 비행 정보를 저장. TODO
                 *      ㄴ 비행 중지. // TODO 쓰레드 중지가 되는지 확인 필요. 안되면 마지막에 프로세스를 죽여야 됨.
                 *      ㄴ 리더 교체 필요 메시지와 FlyingInfo 객체를 Drone 객체에 포함 시켜서 전송. TODO
                 *      ㄴ 로깅. TODO
                 */
                ErrorType errorType = errorEventMap.get(i);
                if(this.isExistErrorEvent(errorType)){
                    System.out.println(i + "초에 에러 이벤트 발생: " + errorType);
                    System.out.println("비행 시 좌표: " + longitude + ", " + latitude);

                    flightStatus.addErrorEvent(errorType);
                    flightStatus.updateErrorEvent();

                    FlyingInfo flyingInfo = drone.getFlyingInfo();

                    /**
                     * 리더 교체가 필요한 장애가 발생했는가?
                     * TODO 여기서부터 리더에 대한 프로세스 진행.
                     */
                    if(leaderOrFollower.equals("L") && flightStatus.hasErrorEventForReplacingLeader()){
                        System.out.println("심각한 장애 발생으로 인해 리더 교체 프로세스 실시!!!!!!!!!!!!!!!!!!!!!!");


                        flyingInfo.setMessage(FlyingMessage.STATUS_NEED_REPLACE_LEADER);
                        flyingInfo.setFlightStatus(flightStatus);
                        // TODO FlyingInfo 객체에 여러가지 정보를 더 담아야 됨.
                        drone.setFlyingInfo(flyingInfo);

                        // TODO 메시지 전송은 ClientSender를 거쳐서 하도록 수정.
//                            objectOutputStream.writeObject(drone);
//                        waitFlight();

                    }else{      /** 팔로워들에게 적용되는 프로세스 **/
                        flyingInfo.setFlightStatus(flightStatus);
                    }



                }

            }

            System.out.println("###### 목적지 도착. Now landing...");
            Thread.sleep(3000);
            System.out.println("###### 비행 종료..");
            System.out.println("###### 누적 업데이트 된 전체 장애 정보 출력..");
            System.out.println("TRIVIAL: " + flightStatus.getTrivialList().size());
            System.out.println("MINOR: " + flightStatus.getMinorList().size());
            System.out.println("MAJOR: " + flightStatus.getMajorList().size());
            System.out.println("CRITICAL: " + flightStatus.getCriticalList().size());
            System.out.println("BLOCK: " + flightStatus.getBlockList().size());
            socket.close();
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return status;
    }

    public synchronized void waitFlight(){
        System.out.println("쓰레드 대기 상태로 진입..");
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isExistErrorEvent(ErrorType errorType) {
        return (errorType != null) && (errorType != ErrorType.NORMAL);
    }

}
