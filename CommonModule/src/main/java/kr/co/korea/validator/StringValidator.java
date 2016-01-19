package kr.co.korea.validator;

import kr.co.korea.domain.Drone;
import kr.co.korea.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by kjs on 2016-01-15.
 */
public class StringValidator {
    public static boolean isNumber(String inputStr){
        boolean result = Pattern.matches("^[1-9]{0,1}[0-9]{1}$", inputStr);
        return result;
    }

    public static boolean isEmpty(String inputStr){
        return StringUtils.nullToWhiteSpace(inputStr).isEmpty();
    }

    public static boolean isLeaderOrFollower(String inputStr){
        boolean result = Pattern.matches("^[L|F]{1}$", inputStr.toUpperCase());
        return result;
    }

    public static boolean isExistLeader(String inputStr, Map<String, Drone> droneMap){
        boolean result = false;

        if(inputStr.toUpperCase().equals("L")){
            for(String key : droneMap.keySet()){
                Drone drone = droneMap.get(key);
                String leaderOrFollower = drone.getLeaderOrFollower();

                if(leaderOrFollower.equals("L")){
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public static boolean isNotExistLeader(Map<String, Drone> droneMap){
        boolean result = true;
        for(String key : droneMap.keySet()){
            Drone drone = droneMap.get(key);
            String leaderOrFollower = drone.getLeaderOrFollower();

            if(leaderOrFollower.toUpperCase().equals("L")){
                result = false;
                break;
            }
        }

        return result;
    }

    public static boolean isGreaterThan(String input, int num) {
        return Integer.parseInt(input) >= num;
    }
    public static boolean isBetween(String input, int startNum, int endNum) {

        return (Integer.parseInt(input) >= startNum) && (Integer.parseInt(input) <= endNum);
    }
}
