package kr.co.korea.socket;

import kr.co.korea.domain.DroneSetting;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-20. 현재 사용 안함.
 */
public class ControllerServerSender extends Thread {
    private ObjectOutputStream objectOutputStream;
    private DroneSetting setting;

    public ControllerServerSender(ObjectOutputStream objectOutputStream, DroneSetting setting){
        this.objectOutputStream = objectOutputStream;
        this.setting = setting;
    }

    public void run(){
        try {
            objectOutputStream.writeObject(setting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
