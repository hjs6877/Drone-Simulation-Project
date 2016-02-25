import kr.co.korea.error.ErrorType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ideapad on 2016-02-25.
 */
public class ErrorTypeOldTest {
    @Test
    public void getPointTest(){
        System.out.println(ErrorType.BATTERY_LIFE);
        assertEquals(1.0, ErrorType.BATTERY_LIFE.getPoint(), 0.0);

    }
}
