package kr.co.korea;

import java.net.Socket;

/**
 * Created by ideapad on 2016-01-17.
 */
public class MercuryClient {
    private String serverIp = "127.0.0.1";
    private int serverPort = 5555;
    private Socket socket;

    public static void main(String[] args){
        MercuryClient mercuryClient = new MercuryClient();
        mercuryClient.connectToController();
    }

    public void connectToController(){

    }
}
