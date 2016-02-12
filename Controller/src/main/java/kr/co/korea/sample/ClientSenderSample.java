package kr.co.korea.sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by kjs on 2016-02-12.
 */
public class ClientSenderSample extends Thread {
    Socket socket;

    private ObjectOutputStream objectOutputStream;
    String name;

    public ClientSenderSample(Socket socket, String name) {
        this.socket = socket;
        try {
            System.out.println("서버에 연결되었습니다.3");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.name = name;
            System.out.println("서버에 연결되었습니다.4");
        } catch(Exception e) {}
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            if(objectOutputStream!=null) {
                objectOutputStream.writeUTF(name);
            }

            while(objectOutputStream!=null) {
                objectOutputStream.writeUTF("["+name+"]"+scanner.nextLine());					}
        } catch(IOException e) {}
    } // run()
}
