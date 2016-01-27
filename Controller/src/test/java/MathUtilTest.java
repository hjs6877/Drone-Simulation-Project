import kr.co.korea.domain.Coordination;
import kr.co.korea.service.LocationProvider;
import kr.co.korea.util.MathUtils;
import kr.co.korea.validator.StringValidator;
import org.junit.Test;

import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;

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
         * TODO 출발지와 목적지를 입력하여 얻은 좌표로 거리 계산 테스트.
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

    @Test
    public void calculateSecondsByDistanceAndSpeed(){
        assertEquals(MathUtils.calculateSecondsByDistanceAndSpeed(1000, 20), 80, 1);
    }
}
