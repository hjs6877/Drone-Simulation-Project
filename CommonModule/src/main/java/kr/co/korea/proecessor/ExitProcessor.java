package kr.co.korea.proecessor;

import java.io.Serializable;

/**
 * Created by ideapad on 2016-01-21.
 */
public class ExitProcessor implements Processor, Serializable {
    public void doProcess() {
        System.out.println("Controller부터 exit 명령을 전달받았습니다. 접속을 종료합니다..");
        System.exit(-1);
    }
}
