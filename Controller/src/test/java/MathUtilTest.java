import kr.co.korea.domain.Coordination;
import kr.co.korea.service.LocationProvider;
import kr.co.korea.util.MathUtils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by ideapad on 2016-01-24.
 */
public class MathUtilTest {
    @Test
    public void calculateDistance(){
        double x1 = 10;
        double y1 = 3;

        double x2 = 5;
        double y2 = 6;

        System.out.println(MathUtils.getDistance(x1, y1, x2, y2));
    }

    @Test
    public void calculateDistanceByCoordinate(){
        /**
         * 출발지와 목적지를 입력하여 얻은 좌표로 거리 계산 테스트.
         * 1. 출발지를 입력. 좌표를 얻어온다.
         * 2. 목적지를 입력. 좌표를 얻어온다.
         * 3. 거리를 계산한다.
         */
        Map<String, Coordination> departureCoordinationMap;
        String departure = "큰고개역";

        departureCoordinationMap = LocationProvider.getCoordination(departure);

        if(departureCoordinationMap == null){
            System.out.println("입력하신 출발지의 좌표가 존재하지 않습니다.");
        }

        Map<String, Coordination> destinationCoordinationMap = null;
        String destination = "아양교역";

        destinationCoordinationMap = LocationProvider.getCoordination(destination);

        Coordination departureCoordination = departureCoordinationMap.get(departure);
        Coordination destinationCoordination = destinationCoordinationMap.get(destination);

        System.out.println("출발지 좌표: " + departureCoordination.getLongitude() + ", " + departureCoordination.getLatitude());
        System.out.println("목적지 좌표: " + destinationCoordination.getLongitude() + ", " + destinationCoordination.getLatitude());

        System.out.println(departure + " --> " + destination + " : " +
                MathUtils.calculateDistanceByLngLat(departureCoordination.getLongitude(), departureCoordination.getLatitude(),
                        destinationCoordination.getLongitude(), destinationCoordination.getLatitude()));
    }

    /**
     * 비행 시간 계산 테스트
     * (거리 / (시속*1000)) * 3600 = 비행 시간(초)
     */
    @Test
    public void calculateSecondsByDistanceAndSpeed(){
        assertEquals(MathUtils.calculateSecondsBySpeedAndDistance(20, 800), 160, 1);
    }



    @Test
    public void calculateAngleTest(){
        /**
         * TODO 출발지와 목적지를 입력하여 얻은 좌표로 거리 계산 테스트.
         * 1. 출발지를 입력. 좌표를 얻어온다.
         * 2. 목적지를 입력. 좌표를 얻어온다.
         * 3. 거리를 계산한다.
         */
        Map<String, Coordination> departureCoordinationMap;
        String departure = "공덕역";

        departureCoordinationMap = LocationProvider.getCoordination(departure);

        if(departureCoordinationMap == null){
            System.out.println("입력하신 출발지의 좌표가 존재하지 않습니다.");
        }

        Map<String, Coordination> destinationCoordinationMap = null;
        String destination = "아현역";

        destinationCoordinationMap = LocationProvider.getCoordination(destination);

        if(destinationCoordinationMap == null){
            System.out.println("입력하신 목적지의 좌표가 존재하지 않습니다.");
        }

        Coordination departureCoordination = departureCoordinationMap.get(departure);
        Coordination destinationCoordination = destinationCoordinationMap.get(destination);

        System.out.println("출발지 경도: " + departureCoordination.getLongitude());
        System.out.println("출발지 위도: " + departureCoordination.getLatitude());

        System.out.println("방위각: " + MathUtils.calculateAngle(departureCoordination.getLatitude(), departureCoordination.getLongitude(),
                destinationCoordination.getLatitude(), destinationCoordination.getLongitude()) + "도");
    }



    @Test
    public void calculateCoordinateByAngleAndDistanceTest(){
        double departureX10 = 127.029196643;
        double departureY10 = 37.586283911;
        double destinationX11 = 127.03629823;
        double destinationY11 = 37.590630832;
        double totalSeconds = 71.0;
        double atSeconds = 71.0;

        Map<String, Double> coordinationMap1 = MathUtils.calculateCoordinateAtSeconds(departureX10, departureY10, destinationX11, destinationY11, totalSeconds, atSeconds);
        System.out.println("출발지1 좌표: " + departureX10 + ", " + departureY10);
        System.out.println("목적지1 좌표: " + destinationX11 + ", " + destinationY11);

        double longitude1 = coordinationMap1.get("longitude");
        double latitude1 = coordinationMap1.get("latitude");

        System.out.println(atSeconds + "초 동안 이동한 좌표1: " + longitude1 + ", " + latitude1);


        double departureX20 = 127.03629823;
        double departureY20 = 37.590630832;
        double destinationX21 = 127.029196643;
        double destinationY21 = 37.586283911;


        Map<String, Double> coordinationMap2 = MathUtils.calculateCoordinateAtSeconds(departureX20, departureY20, destinationX21, destinationY21, totalSeconds, atSeconds);
        System.out.println("출발지2 좌표: " + departureX20 + ", " + departureY20);
        System.out.println("목적지2 좌표: " + destinationX21 + ", " + destinationY21);

        double longitude2 = coordinationMap2.get("longitude");
        double latitude2 = coordinationMap2.get("latitude");

        System.out.println(atSeconds + "초 동안 이동한 좌표2: " + longitude2 + ", " + latitude2);
    }

}
