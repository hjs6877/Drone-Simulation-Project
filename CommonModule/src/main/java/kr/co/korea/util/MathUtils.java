package kr.co.korea.util;

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

}
