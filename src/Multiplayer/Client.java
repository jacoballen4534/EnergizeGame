package Multiplayer;

import java.io.IOException;
import java.net.*;

public class Client {

    private int port;
    private InetAddress serverAddress;
    private String ipAddress;
    private DatagramSocket socket;


    //This is the host and port of the server the client is connecting to.
    public Client(String host, int port) { //This takes params in form host = ip, port = port.
        this.ipAddress = host;
        this.port = port;
    }

    public Client(String host) { //This takes address in form ip:port
        String[] tokens = host.split(":");
        if (tokens.length != 2) { //This will be an invalid address.
            return;
        }
        this.ipAddress = tokens[0];
        try {
            this.port = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace(); //This means the port is invalid.
        }



    }


    public boolean connect() {
        try {
            serverAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }

        try {
            socket = new DatagramSocket();//This is the client socket. It can have a random port as it will send data before receiving any.
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        sendConnectionPacket(); //Send connection packet to send the server a message, so that the server can get the client addredd
        //Wait for a reply from the server to ensure it has this client port.
        return true;
    }


    private void sendConnectionPacket() {
        byte[] data = "ConnectionPacket".getBytes();
        send(data);
    }

    public void send(byte[] data) {
        assert (socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

