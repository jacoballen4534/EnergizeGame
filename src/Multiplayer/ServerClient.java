package Multiplayer;

import java.net.InetAddress;

public class ServerClient {

    public int userID;
    public InetAddress address;
    public int port;
    public boolean status = false; //is connected.
    public int attempt;
    private static int nextUserID = 1;

    public ServerClient(InetAddress address, int port) {
        this.userID = nextUserID++;
        this.address = address;
        this.port = port;
        this.status = true;
        this.attempt = 0;
    }
}
