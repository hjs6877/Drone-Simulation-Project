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
}
