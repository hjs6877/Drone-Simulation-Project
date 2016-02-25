package kr.co.korea.thread;

import kr.co.korea.domain.*;
import kr.co.korea.error.ErrorType;
import kr.co.korea.util.DateUtils;
import kr.co.korea.util.FlightRecorder;
import kr.co.korea.util.MathUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

/**
 * 비행을 처리하는 쓰레드
 */
public class Flyer extends Thread {
    public FlyingMessage DO_FLYING_WAIT = null;
    private Socket socket;
    private ClientSender clientSender;
    private Drone droneToController;
    private Drone droneFromController;
    TreeMap<Long, ErrorType> errorEventMap;
    DroneSetting setting;
    private FlightRecorder flightRecorder;
    private String fileName;

    public Flyer(Socket socket, ClientSender clientSender) throws IOException {
        this.socket = socket;
        this.clientSender = clientSender;
    }

    public void run() {

        /**
         * 비행 시작..
         */
        this.fly();
    }


    public void setDrone(Drone drone) {

        droneFromController = drone;
    }

    public Drone getDrone() {
        return droneToController;
    }

    /**
     * 해당 드론 프로세스가 리더인지 확인.
     *
     * @return
     */
    private boolean isLeader(){

        return droneFromController.getLeaderOrFollower().equals("L");
    }

    public void fly() {
        fileName = this.createFileName();

        try {
            flightRecorder = new FlightRecorder(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String droneName = droneFromController.getName();
        String defaultLeaderOrFollower = droneFromController.getLeaderOrFollower();
        setting = droneFromController.getDroneSetting();
        errorEventMap = droneFromController.getErrorEvent();

        String departure = setting.getDeparture();
        String destination = setting.getDestination();
        double distance = setting.getDistance();
        double departureLongitude = setting.getDepartureCoordination().get(departure).getLongitude();
        double departureLatitude = setting.getDepartureCoordination().get(departure).getLatitude();
        double destinationLongitude = setting.getDestinationCoordination().get(destination).getLongitude();
        double destinationLatitude = setting.getDestinationCoordination().get(destination).getLatitude();
        Map<Long, ErrorType> errorEventMap = droneFromController.getErrorEvent();

        long flightTime = setting.getFlightTime();
        int speed = setting.getSpeed();

        System.out.println("===================================================================");
        System.out.println("droneName: " + droneName);
        System.out.println("초기 리더 여부: " + defaultLeaderOrFollower);
        System.out.println("출발지: " + setting.getDeparture());
        System.out.println("목적지: " + setting.getDestination());
        System.out.println("비행시간: " + setting.getFlightTime());
        System.out.println("장애 이벤트: " + errorEventMap);
        System.out.println("===================================================================");


        int countDown = 3;

        System.out.println("===================================================================");
        System.out.println("## Take off and now hovering..");
        System.out.println("===================================================================");

        try {
            Thread.sleep(3000);

            System.out.println("===================================================================");
            System.out.println(countDown + "초 후에 비행을 시작합니다..");
            System.out.println("===================================================================");

            for(long i=countDown; i>0; i--){
                System.out.println(i);
                Thread.sleep(1000);
            }

            int startTime = 1;

            FlyingInfo flyingInfo = droneFromController.getFlyingInfo();
            FlightStatus flightStatus = flyingInfo.getFinalFlightStatus();

            String flightStartDate = DateUtils.getCurrentDateDefaultFormatted();


            flightRecorder.writeFlyingInfoFileHeader();



            /**
             * 실제 비행하는 부분. 리더일때와 팔로워 일때의 프로세스가 달라야 함.
             * 리더인지를 체크해서 별도 로직을 적용해야 함.
             * // TODO 로깅 필요.
             */
            for(long atSeconds=startTime; atSeconds<=flightTime; atSeconds++){

                String currentLeaderOrFollower = droneFromController.getLeaderOrFollower();

                Map<String, Double> coordinationMapAtSeconds = MathUtils.calculateCoordinateAtSeconds(departureLongitude, departureLatitude,
                        destinationLongitude, destinationLatitude, flightTime, atSeconds);
                ErrorType ErrorType = errorEventMap.get(atSeconds) != null ? errorEventMap.get(atSeconds) : kr.co.korea.error.ErrorType.NORMAL;

                boolean isExistErrorEvent = this.isExistErrorEvent(ErrorType);

                double longitudeAtSeconds = coordinationMapAtSeconds.get("longitude");
                double latitudeAtSeconds = coordinationMapAtSeconds.get("latitude");
                double remainDistance = this.calculateRemainDistance(setting, atSeconds);

                /**
                 * 비행 시작 일시, Drone 이름, 초기 리더/팔로워 구분, 현재 시점별 리더/팔로워 구분,
                 * 비행 속도, 비행 시점(초), 시점별 비행 좌표(경/위도), 장애 발생 유무, 장애 타입, 잔여 거리
                 */
                String flightInfo = flightStartDate     + FlightRecorder.COMMA +
                        droneName                       + FlightRecorder.COMMA +
                        defaultLeaderOrFollower         + FlightRecorder.COMMA +
                        currentLeaderOrFollower         + FlightRecorder.COMMA +
                        speed                           + FlightRecorder.COMMA +
                        atSeconds                       + FlightRecorder.COMMA +
                        longitudeAtSeconds              + FlightRecorder.COMMA +
                        latitudeAtSeconds               + FlightRecorder.COMMA +
                        isExistErrorEvent               + FlightRecorder.COMMA +
                        ErrorType + FlightRecorder.COMMA +
                        distance                        + FlightRecorder.COMMA +
                        remainDistance;

                System.out.println("## " + atSeconds + "초 비행");
                System.out.println("## 현재 리더 여부: " + currentLeaderOrFollower);

                /**
                 * 비행 기록.
                 */
                flightRecorder.writeToFile(flightInfo, true);

                /**
                 * 비행 대기 명령이 할당 되었을 때, 비행을 일시 중지한다.
                 *
                 */
                if(DO_FLYING_WAIT == FlyingMessage.DO_FLYING_WAIT_FOR_REPLACE_LEADER
                        || DO_FLYING_WAIT == FlyingMessage.DO_FLYING_WAIT_FOR_STOP_FLYING){
                    System.out.println("===================================================================");
                    System.out.println("## 비행 대기상태로 전환합니다..");
                    System.out.println("===================================================================");

                    this.waitFlight();
                }




                /**
                 * 장애 이벤트가 해당 비행 시간(초)에 존재한다면(발생한다면)
                 * - FlightStatus의 발생한 장애 이벤트별 리스트에 저장.
                 * - 장애 이벤트별 업데이트
                 *      ㄴ 장애 이벤트 리스트 별로 발생 빈도수를 확인 한후, 일정 횟수 이상 되면
                 *      상위 장애를 1회 추가 한 후, 해당 장애 clear.
                 * - CRITICAL 또는 BLOCK 장애가 발생했는지 장애 리스트의 빈도수를 확인해서 리더교체가 필요한 장애 상태인지를 확인.
                 *
                 * - 리더교체가 필요한 상태라면(CRITICAL 2회, BLOCK 1회 라면) 리더 교체 프로세스를 시작. TODO
                 *      ㄴ FlyingInfo 객체에 해당 시점까지의 비행 정보를 저장. TODO
                 *      ㄴ 비행 중지. // TODO 쓰레드 중지가 되는지 확인 필요. 안되면 마지막에 프로세스를 죽여야 됨.
                 *      ㄴ 리더 교체 필요 메시지와 FlyingInfo 객체를 Drone 객체에 포함 시켜서 전송. TODO
                 *      ㄴ 로깅. TODO
                 */

                if(isExistErrorEvent){
                    System.out.println(atSeconds + "초에 에러 이벤트 발생: " + ErrorType);
                    System.out.println("비행 시 좌표: " + longitudeAtSeconds + ", " + latitudeAtSeconds);

                    flightStatus.addErrorEvent(ErrorType);
                    flightStatus.updateErrorEvent();
                    flightStatus.setTotalErrorPoint();

                    flyingInfo.setFinalFlightStatus(flightStatus);

                    /**
                     * 리더 교체가 필요한 장애가 발생했다면??
                     * - 'STATUS_NEED_REPLACE_LEADER' 메시지 전송.
                     * - 현재까지의 비행 정보를 컨트롤러에게 전송.
                     * - 비행 대기 상태로 전환.
                     * TODO 여기서부터 리더에 대한 프로세스 진행.
                     */
                    if(droneFromController.getLeaderOrFollower().equals("L") && flightStatus.hasThreshholdErrorEvent()) {
                        System.out.println("===================================================================");
                        System.out.println("## 심각한 장애 발생으로 인해 리더 교체 프로세스를 실시합니다!!!!!!!");
                        System.out.println("===================================================================");

                        flyingInfo.setMessage(FlyingMessage.STATUS_NEED_REPLACE_LEADER);
                        flyingInfo.setFinalCoordination(coordinationMapAtSeconds);
                        flyingInfo.setFinalFlightTime(atSeconds);
                        flyingInfo.setRemainDistance(remainDistance);

                        droneFromController.setFlyingInfo(flyingInfo);

                        droneToController = droneFromController;

                        System.out.println("===================================================================");
                        System.out.println("++++ 송신 메시지: 리더 교체 필요(STATUS_NEED_REPLACE_LEADER) 메시지를 송신하였습니다..");
                        System.out.println("===================================================================");

                        /** 리더 교체 필요 메시지 전송 **/
                        clientSender.sendDroneToController(droneToController);

                    } else if (flightStatus.hasThreshholdErrorEvent()){      /** 팔로워들에게 적용되는 프로세스 **/
                        System.out.println("===================================================================");
                        System.out.println("## 심각한 장애 발생으로 인해 팔로워 비행을 중단합니다.");
                        System.out.println("===================================================================");


                        flyingInfo.setMessage(FlyingMessage.STATUS_NEED_STOP_FLYING);
                        flyingInfo.setFinalCoordination(coordinationMapAtSeconds);
                        flyingInfo.setFinalFlightTime(atSeconds);
                        flyingInfo.setRemainDistance(remainDistance);

                        droneFromController.setFlyingInfo(flyingInfo);
                        droneToController = droneFromController;

                        System.out.println("===================================================================");
                        System.out.println("++++ 송신 메시지: 비행 중지 필요(STATUS_NEED_STOP_FLYING) 메시지를 송신하였습니다..");
                        System.out.println("===================================================================");

                        /**
                         * 팔로워 비행 중지 필요 메시지 및 비행 정보 전송
                         * TODO 해당 팔로워만 DronRunnerRepository에서 선택하기 위해서는 STATUS_NEED_STOP_FLYING 메시지를 포함한
                         * TODO drone 객체를 함께 전송해주어야 한다.
                         **/
                        clientSender.sendDroneToController(droneToController);
                    }

                }else{  /** 장애가 발생하지 않았을 경우 **/
                    flyingInfo.setFinalFlightStatus(flightStatus);
                    droneToController = droneFromController;
                }


                // 비행을 위한 1초 딜레이
                Thread.sleep(1000);
            }  // flight loop

            flightRecorder.close();

            System.out.println("===================================================================");
            System.out.println("## 목적지 도착. 착륙 대기중..");
            System.out.println("===================================================================");

            Map<String, Double> coordinationMapAtArraivedSeconds = MathUtils.calculateCoordinateAtSeconds(departureLongitude, departureLatitude,
                    destinationLongitude, destinationLatitude, flightTime, flightTime);

            double longitude = coordinationMapAtArraivedSeconds.get("longitude");
            double latitude = coordinationMapAtArraivedSeconds.get("latitude");

            double remainDistance = MathUtils.calculateDistanceByLngLat(longitude, latitude, longitude, latitude);

            /**
             * - 최종 비행 메시지 저장.
             * - 최종 장애 상태 저장.
             * - 최종 비행 좌표 저장.
             * - 최종 비행 시간 저장.
             * - 비행 잔여 거리 저장.
             */
            flyingInfo.setMessage(FlyingMessage.STATUS_FLYING_WAITED_FOR_FINISH_FLYING);
            flyingInfo.setFinalFlightStatus(flightStatus);
            flyingInfo.setFinalCoordination(coordinationMapAtArraivedSeconds);
            flyingInfo.setFinalFlightTime(flightTime);
            flyingInfo.setRemainDistance(remainDistance);

            droneFromController.setFlyingInfo(flyingInfo);
            droneToController = droneFromController;


            System.out.println("===================================================================");
            System.out.println("++++ 송신 메시지:  비행 종료를 위한 대기 (STATUS_FLYING_WAITED_FOR_FINISH_FLYING) 메시지를 송신하였습니다..");
            System.out.println("===================================================================");

            System.out.println("===================================================================");
            System.out.println("## 도착 비행 정보..");
            System.out.println("최종 좌표: " + coordinationMapAtArraivedSeconds.get("longitude") + ", " + coordinationMapAtArraivedSeconds.get("latitude"));
            System.out.println("최종 비행 시간: " + flightTime + "초");
            System.out.println("최종 비행 잔여 거리: " + remainDistance);
            System.out.println("===================================================================");
            System.out.println("###### 누적 업데이트 된 최종 장애 정보 출력..");
            System.out.println("TRIVIAL: " + flightStatus.getTrivialList().size());
            System.out.println("MINOR: " + flightStatus.getMinorList().size());
            System.out.println("MAJOR: " + flightStatus.getMajorList().size());
            System.out.println("CRITICAL: " + flightStatus.getCriticalList().size());
            System.out.println("BLOCK: " + flightStatus.getBlockList().size());
            System.out.println("===================================================================");





            /** 비행 도착 메시지 및 비행 정보 전송 */
            clientSender.sendDroneToController(droneToController);



        } catch (InterruptedException e) {
            e.printStackTrace();
        } //catch (IOException e) {
        catch (IOException e) {
            e.printStackTrace();
        }
//            e.printStackTrace();
//        }

    }


    private double calculateRemainDistance(DroneSetting setting, long atSeconds) {
        String departure = setting.getDeparture();
        String destination = setting.getDestination();
        double departureLongitude = setting.getDepartureCoordination().get(departure).getLongitude();
        double departureLatitude = setting.getDepartureCoordination().get(departure).getLatitude();
        double destinationLongitude = setting.getDestinationCoordination().get(destination).getLongitude();
        double destinationLatitude = setting.getDestinationCoordination().get(destination).getLatitude();
        long flightTime = setting.getFlightTime();

        Map<String, Double> coordinationMapAtSeconds = MathUtils.calculateCoordinateAtSeconds(departureLongitude, departureLatitude,
                destinationLongitude, destinationLatitude, flightTime, atSeconds);
        double longitudeAtSeconds = coordinationMapAtSeconds.get("longitude");
        double latitudeAtSeconds = coordinationMapAtSeconds.get("latitude");

        Map<String, Double> coordinationMapAtDestination = MathUtils.calculateCoordinateAtSeconds(departureLongitude, departureLatitude,
                destinationLongitude, destinationLatitude, flightTime, flightTime);
        double longitudeAtDestination = coordinationMapAtDestination.get("longitude");
        double latitudeAtDestination = coordinationMapAtDestination.get("latitude");


        return MathUtils.calculateDistanceByLngLat(longitudeAtSeconds, latitudeAtSeconds,
                longitudeAtDestination, latitudeAtDestination);
    }

    private String createFileName() {
        String dateStr = DateUtils.getCurrentDateForFileName();
        String droneName = droneFromController.getName();

        return dateStr.concat(FlightRecorder.DASH).concat("flyingInfo").concat(FlightRecorder.DASH)
                .concat("single").concat(FlightRecorder.DASH).concat(droneName).concat(".csv");
    }

    private boolean isExistErrorEvent(ErrorType ErrorType) {
        return (ErrorType != null) && (ErrorType != kr.co.korea.error.ErrorType.NORMAL);
    }


    public synchronized void waitFlight(){
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}