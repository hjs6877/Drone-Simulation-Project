package kr.co.korea.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-24.
 */
public class MathUtils {
    public static double getDistance(double departureX, double departureY, double destinationX, double destinationY){
        double distance;

        if((departureX == destinationX) && (departureY == destinationY)){
            distance = 0.0;
        }else if((departureX == destinationX) && (departureY != destinationY)){
            distance = Math.abs(destinationY-departureY);
        }else if((departureX != destinationX) && (departureY == destinationY)){
            distance = Math.abs(departureX-destinationX);
        }else {
            double a = Math.pow(destinationX-departureX, 2);
            double b = Math.pow(destinationY-departureY, 2);
            distance = Math.sqrt(a+b);
        }

        return distance;

    }

    /**
     * 경/위도를 좌표로 가지는 2개 지역의 거리 계산.
     *
     * @param P1_longitude
     * @param P1_latitude
     * @param P2_longitude
     * @param P2_latitude
     * @return
     */
    public static double calculateDistanceByLngLat(double P1_longitude, double P1_latitude,
                                                   double P2_longitude, double P2_latitude) {
        if ((P1_latitude == P2_latitude) && (P1_longitude == P2_longitude)) {
            return 0;
        }
        double e10 = P1_latitude * Math.PI / 180;
        double e11 = P1_longitude * Math.PI / 180;
        double e12 = P2_latitude * Math.PI / 180;
        double e13 = P2_longitude * Math.PI / 180;
        /* 타원체 GRS80 */
        double c16 = 6356752.314140910;
        double c15 = 6378137.000000000;
        double c17 = 0.0033528107;
        double f15 = c17 + c17 * c17;
        double f16 = f15 / 2;
        double f17 = c17 * c17 / 2;
        double f18 = c17 * c17 / 8;
        double f19 = c17 * c17 / 16;
        double c18 = e13 - e11;
        double c20 = (1 - c17) * Math.tan(e10);
        double c21 = Math.atan(c20);
        double c22 = Math.sin(c21);
        double c23 = Math.cos(c21);
        double c24 = (1 - c17) * Math.tan(e12);
        double c25 = Math.atan(c24);
        double c26 = Math.sin(c25);
        double c27 = Math.cos(c25);
        double c29 = c18;
        double c31 = (c27 * Math.sin(c29) * c27 * Math.sin(c29))
                + (c23 * c26 - c22 * c27 * Math.cos(c29))
                * (c23 * c26 - c22 * c27 * Math.cos(c29));
        double c33 = (c22 * c26) + (c23 * c27 * Math.cos(c29));
        double c35 = Math.sqrt(c31) / c33;
        double c36 = Math.atan(c35);
        double c38 = 0;
        if (c31 == 0) {
            c38 = 0;
        } else {
            c38 = c23 * c27 * Math.sin(c29) / Math.sqrt(c31);
        }
        double c40 = 0;
        if ((Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))) == 0) {
            c40 = 0;
        } else {
            c40 = c33 - 2 * c22 * c26
                    / (Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38)));
        }

        double c41 = Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))
                * (c15 * c15 - c16 * c16) / (c16 * c16);
        double c43 = 1 + c41 / 16384
                * (4096 + c41 * (-768 + c41 * (320 - 175 * c41)));
        double c45 = c41 / 1024 * (256 + c41 * (-128 + c41 * (74 - 47 * c41)));
        double c47 = c45
                * Math.sqrt(c31)
                * (c40 + c45
                / 4
                * (c33 * (-1 + 2 * c40 * c40) - c45 / 6 * c40
                * (-3 + 4 * c31) * (-3 + 4 * c40 * c40)));
        double c50 = c17
                / 16
                * Math.cos(Math.asin(c38))
                * Math.cos(Math.asin(c38))
                * (4 + c17
                * (4 - 3 * Math.cos(Math.asin(c38))
                * Math.cos(Math.asin(c38))));
        double c52 = c18
                + (1 - c50)
                * c17
                * c38
                * (Math.acos(c33) + c50 * Math.sin(Math.acos(c33))
                * (c40 + c50 * c33 * (-1 + 2 * c40 * c40)));
        double c54 = c16 * c43 * (Math.atan(c35) - c47);
        // return distance in meter
        return c54;
    }

    //방위각 구하는 부분
    public static short calculateAzimuthByCoordinate(double P1_longitude, double P1_latitude,
                                                     double P2_longitude, double P2_latitude) {
        // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에
        //라디안 각도로 변환한다.
        double Cur_Lat_radian = P1_latitude * (3.141592 / 180);
        double Cur_Lon_radian = P1_longitude * (3.141592 / 180);
        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에
        // 라디안 각도로 변환한다.
        double Dest_Lat_radian = P2_latitude * (3.141592 / 180);
        double Dest_Lon_radian = P2_longitude * (3.141592 / 180);
        // radian distance
        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian)
                * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian)
                * Math.cos(Dest_Lat_radian)
                * Math.cos(Cur_Lon_radian - Dest_Lon_radian));
        // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는
        //방향을 설정해야 한다. 라디안값이다.
        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math
                .sin(Cur_Lat_radian)
                * Math.cos(radian_distance))
                / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));
        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.
        double true_bearing = 0;
        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0) {
            true_bearing = radian_bearing * (180 / 3.141592);
            true_bearing = 360 - true_bearing;
        } else {
            true_bearing = radian_bearing * (180 / 3.141592);
        }
        return (short) true_bearing;
    }

    /**
     * 거리와 속도를 이용해 총 비행 시간 구하기.
     *
     * @param speed
     * @param distance
     * @return
     */
    public static double calculateSecondsBySpeedAndDistance(double speed, double distance){
        /**
         *  거리: 단위 - m(미터)
         *  비행시간: 단위 - seconds(초)
         * (거리 / (시속*1000)) * 3600 = 비행 시간(초)
         */
        double seconds = (distance / (speed * 1000)) * 3600;
        return seconds;
    }

    /**
     * 두 지점간의 각도 구하기.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double calculateAngle(double x1,double y1, double x2,double y2){
        double dx = x2 - x1;
        double dy = y2 - y1;

        double rad= Math.atan2(dx, dy);
        double degree = (rad*180)/Math.PI ;

        return degree;
    }

    /**
     * 해당 시간(초)동안에 이동한 거리 계산.
     *
     * @param totalDistance
     * @param totalSeconds
     * @param atSeconds
     * @return
     */
    public static double calculateDistanceAtSeconds(double totalDistance, double totalSeconds, double atSeconds){
        double distanceAtSeconds = (totalDistance / totalSeconds) * atSeconds;

        return distanceAtSeconds;
    }

    /**
     * 해당 시간(초)동안에 이동한 좌표 계산. 평면 좌표계에서의 시뮬레이터용 계산. 실제 비행에는 사용하지 못함.
     *
     * @param departureX
     * @param departureY
     * @param destinationX
     * @param destinationY
     * @param totalSeconds
     * @param atSeconds
     * @return
     */
    public static Map<String, Double> calculateCoordinateAtSeconds(double departureX, double departureY,
                                                                   double destinationX, double destinationY,
                                                                   double totalSeconds, double atSeconds){
        Map<String, Double> coordinationMap = new HashMap<String, Double>();

        double differencesX = Math.abs(destinationX - departureX);
        double differencesY = Math.abs(destinationY - departureY);

        double longitudePerSecond = differencesX / totalSeconds;
        double latitudePerSecond = differencesY / totalSeconds;

        double longitudeAtSeconds = longitudePerSecond * atSeconds;
        double latitudeAtSeconds = latitudePerSecond * atSeconds;

        double newDepartureX = 0.0;
        double newDepartureY = 0.0;

        if((destinationX > departureX) && (destinationY > departureY)){
            newDepartureX = departureX + longitudeAtSeconds;
            newDepartureY = departureY + latitudeAtSeconds;
        }else if((destinationX > departureX) && (destinationY < departureY)){
            newDepartureX = departureX + longitudeAtSeconds;
            newDepartureY = departureY - latitudeAtSeconds;
        }else if((destinationX < departureX) && (destinationY < departureY)){
            newDepartureX = departureX - longitudeAtSeconds;
            newDepartureY = departureY - latitudeAtSeconds;
        }else if((destinationX < departureX) && (destinationY > departureY)){
            newDepartureX = departureX - longitudeAtSeconds;
            newDepartureY = departureY + latitudeAtSeconds;
        }

        double longitude = Double.parseDouble(String.format("%.9f", newDepartureX));
        double latitude = Double.parseDouble(String.format("%.9f", newDepartureY));

        coordinationMap.put("longitude", longitude);
        coordinationMap.put("latitude", latitude);

        return coordinationMap;
    }

    public static int getRandomIntNumber(int min, int max){
        int randomNumber = (int) (Math.random() * (max - min + 1)) + min;

        return randomNumber;
    }

    public static long getRandomLongNumber(long min, long max){
        long randomNumber = (long) (Math.random() * (max - min + 1)) + min;

        return randomNumber;
    }
}
