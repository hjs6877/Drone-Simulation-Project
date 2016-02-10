package kr.co.korea.proecessor;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-21.
 */
public interface Processor {
    public void doProcess(ObjectOutputStream objectOutputStream);
}
