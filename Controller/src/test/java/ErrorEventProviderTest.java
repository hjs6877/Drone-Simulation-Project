import kr.co.korea.domain.FlightStatus;
import kr.co.korea.error.ErrorEventProvider;
import kr.co.korea.error.ErrorLevel;
import kr.co.korea.error.ErrorType;
import org.junit.Test;

import java.util.Map;

/**
 * Created by ideapad on 2016-02-01.
 */
public class ErrorEventProviderTest {
    @Test
    public void createRandomErrorEventTest(){
        ErrorEventProvider errorEventProvider = new ErrorEventProvider();
        Map<Long, ErrorType> errorEventMap = errorEventProvider.createRandomErrorEvent(500, ErrorLevel.WEAK);

        System.out.println(errorEventMap);
    }

    @Test
    public void calculateAddErrorTypeTest(){
        int startTime = 1;
        int flightTime = 500;

        ErrorEventProvider errorEventProvider = new ErrorEventProvider();
        Map<Long, ErrorType> errorEventMap = errorEventProvider.createRandomErrorEvent(flightTime, ErrorLevel.WEAK);

        System.out.println(errorEventMap);

        FlightStatus flightStatus = new FlightStatus();

        for(long i=startTime; i<=flightTime; i++){
            ErrorType errorType = errorEventMap.get(i);
            if(this.isExistErrorEvent(errorType)){
                System.out.println(i + "초에 에러 이벤트 발생: " + errorType);


                flightStatus.addErrorEvent(errorType);

                if(flightStatus.hasThreshholdErrorEvent()){
                    System.out.println("심각한 장애 발생으로 인해 리더 교체 프로세스 실시!!!!!!!!!!!!!!!!!!!!!!");
                    break;
                }
            }
        }

        System.out.println("###### 장애 이벤트 현황 ######");
        System.out.println("errorEventList: " + flightStatus.getErrorEventList());
        System.out.println("ErrorEventPointList: " + flightStatus.getErrorEventPointList().size());
        System.out.println("getTotalErrorPoint: " + flightStatus.getTotalErrorPoint());
    }

    private boolean isExistErrorEvent(ErrorType errorType) {
        return (errorType != null) && (errorType != ErrorType.NORMAL);
    }
}
