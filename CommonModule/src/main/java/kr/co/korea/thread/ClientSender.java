package kr.co.korea.thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by kjs on 2016-02-12.
 */
public class ClientSender extends Thread{
    private Socket socket;
    private ObjectOutputStream objectOutputStream;



    public ClientSender(Socket socket) {
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        } catch(Exception e) {}
    }

    public void run() {
        while(objectOutputStream != null){

        }
    } // run()

    public synchronized void sendDroneToController(Object object) throws IOException {
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }
}
