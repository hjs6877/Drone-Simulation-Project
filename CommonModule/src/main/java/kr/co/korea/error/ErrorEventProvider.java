package kr.co.korea.error;

import kr.co.korea.util.MathUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ideapad on 2016-02-01.
 */
public class ErrorEventProvider {
    /**
     *  장애 타입을 weak, ordinary, strong 순으로 구분하여 생성. 높은 장애 등급이 많을 수록 strong. 갯수는 적절히 조정.
     *  - errorTypesWeak : 4, 3, 2, 1, 0
     *  - errorTypesOrdinary : 4, 4, 3, 3, 1
     *  - errorTypesStrong : 3, 3, 3, 3, 3
     */
    private ErrorType[] errorTypesWeak = {
            ErrorType.TRIVIAL, ErrorType.TRIVIAL, ErrorType.TRIVIAL, ErrorType.TRIVIAL,
            ErrorType.MINOR, ErrorType.MINOR, ErrorType.MINOR,
            ErrorType.MAJOR, ErrorType.MAJOR,
            ErrorType.CRITICAL
    };
    private ErrorType[] errorTypesOrdinary = {
            ErrorType.TRIVIAL, ErrorType.TRIVIAL, ErrorType.TRIVIAL, ErrorType.TRIVIAL,
            ErrorType.MINOR, ErrorType.MINOR, ErrorType.MINOR, ErrorType.MINOR,
            ErrorType.MAJOR, ErrorType.MAJOR, ErrorType.MAJOR,
            ErrorType.CRITICAL, ErrorType.CRITICAL, ErrorType.CRITICAL,
            ErrorType.BLOCK
    };
    private ErrorType[] errorTypesStrong = {
            ErrorType.TRIVIAL, ErrorType.TRIVIAL, ErrorType.TRIVIAL,
            ErrorType.MINOR, ErrorType.MINOR, ErrorType.MINOR,
            ErrorType.MAJOR, ErrorType.MAJOR, ErrorType.MAJOR,
            ErrorType.CRITICAL, ErrorType.CRITICAL, ErrorType.CRITICAL,
            ErrorType.BLOCK, ErrorType.BLOCK, ErrorType.BLOCK
    };

    private final int errorEventSize = 30;
    private final int errorEventCount = 15;

    public TreeMap<Long, ErrorType> createRandomErrorEvent(long flightTime, ErrorLevel errorLevel){
        Map<Long, ErrorType> errorEventMap = new HashMap<Long, ErrorType>();

        ErrorType[] errorTypes = this.getErrorTypes(errorLevel);

        ErrorType[] errorEvents = new ErrorType[errorEventSize];
        Arrays.fill(errorEvents, ErrorType.NORMAL);

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

        return errorTypes;
    }


}
