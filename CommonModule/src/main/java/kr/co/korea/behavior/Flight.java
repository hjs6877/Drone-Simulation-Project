package kr.co.korea.behavior;

import kr.co.korea.domain.*;
import kr.co.korea.error.ErrorType;
import kr.co.korea.util.MathUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

/**
 * 리더의 비행 시작(without error)시, 시나리오
 * 1. 남은 비행 시간보다 작은 시간동안 비행.
 *      (1) 1초동안의 비행 상태 저장 및 로깅.
 *          - droneName, 남은 비행시간, 위도, 경도, 이동거리, 장애 유형, 장애 포인트, 장애 누적 포인트
 *      (2) 장애 누적 포인트가 10이상이면,
 *          1) 컨트롤러에게 비행 상태 정보 전송
 *          2) 컨트롤러는 리더로부터 10 이상의 장애 누적 포인트 상태 정보를 전송 받으면 hovering 명령을
 *             각 Drone에게 브로드캐스팅.
 *          3) 각 Drone은 hovering을 유지하면서 장애 상태 정보를 컨트롤러에게 전송(실제로는 Drone별로 각각 서로의 정보를 공유해야 됨.)
 *          4) 기존 리더 프로세스 Kill
 *          5) 남은 Drone이 있다면,
 *              1) 비행 시간 정보를 가지고 새로운 리더에게 비행 프로세스 시작 명령
 *              2) 팔로워는 장애 상태 정보만 남은 비행 시간동안 갱신한다.
 *          6) 남은 Drone이 없다면,
 *              1) 비행 결과 출력.
 *      (3) 1초의 Thread.sleep(1000)씩 딜레이.
 *      (4) 비행 성공 후, 종료시 비행 결과 출력.
 *          * 전체 비행 결과
 *          - 총 비행 거리
 *          - 총 비행 시간
 *          - 비행 성공 Drone 대수
 *          - 비행 성공 Drone Name
 *          - 리더 교체 횟수
 *          - 리더 교체 시점
 *          - 리더 교체 시점별 리더
 *
 *          * 개별 비행 결과
 *          - Drone Name
 *          - 비행 성공 여부
 *          - 총 비행 거리
 *          - 총 비행 시간
 *          - 장애 발생 시점. 장애 발생 유형, 장애 포인트
 *          - 누적 장애 포인트
 *          - 리더 교체 시점
 */
// TODO 인터페이스로 바꾸고, LeaderFlight, FollowerFlight을 구현하는것으로 변경.
public class Flight {
    private ObjectOutputStream objectOutputStream;
    private String droneName;
    private Drone drone;
    private DroneSetting setting;
    private TreeMap<Long, ErrorType> errorEventMap;
    private FlightStatus flightStatus;


    public Flight(ObjectOutputStream objectOutputStream, String droneName, Drone drone){
        this.objectOutputStream = objectOutputStream;
        this.droneName = droneName;
        this.drone = drone;
        flightStatus = new FlightStatus();
    }

    public FlightStatus flyAsLeader(){
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

        System.out.println("droneName: " + droneName);
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

                    /**
                     * 리더 교체가 필요한 장애가 발생했는가?
                     * TODO 여기서부터 리더 프로세스가 추가.
                     */
                    if(drone.getLeaderOrFollower().equals("L") && flightStatus.hasErrorEventForReplacingLeader()){
                        System.out.println("심각한 장애 발생으로 인해 리더 교체 프로세스 실시!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println("쓰레드 명: " + Thread.currentThread().getName());

                        FlyingInfo flyingInfo = drone.getFlyingInfo();
                        flyingInfo.setMessage(FlyingMessage.REPLACE_LEADER);

                        drone.setFlyingInfo(flyingInfo);
                        try {
                            objectOutputStream.writeObject(drone);
//                            System.out.println("쓰레드 대기 상태로 진입..");
//                            Thread.sleep(10000);
//                            synchronized (Thread.currentThread()){
//                                System.out.println("쓰레드 대기 상태로 진입..");
//
//                                Thread.currentThread().wait();
//                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("비행 메시지:::: " + drone.getFlyingInfo().getMessage());

                }

            }

            System.out.println("###### 목적지 도착. Now landing...");
            Thread.sleep(3000);
            System.out.println("###### 비행 종료..");
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return status;
    }

    private boolean isExistErrorEvent(ErrorType errorType) {
        return (errorType != null) && (errorType != ErrorType.NORMAL);
    }

    public FlightStatus flyAsFollower(){
        setting = drone.getDroneSetting();
        errorEventMap = drone.getErrorEvent();

        String departure = setting.getDeparture();
        String destination = setting.getDestination();
        double departureLongitude = setting.getDepartureCoordination().get(departure).getLongitude();
        double departureLatitude = setting.getDepartureCoordination().get(departure).getLatitude();
        double destinationLongitude = setting.getDestinationCoordination().get(destination).getLongitude();
        double destinationLatitude = setting.getDestinationCoordination().get(destination).getLatitude();
        long flightTime = setting.getFlightTime();

        System.out.println("droneName: " + droneName);
        System.out.println("리더 여부: " + setting.getDroneMap().get(droneName).getLeaderOrFollower());
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
             */
            for(long i=startTime; i<=flightTime; i++){
                Thread.sleep(1000);
                System.out.println("###### " + i + "초 비행");

                Map<String, Double> coordinationMapAtSeconds = MathUtils.calculateCoordinateAtSeconds(departureLongitude, departureLatitude,
                        destinationLongitude, destinationLatitude, flightTime, startTime);

                double longitude = coordinationMapAtSeconds.get("longitude");
                double latitude = coordinationMapAtSeconds.get("latitude");

                ErrorType errorType = errorEventMap.get(i);

                if(this.isExistErrorEvent(errorType)){
                    System.out.println(i + "초에 에러 이벤트 발생: " + errorEventMap.get(i));
                    System.out.println("비행 시 좌표: " + longitude + ", " + latitude);
                }

            }

            System.out.println("###### 목적지 도착. Now landing...");
            Thread.sleep(3000);
            System.out.println("###### 비행 종료..");
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return status;
    }
}
