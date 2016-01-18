package kr.co.korea;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ClientReceiverForController extends Thread {
    Socket socket;
    DataInputStream dis;

    ClientReceiverForController(Socket socket){
        this.socket = socket;

        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (dis != null){
            try {
                System.out.println(dis.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
