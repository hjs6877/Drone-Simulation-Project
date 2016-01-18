package kr.co.korea;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ideapad on 2016-01-17.
 */
public class ClientSenderForDrone1Server extends Thread{
    Socket socket;
    DataOutputStream dos;
    String name;

    ClientSenderForDrone1Server(Socket socket, String name){
        this.socket = socket;
        this.name = name;
        try {
            dos = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){

        if(dos != null){
            try {
                dos.writeUTF(name);

                Scanner scanner = new Scanner(System.in);

                while(dos != null){
                    dos.writeUTF("[" + name + "]" + scanner.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
