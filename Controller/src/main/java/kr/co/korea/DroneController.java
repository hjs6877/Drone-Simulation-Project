package kr.co.korea;

import kr.co.korea.domain.Coordination;
import kr.co.korea.domain.Drone;
import kr.co.korea.domain.DroneSetting;
import kr.co.korea.domain.FlyingMessage;
import kr.co.korea.error.ErrorEventProvider;
import kr.co.korea.error.ErrorLevel;
import kr.co.korea.error.ErrorType;
import kr.co.korea.repository.DroneControllerServerRepository;
import kr.co.korea.repository.DroneRunnerRepository;
import kr.co.korea.runner.DroneRunner;
import kr.co.korea.service.LocationProvider;
import kr.co.korea.util.MathUtils;
import kr.co.korea.validator.StringValidator;

import java.io.IOException;
import java.util.*;

/**
 * Created by kjs on 2016-01-15.
 */
public class DroneController {
    /**
     * TODO 목적지에 도착한 Drone들에 대한 모든 Drone들의 비행 정보를 표시하는 처리.
     * DroneControllerServer 241에서 DronRunner가 null이다. 여기를 해결하는 부분을 해보고 안되면 일단 보류. 마지막 종료는 그대로 종료하는걸로..
     *
     * TODO 중간에 비행이 완전 중지 될 경우 --> Drone들의 비행 정보를 표시하는 처리.
     * 팔로워에 심각한 장애가 발생하여 비행 대긴 시킨 시점과 리더에 심각한 장애가 발생하여
     * 모든 팔로워들을 비행 대기 시킨 시점이 겹칠 경우 syncronize 할 필요가 있음. TODO
     * 그렇지 않으면 팔로워가 심각한 장애로 비행 대기 중일 때, 리더 선출 후,
     * 팔로워들을 깨울 때 심각한 장애로 비행 대기 중인 팔로워도 깨워질 수 있다.
     */
    /**
     * 1. 비행 환경 설정.
     *      (1) 드론 대수 설정 √
     *      (2) 포메이션 형태 설정(드론 대수에 따라 자동 지정 또는 수동 지정) √
     *      (2) 초기 리더 설정 √
     *      (3) 출발지, 목적지 좌표(위도/경도)및 거리(위도/경도에 따라 자동 계산) 설정 √
     *      (4) 드론 속도 설정(km, 전체 드론에 일괄 적용) √
     *      (5) 배터리 용량 및 충전 상태(optional)
     *      (6) 리더의 장애 설정(내부 장애만 설정 가능) --> 리더 비행 시에 자동으로 발생해야 할 프로세스임. // TODO 이쪽 주석에서 추후 제거.
     *          - 랜덤으로 장애 발생 횟수 및 장애 발생 시점을 지정.
     *          - 장애 심각도 역시 랜덤으로 발생.(심각도가 '비행 불능' 일 경우에는 리더 교체)
     * 2. 각각의 드론은 비행 대기 상태로 대기중.(프로세스 실행해서 대기)
     * 3. 비행 시작
     * 4. 로깅
     *      (1) 콘솔로 출력
     *      (2) 파일에 로깅(드론별로 파일에 로깅)
     *          - 출발지, 목적지 좌표
     *          - 이동한 거리
     *          - 남은 거리
     *          - 배터리 용량
     *          - 초기 리더인지, 팔로워인지
     *          - 장애 발생 정보
     *              - 어떤 드론에게 장애가 발생했는지
     *              - 장애 유형
     *              - 장애 심각도
     *          - 리더 변경 시, 리더인지, 팔로워인지
     *
     *
     * 5. 비행 종료에 따른 결과 보고
     *      (1) 발생한 장애 유형 정보
     *      (2) 장애 발생 정보(횟수, 유형, 심각도)
     *      (3) 리더 변경 정보(변경 횟수, 변경 히스토리(Mars --> Venus --> Earth 등으로..))
     *      (4) 몇대의 드론이 비행에 성공했나
     *
     *
     * 6. 비행 시뮬레이션 시각화(optional)
     *      (1) 드론별 비행 이동 경로
     *      (2) 장애 발생 시점 및 장애 발생 지점
     *      (3) 결과 보고
     *
     * 7. 이 연구의 핵심은 장애 발생 시, 드론간의 커뮤니케이션을 통해 동적으로 리더를 선정함으로 인해
     * 최종 목적지까지로의 비행 임무를 완수할 가능성이 높아진다는것이다. 이부분을 적극 주장하자.
     */
    public static DroneRunnerRepository droneRunnerRepository = new DroneRunnerRepository();
    public static DroneControllerServerRepository droneControllerServerRepository = new DroneControllerServerRepository();


    public static void main(String[] args) throws IOException {

        DroneController controller = new DroneController();
        DroneSetting setting = new DroneSetting();


        /**
         * ControllerServer Thread 가동.
         */
        controller.startDroneControllerServer();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 비행 환경 설정.
         */
        controller.setQuestionForConfiguration();
        controller.setNumberOfDrone(setting);
        controller.setDroneInfo(setting);
        controller.setFormationType(setting);
        controller.setDeparture(setting);
        controller.setDestination(setting);
        controller.setSpeed(setting);
        controller.setDistance(setting);
        controller.setAngle(setting);
        controller.setFilghtTime(setting);
        controller.setRandomErrorEvent(setting);
        controller.chooseStartFlightOrNot(setting);
        controller.setDroneSetting(setting);

        controller.sendFlyingStartMessage();


        int formationType = setting.getFormationType();
        int numberOfDrone = setting.getNumberOfDrone();
        String departure = setting.getDeparture();
        String destination = setting.getDestination();
        double departureLongitude = setting.getDepartureCoordination().get(departure).getLongitude();
        double departureLatitude = setting.getDepartureCoordination().get(departure).getLatitude();
        double destinationLongitude = setting.getDestinationCoordination().get(destination).getLongitude();
        double destinationLatitude = setting.getDestinationCoordination().get(destination).getLatitude();
        int speed = setting.getSpeed();
        double distance = setting.getDistance();
        double angle = setting.getAngle();

        long flightTime = setting.getFlightTime();

        System.out.println("드론 비행 대수: " + numberOfDrone);
        System.out.println("포메이션 타입: " + formationType);
        System.out.println("출발지: " + departure);
        System.out.println("출발지 좌표: " + departureLongitude + ", " + departureLatitude);
        System.out.println("목적지: " + destination);
        System.out.println("목적지 좌표: " + destinationLongitude + ", " + destinationLatitude);
        System.out.println("비행 거리: " + distance);
        System.out.println("비행 속도: " + speed);
        System.out.println("각도: " + angle);
        System.out.println("비행 시간: " + flightTime + "초");
    }

    /**
     * Controller Server Thread 시작.
     */
    public void startDroneControllerServer() throws IOException {
        DroneControllerServer droneControllerServer = new DroneControllerServer();
        DroneController.droneControllerServerRepository.addDroneControllerServer(droneControllerServer);

        droneControllerServer.start();
    }


    /**
     * 비행 환경 설정을 위한 사전 질문.
     */
    private void setQuestionForConfiguration(){
        while(true){
            System.out.print("Drone 프로세스를 가동했습니까?(y or n): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if(StringValidator.isEmpty(input)){
                System.out.println("'y' 또는 'n'을 입력해주세요");
                continue;
            }

            if((!input.toLowerCase().equals("y")) && (!input.toLowerCase().equals("n"))){
                System.out.println("비행을 시작하시려면 y, 종료하시려면 n을 입력해주세요.");
                continue;
            }

            if(input.toLowerCase().equals("n")){
                System.out.println("Drone 프로세스 가동 후 Controller를 재 가동해주세요.");
                System.exit(-1);
            }

            if(input.toLowerCase().equals("y")){
                break;
            }
        }
    }


    /**
     * 드론 댓수 설정.
     *
     * @param setting
     */
    private void setNumberOfDrone(DroneSetting setting) throws IOException {
        int minNum = 2;

        int numberOfDrone = DroneController.droneRunnerRepository.size();
        if(numberOfDrone < minNum){
            System.out.println("현재 비행 가능한 Drone은 " + numberOfDrone + "대 입니다.");
            System.out.println("비행 가능한 Drone은 최소 " + minNum + "대 이상이여야 합니다. Drone 프로세스 가동 후 Controller를 재 가동해주세요.");

            if(numberOfDrone != 0){
                DroneController.droneRunnerRepository.sendMessageToAllClient(FlyingMessage.DO_FLYING_STOP);
            }
            System.exit(-1);
        }

        System.out.println(numberOfDrone + "대의 Drone이 비행에 참여합니다.");
        setting.setNumberOfDrone(numberOfDrone);

//        TODO Future Work.
//        while(true){
//            System.out.print("비행 할 드론의 대수 입력: ");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
//
//            if(!StringValidator.isNumber(input)){
//                System.out.println("비행 할 드론 대수는 숫자로 입력해주세요.");
//                continue;
//            }
//
//            if (!StringValidator.isBetween(input, num, numberOfClients)){
//                System.out.println("비행 할 드론 대수는 " + num + "~" + numberOfClients + "의 숫자로 입력해주세요.");
//                continue;
//            }
//
//            numberOfDrone = Integer.parseInt(input);
//            break;
//
//        }
    }


    /**
     * 드론 이름, 리더/팔로워 구분 등의 드론 입력 설정.
     *
     * @param setting
     */
    private void setDroneInfo(DroneSetting setting) {
        int numberOfDrone = setting.getNumberOfDrone();

        Map<String, Drone> droneMap = new HashMap<String, Drone>();

        System.out.println(numberOfDrone + "대의 Drone에 대한 정보를 설정합니다.");

        Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();
        int i = 1;
        while(iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            Drone drone = droneRunner.getDroneFromClient();
            String droneName = drone.getName();

            System.out.println("## " + droneName + "의 정보 설정 시작.");

            /**
             * 리더 및 팔로워 구분 입력.
             */
            while(true){
                System.out.print("Leader 설정(Leader일 경우 'L', Follower일 경우 'F' 입력): ");
                Scanner scanner2 = new Scanner(System.in);
                String leaderOrFollower = scanner2.nextLine();

                if(!StringValidator.isLeaderOrFollower(leaderOrFollower)){
                    System.out.println("Leader/Follower는 'L' 또는 'F'로 입력해주세요.");
                    continue;
                }

                if(StringValidator.isExistLeader(leaderOrFollower, droneMap)){
                    System.out.println("이미 리더가 존재합니다. 'F'를 입력해주세요.");
                    continue;
                }

                if((i == numberOfDrone) && (leaderOrFollower.toUpperCase().equals("F")) && (StringValidator.isNotExistLeader(droneMap))){
                    System.out.println("리더가 존재하지않습니다. 'L'을 입력해주세요.");
                    continue;
                }

                i++;
                leaderOrFollower = leaderOrFollower.toUpperCase();
                drone.setLeaderOrFollower(leaderOrFollower);
                break;
            }

            droneMap.put(droneName, drone);
        }


        setting.setDroneMap(droneMap);
    }


    /**
     * 포메이션 대형 설정.
     * @return
     */
    private void setFormationType(DroneSetting setting) {
        int formationType = 0;

        while(true){
            System.out.print("포메이션 타입 입력(1-Horizontal, 2-Vertical, 3-Triangle, 4-Diamond:");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
            String input = "1";
            int startNum = 1;
            int endNum = 4;

            if(!StringValidator.isNumber(input)){
                System.out.println("포메이션 타입은 숫자로 입력해주세요.");
                continue;
            }

            if(!StringValidator.isBetween(input, startNum, endNum)){
                System.out.println("포메이션 타입은 " + startNum + "에서 " + endNum + "사이의 숫자로 입력해주세요.");
                continue;
            }

            formationType = Integer.parseInt(input);
            break;
        }

        setting.setFormationType(formationType);

    }


    /**
     * 출발지 설정. 출발지 설정 후, 출발지 좌표까지 지정 됨.
     *
     * @param setting
     */
    private void setDeparture(DroneSetting setting) {
        /**
         * 출발지 입력.
         */
        while(true){
            String departure = "";

            System.out.print("출발지 입력: ");
//            Scanner scanner1 = new Scanner(System.in);
//            departure = scanner1.nextLine();

            departure = "안암역";
            if(StringValidator.isEmpty(departure)){
                System.out.println("출발지를 입력해주세요.");
                continue;
            }

            setting.setDeparture(departure);

            Map<String, Coordination> coordinationMap = getDepartureCoordination(setting);

            if(coordinationMap == null){
                System.out.println("입력하신 출발지의 좌표가 존재하지 않습니다.");
                continue;
            }


            setting.setDepartureCoordination(coordinationMap);
            break;
        }
    }


    /**
     * 목적지 설정. 목적지 설정 후, 목적지 좌표까지 지정 됨.
     * @param setting
     */
    private void setDestination(DroneSetting setting) {
        /**
         * 목적지 입력.
         */
        while(true){
            String destination = "";

            System.out.print("목적지 입력: ");
//            Scanner scanner1 = new Scanner(System.in);
//            destination = scanner1.nextLine();

            destination = "고려대역";

            if(StringValidator.isEmpty(destination)){
                System.out.println("목적지를 입력해주세요.");
                continue;
            }

            setting.setDestination(destination);

            Map<String, Coordination> coordinationMap = getDestinationCoordination(setting);

            if(coordinationMap == null){
                System.out.println("입력하신 목적지의 좌표가 존재하지 않습니다.");
                continue;
            }

            setting.setDestinationCoordination(coordinationMap);
            break;

        }
    }


    /**
     * 출발지 좌표 셋팅.
     *
     * @param setting
     * @return
     */
    private Map<String, Coordination> getDepartureCoordination(DroneSetting setting) {
        String departure = setting.getDeparture();

        return LocationProvider.getCoordination(departure);
    }


    /**
     * 목적지 좌표 셋팅.
     * @param setting
     * @return
     */
    private Map<String, Coordination> getDestinationCoordination(DroneSetting setting) {
        String destination = setting.getDestination();

        return LocationProvider.getCoordination(destination);
    }


    /**
     * 비행 속도 설정.
     *
     * @param setting
     */
    private void setSpeed(DroneSetting setting) {
        int speed = 0;

        while(true){
            System.out.print("속도 입력: ");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
            String input = "60";
            int num = 1;



            if(!StringValidator.isNumber(input)){
                System.out.println("비행 속도는 숫자로 입력해주세요.");
                continue;
            }

            if(!StringValidator.isGreaterThan(input, num)){
                System.out.println("비행 속도는 " + num + " 이상의 숫자로 입력해주세요.");
                continue;
            }

            speed = Integer.parseInt(input);
            break;

        }

        setting.setSpeed(speed);
    }


    /**
     * 목적지 까지의 거리 설정
     *
     * @param setting
     */
    private void setDistance(DroneSetting setting) {
        Map<String, Coordination> departureCoordinationMap = setting.getDepartureCoordination();
        Map<String, Coordination> destinationCoordinationMap = setting.getDestinationCoordination();

        Coordination departureCoordination = departureCoordinationMap.get(setting.getDeparture());
        Coordination destinationCoordination = destinationCoordinationMap.get(setting.getDestination());

        double departureLongitude = departureCoordination.getLongitude();
        double departureLatitude = departureCoordination.getLatitude();

        double destinationLongitude = destinationCoordination.getLongitude();
        double destinationLatitude = destinationCoordination.getLatitude();

        double distance = MathUtils.calculateDistanceByLngLat(departureLongitude, departureLatitude, destinationLongitude, destinationLatitude);

        setting.setDistance(distance);
    }


    /**
     * 목적지까지의 방위각 설정.
     *
     * @param setting
     */
    private void setAngle(DroneSetting setting) {
        String departure = setting.getDeparture();
        String destination = setting.getDestination();
        double departureLongitude = setting.getDepartureCoordination().get(departure).getLongitude();
        double departureLatitude = setting.getDepartureCoordination().get(departure).getLatitude();
        double destinationLongitude = setting.getDestinationCoordination().get(destination).getLongitude();
        double destinationLatitude = setting.getDestinationCoordination().get(destination).getLatitude();

        double angle = MathUtils.calculateAzimuthByCoordinate(departureLongitude, departureLatitude, destinationLongitude, destinationLatitude);
        setting.setAngle(angle);
    }


    /**
     * 비행 시간 계산 및 설정.
     *
     * @param setting
     */
    private void setFilghtTime(DroneSetting setting) {
        int speed = setting.getSpeed();
        double distance = setting.getDistance();

        double flightTime = MathUtils.calculateSecondsBySpeedAndDistance(speed, distance);

        setting.setFlightTime(Math.round(flightTime));

    }


    /**
     * 랜덤 에러 이벤트 생성 및 설정.
     *
     * @param setting
     */
    private void setRandomErrorEvent(DroneSetting setting) {
        ErrorEventProvider errorEventProvider = new ErrorEventProvider();

        TreeMap<Long, ErrorType> errorEvent;

        Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();

        while(iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            Drone drone = droneRunner.getDroneFromClient();

            String leaderOrFollower = drone.getLeaderOrFollower();
            long flightTime = setting.getFlightTime();

            if(leaderOrFollower.equals("L")){
                errorEvent = errorEventProvider.createRandomErrorEvent(flightTime, ErrorLevel.STRONG);
            }else{
                errorEvent = errorEventProvider.createRandomErrorEvent(flightTime, ErrorLevel.WEAK);
            }

            drone.setErrorEvent(errorEvent);
        }
    }


    /**
     * 비행 시작 명령 내리기.
     *
     * @param setting
     */
    private void chooseStartFlightOrNot(DroneSetting setting) {

        while(true){
            System.out.print("비행을 시작하시겠습니까? 시작하시려면 'y'를 중단하시려면 'n'을 입력하세요: ");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
            String input = "y";

            if(StringValidator.isEmpty(input)){
                System.out.println("'y' 또는 'n'을 입력해주세요");
                continue;
            }

            if(!input.toLowerCase().equals("y")){
                System.out.println("비행 프로세스를 중단합니다.");

                DroneController.droneRunnerRepository.sendMessageToAllClient(FlyingMessage.DO_FLYING_STOP);

                System.exit(-1);
            }

            break;
        }
    }

    private void setDroneSetting(DroneSetting setting){
        Iterator<DroneRunner> iterator = DroneController.droneRunnerRepository.iterator();
        while(iterator.hasNext()){
            DroneRunner droneRunner = iterator.next();
            Drone droneFromClient = droneRunner.getDroneFromClient();
            Drone droneToClient = droneFromClient;

            droneToClient.setDroneSetting(setting);
            droneRunner.setDroneToClient(droneToClient);
        }
    }
    private void sendFlyingStartMessage() {

        System.out.println("## 비행 시작 메시지 전송.");
        DroneController.droneRunnerRepository.sendMessageToAllClientFromController(FlyingMessage.DO_FLYING_START);
    }
}