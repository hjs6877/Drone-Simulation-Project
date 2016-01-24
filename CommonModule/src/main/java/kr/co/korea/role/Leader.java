package kr.co.korea.role;

import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlightStatus;

/**
 * Created by ideapad on 2016-01-21.
 */
public class Leader {
    private String droneName;
    private DroneSetting setting;

    public Leader(String droneName, DroneSetting setting) {
        this.droneName = droneName;
        this.setting = setting;
    }

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
     * @return
     */
    public FlightStatus doLeaderProcess(){
        FlightStatus status = new FlightStatus();
        return status;
    }
}
