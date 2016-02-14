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
public class FlyerSimpleTest extends Thread {
    public String waitMessage = "";
    private Socket socket;
    private ClientSender clientSender;
    Drone drone;
    TreeMap<Long, ErrorType> errorEventMap;
    DroneSetting setting;
    FlightStatus flightStatus;
    private String name;

    public FlyerSimpleTest(Socket socket, ClientSender clientSender, String name) throws IOException {
        flightStatus = new FlightStatus();

        this.socket = socket;
        this.clientSender = clientSender;
        this.name = name;
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

        FlightStatus status = new FlightStatus();

        int countDown = 5;

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
            for(long atSeconds=startTime; atSeconds<=50; atSeconds++){
                Thread.sleep(1000);
                System.out.println("###### " + atSeconds + "초 비행");
                System.out.println("###### waitMessage:: " + waitMessage);

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


                if(name.equals("venus") && atSeconds == 3){
                    System.out.println("심각한 장애 발생으로 인해 리더 교체 프로세스 실시!!!!!!!!!!!!!!!!!!!!!!");

                    /** 리더 교체 필요 메시지 전송 **/
                    clientSender.sendMessage(FlyingMessage.STATUS_NEED_REPLACE_LEADER);

                    /** 비행 대기 상태로 전환 **/
                    System.out.println("리더인 ㅇㅇ 이(가) 비행 대기 상태로 전환합니다..");


                }else{      /** 팔로워들에게 적용되는 프로세스 **/

                }

                if(waitMessage.equals("wait")){
                    this.waitFlight();
                }

            }


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
