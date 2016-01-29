package kr.co.korea.behavior;

import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;

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
public class Flight {
    private String droneName;
    private DroneSetting setting;

    public Flight(String droneName, DroneSetting setting){
        this.droneName = droneName;
        this.setting = setting;
    }

    public FlightStatus flyWithoutError(){
        System.out.println("droneName: " + droneName);
        System.out.println("리더 여부: " + setting.getDroneMap().get(droneName).getLeaderOrFollower());
        System.out.println("출발지: " + setting.getDeparture());
        System.out.println("목적지: " + setting.getDestination());
        System.out.println("비행시간: " + setting.getFlightTime());

        FlightStatus status = new FlightStatus();

        int countDown = 10;
        long flightTime = setting.getFlightTime();

        System.out.println("######### Take off and now hovering..");
        try {
            Thread.sleep(3000);

            System.out.println(countDown + "초 후에 비행을 시작합니다..");

            for(long i=countDown; i>0; i--){
                System.out.println(i);
                Thread.sleep(1000);
            }

            int startTime = 1;
            for(long i=flightTime; i>0; i--){
                Thread.sleep(1000);
                System.out.println("###### " + startTime + "초 비행");
                startTime++;
            }

            System.out.println("###### 목적지 도착. Now landing...");
            Thread.sleep(3000);
            System.out.println("###### 비행 종료..");
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(countDown + "초 후에 비행을 시작합니다..");
        return status;
    }
}
