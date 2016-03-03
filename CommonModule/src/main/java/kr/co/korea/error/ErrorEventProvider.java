package kr.co.korea.error;

import kr.co.korea.util.MathUtils;

import java.util.*;

/**
 * Created by ideapad on 2016-02-01.
 */
public class ErrorEventProvider {
    /**
     *  원본 장애 타입
     *
     */
//    private ErrorType[] errorTypes = {
//            ErrorType.BATTERY_LIFE, ErrorType.BATTERY_LOW, ErrorType.BATTERY_DEAD,
//            ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_CONTINUOUS_ERROR, ErrorType.MOTOR_BALANCE_PERMANENT_ERROR,
//            ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_CONTINUOUS_ERROR, ErrorType.MOTOR_RPM_PERMANENT_ERROR,
//            ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_CONTINUOUS_ERROR, ErrorType.SENSOR_GPS_PERMANENT_ERROR,
//            ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_CONTINUOUS_ERROR, ErrorType.SENSOR_GYRO_PERMANENT_ERROR,
//            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_CONTINUOUS_ERROR, ErrorType.SENSOR_ACCELERATION_PERMANENT_ERROR,
//            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR, ErrorType.SENSOR_ALTITUDE_CONTINUOUS_ERROR, ErrorType.SENSOR_ALTITUDE_PERMANENT_ERROR,
//            ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_INTERMEIDATE_DEMAGE, ErrorType.PROPELLER_HEAVY_DEMAGE,
//            ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_INTERMEIDATE_DEMAGE, ErrorType.FRAME_HEAVY_DEMAGE,
//            ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_CONTINUOUS_ERROR, ErrorType.ESC_PERMANENT_ERROR,
//            ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_CONTINUOUS_ERROR, ErrorType.MCU_CONTROL_PERMANENT_ERROR,
//            ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_CONTINUOUS_ERROR, ErrorType.MCU_ARITHMETIC_PERMANENT_ERROR
//    };

    private ErrorType[] errorTypesWeak = {
            ErrorType.BATTERY_LIFE, ErrorType.BATTERY_LIFE,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR,
            ErrorType.BATTERY_LIFE, ErrorType.BATTERY_LIFE,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR,
            ErrorType.BATTERY_LIFE, ErrorType.BATTERY_LIFE,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR, ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR, ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,  ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE, ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE, ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR, ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR, ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR, ErrorType.MCU_ARITHMETIC_PASSING_ERROR,
            ErrorType.BATTERY_LOW,
            ErrorType.MOTOR_BALANCE_CONTINUOUS_ERROR,
            ErrorType.MOTOR_RPM_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GPS_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GYRO_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ACCELERATION_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ALTITUDE_CONTINUOUS_ERROR,
            ErrorType.PROPELLER_INTERMEIDATE_DEMAGE,
            ErrorType.FRAME_INTERMEIDATE_DEMAGE,
            ErrorType.ESC_CONTINUOUS_ERROR,
            ErrorType.MCU_CONTROL_CONTINUOUS_ERROR,
            ErrorType.MCU_ARITHMETIC_CONTINUOUS_ERROR,
            ErrorType.BATTERY_DEAD,

            ErrorType.MOTOR_BALANCE_PERMANENT_ERROR,
            ErrorType.MOTOR_RPM_PERMANENT_ERROR,
            ErrorType.SENSOR_GPS_PERMANENT_ERROR,
            ErrorType.SENSOR_GYRO_PERMANENT_ERROR,
            ErrorType.SENSOR_ACCELERATION_PERMANENT_ERROR,
            ErrorType.SENSOR_ALTITUDE_PERMANENT_ERROR,
//            ErrorType.PROPELLER_HEAVY_DEMAGE,
//            ErrorType.FRAME_HEAVY_DEMAGE,
//            ErrorType.ESC_PERMANENT_ERROR,
//            ErrorType.MCU_CONTROL_PERMANENT_ERROR,
//            ErrorType.MCU_ARITHMETIC_PERMANENT_ERROR
    };

    // 테스트 용.
//    private ErrorType[] errorTypesWeak = {
//            ErrorType.BATTERY_LIFE, ErrorType.BATTERY_LIFE, ErrorType.BATTERY_LIFE,
//            ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR, ErrorType.MOTOR_BALANCE_PASSING_ERROR,
//            ErrorType.MOTOR_BALANCE_PASSING_ERROR,
//            ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR,
//            ErrorType.MOTOR_RPM_PASSING_ERROR, ErrorType.MOTOR_RPM_PASSING_ERROR,
//            ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR, ErrorType.SENSOR_GPS_PASSING_ERROR,
//            ErrorType.SENSOR_GPS_PASSING_ERROR
//    };

    private ErrorType[] errorTypesOrdinary = {
            ErrorType.BATTERY_LIFE,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR,

            ErrorType.BATTERY_LIFE,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR,

            ErrorType.BATTERY_LIFE,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR,


            ErrorType.BATTERY_LOW,
            ErrorType.MOTOR_BALANCE_CONTINUOUS_ERROR,
            ErrorType.MOTOR_RPM_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GPS_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GYRO_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ACCELERATION_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ALTITUDE_CONTINUOUS_ERROR,
            ErrorType.PROPELLER_INTERMEIDATE_DEMAGE,
            ErrorType.FRAME_INTERMEIDATE_DEMAGE,
            ErrorType.ESC_CONTINUOUS_ERROR,
            ErrorType.MCU_CONTROL_CONTINUOUS_ERROR,
            ErrorType.MCU_ARITHMETIC_CONTINUOUS_ERROR,
            ErrorType.BATTERY_LOW,

            ErrorType.MOTOR_BALANCE_CONTINUOUS_ERROR,
            ErrorType.MOTOR_RPM_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GPS_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GYRO_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ACCELERATION_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ALTITUDE_CONTINUOUS_ERROR,
            ErrorType.PROPELLER_INTERMEIDATE_DEMAGE,
            ErrorType.FRAME_INTERMEIDATE_DEMAGE,
            ErrorType.ESC_CONTINUOUS_ERROR,
            ErrorType.MCU_CONTROL_CONTINUOUS_ERROR,
            ErrorType.MCU_ARITHMETIC_CONTINUOUS_ERROR,

            ErrorType.MOTOR_BALANCE_CONTINUOUS_ERROR,
            ErrorType.MOTOR_RPM_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GPS_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GYRO_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ACCELERATION_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ALTITUDE_CONTINUOUS_ERROR,
            ErrorType.PROPELLER_INTERMEIDATE_DEMAGE,
            ErrorType.FRAME_INTERMEIDATE_DEMAGE,
            ErrorType.ESC_CONTINUOUS_ERROR,
            ErrorType.MCU_CONTROL_CONTINUOUS_ERROR,
            ErrorType.MCU_ARITHMETIC_CONTINUOUS_ERROR,


            ErrorType.BATTERY_DEAD,
            ErrorType.MOTOR_BALANCE_PERMANENT_ERROR,
            ErrorType.MOTOR_RPM_PERMANENT_ERROR,
            ErrorType.SENSOR_GPS_PERMANENT_ERROR,
            ErrorType.SENSOR_GYRO_PERMANENT_ERROR,
            ErrorType.SENSOR_ACCELERATION_PERMANENT_ERROR,
            ErrorType.SENSOR_ALTITUDE_PERMANENT_ERROR,
            ErrorType.PROPELLER_HEAVY_DEMAGE,
            ErrorType.FRAME_HEAVY_DEMAGE,
            ErrorType.ESC_PERMANENT_ERROR,
            ErrorType.MCU_CONTROL_PERMANENT_ERROR,
            ErrorType.MCU_ARITHMETIC_PERMANENT_ERROR
    };

    private ErrorType[] errorTypesStrong = {
            ErrorType.BATTERY_LIFE,
            ErrorType.MOTOR_BALANCE_PASSING_ERROR,
            ErrorType.MOTOR_RPM_PASSING_ERROR,
            ErrorType.SENSOR_GPS_PASSING_ERROR,
            ErrorType.SENSOR_GYRO_PASSING_ERROR,
            ErrorType.SENSOR_ACCELERATION_PASSING_ERROR,
            ErrorType.SENSOR_ALTITUDE_PASSING_ERROR,
            ErrorType.PROPELLER_LIGHT_DEMAGE,
            ErrorType.FRAME_LIGHT_DEMAGE,
            ErrorType.ESC_PASSING_ERROR,
            ErrorType.MCU_CONTROL_PASSING_ERROR,
            ErrorType.MCU_ARITHMETIC_PASSING_ERROR,
            ErrorType.BATTERY_LOW,
            ErrorType.MOTOR_BALANCE_CONTINUOUS_ERROR,
            ErrorType.MOTOR_RPM_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GPS_CONTINUOUS_ERROR,
            ErrorType.SENSOR_GYRO_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ACCELERATION_CONTINUOUS_ERROR,
            ErrorType.SENSOR_ALTITUDE_CONTINUOUS_ERROR,
            ErrorType.PROPELLER_INTERMEIDATE_DEMAGE,
            ErrorType.FRAME_INTERMEIDATE_DEMAGE,
            ErrorType.ESC_CONTINUOUS_ERROR,
            ErrorType.MCU_CONTROL_CONTINUOUS_ERROR,
            ErrorType.MCU_ARITHMETIC_CONTINUOUS_ERROR,
            ErrorType.BATTERY_DEAD, ErrorType.BATTERY_DEAD,
            ErrorType.MOTOR_BALANCE_PERMANENT_ERROR, ErrorType.MOTOR_BALANCE_PERMANENT_ERROR,
            ErrorType.MOTOR_RPM_PERMANENT_ERROR, ErrorType.MOTOR_RPM_PERMANENT_ERROR,
            ErrorType.SENSOR_GPS_PERMANENT_ERROR, ErrorType.SENSOR_GPS_PERMANENT_ERROR,
            ErrorType.SENSOR_GYRO_PERMANENT_ERROR, ErrorType.SENSOR_GYRO_PERMANENT_ERROR,
            ErrorType.SENSOR_ACCELERATION_PERMANENT_ERROR, ErrorType.SENSOR_ACCELERATION_PERMANENT_ERROR,
            ErrorType.SENSOR_ALTITUDE_PERMANENT_ERROR, ErrorType.SENSOR_ALTITUDE_PERMANENT_ERROR,
            ErrorType.PROPELLER_HEAVY_DEMAGE, ErrorType.PROPELLER_HEAVY_DEMAGE,
            ErrorType.FRAME_HEAVY_DEMAGE, ErrorType.FRAME_HEAVY_DEMAGE,
            ErrorType.ESC_PERMANENT_ERROR, ErrorType.ESC_PERMANENT_ERROR,
            ErrorType.MCU_CONTROL_PERMANENT_ERROR, ErrorType.MCU_CONTROL_PERMANENT_ERROR,
            ErrorType.MCU_ARITHMETIC_PERMANENT_ERROR, ErrorType.MCU_ARITHMETIC_PERMANENT_ERROR
    };

    private final int errorEventSize = 60;
    private final int errorEventCount = 20;



    public TreeMap<Long, ErrorType> createRandomErrorEvent(long flightTime, ErrorLevel errorLevel){
        Map<Long, ErrorType> errorEventMap = new HashMap<Long, ErrorType>();

        /**
         * count를 늘려주면 더 많은 에러 이벤트를 장애 이벤트 배열에 추가할 수 있다.
         *
         */
        ErrorType[] errorTypes = this.getErrorTypes(errorLevel);

        ErrorType[] errorEvents = new ErrorType[errorEventSize];
        Arrays.fill(errorEvents, ErrorType.NORMAL);

        /**
         * 테스트용. 초기 장애 이벤트 배열의 요소를 BLOCK 이벤트로 초기화 시킨다.
         */
//        Arrays.fill(errorEvents, ErrorType.BATTERY_DEAD);

        for(int i=0; i < errorEventCount; i++){
            int minErrorTypeIndex = 0;
            int maxErrorTypeIndex = errorTypes.length - 1;

            /**
             * 장애 타입을 랜덤으로 생성.
             */
            int errorTypesIndex = MathUtils.getRandomIntNumber(minErrorTypeIndex, maxErrorTypeIndex);
            ErrorType errorType = errorTypes[errorTypesIndex];

            int minErrorEventsIndex = 0;
            int maxErrorEventsIndex = errorEvents.length - 1;

            /**
             * 전체 장애 이벤트 배열에 랜덤으로 장애 타입을 추가.
             */
            int errorEventsIndex = MathUtils.getRandomIntNumber(minErrorEventsIndex, maxErrorEventsIndex);

            if(errorEvents[errorEventsIndex] == ErrorType.NORMAL){
                errorEvents[errorEventsIndex] = errorType;
            }

        }

        for(int i=0; i < errorEventCount; i++){

            int minErrorEventsIndex = 0;
            int maxErrorEventsIndex = errorEvents.length - 1;

            /**
             * 전체 비행 시간(초)중에서 랜덤한 시간에 장애 이벤트 배열에서 랜덤하게 장애를 선출해서 MAP(비행시간, 장애 타입)을 생성.
             */
            long minFlightTime = 1;
            long maxFlightTime = flightTime;

            long atSeconds = MathUtils.getRandomLongNumber(minFlightTime, maxFlightTime);

            int happenedErrorEventsIndex = MathUtils.getRandomIntNumber(minErrorEventsIndex, maxErrorEventsIndex);

            if(!errorEventMap.containsKey(atSeconds)){
                errorEventMap.put(atSeconds, errorEvents[happenedErrorEventsIndex]);
            }

        }

        /**
         * 비행 시간 순으로 정렬.
         */
        TreeMap<Long, ErrorType> treeMap = new TreeMap<Long, ErrorType>(errorEventMap);

        return treeMap;
    }

    private ErrorType[] getErrorTypes(ErrorLevel errorLevel) {
        ErrorType[] errorTypes = null;

        if(errorLevel == ErrorLevel.WEAK){
            errorTypes = errorTypesWeak;
        }else if(errorLevel == ErrorLevel.ORDINARY){
            errorTypes = errorTypesOrdinary;
        }else if(errorLevel == ErrorLevel.STRONG){
            errorTypes = errorTypesStrong;
        }else{
            errorTypes = errorTypesWeak;
        }

        List<ErrorType> list = Arrays.asList(errorTypes);
        Collections.shuffle(list);
        ErrorType[] errorTypesShuffled = list.toArray(new ErrorType[list.size()]);

        return errorTypesShuffled;
    }

    /**
     * 에러 레벨을 통해서 에러 발생 카운트를 결정 한다.
     * - 에러 레벨이 높을수록 에러 발생 카운트는 길어짐. 기본 에러 이벤트 사이즈는 50.
     * - WEAK 일 때, 에러 카운트는 15회
     * - ORDINARY 일 때, 에러 카운트는 25회
     * - STRONG 일 때, 에러 카운트는 40회
     * @param errorLevel
     * @return
     */
//    private int getErrorEventCount(ErrorLevel errorLevel) {
//        int errorEventCount = 0;
//        if(errorLevel == ErrorLevel.WEAK){
//            errorEventCount = (int) (errorEventSize * 0.3);
//        }else if(errorLevel == ErrorLevel.ORDINARY){
//            errorEventCount = (int) (errorEventSize * 0.5);
//        }else if(errorLevel == ErrorLevel.STRONG){
//            errorEventCount = (int) (errorEventSize * 0.8);
//        }
//        return errorEventCount;
//    }


}
