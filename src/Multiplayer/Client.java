package Multiplayer;

import java.io.IOException;
import java.net.*;

public class Client {

    //Packet constants
    private final static byte[] PACKET_HEADER = new byte[] {0x40, 0x20}; //TODO: Make a list of different headers. This can be any identifier.
    private final static byte PACKET_TYPE_CONNECT = 0x01;


    private int serverPort;
    private InetAddress serverAddress;
    private String ipAddress;
    private DatagramSocket socket;



    //This is the host and serverPort of the server the client is connecting to.
    public Client(String host, int port) { //This takes params in form host = ip, serverPort = serverPort.
        this.ipAddress = host;
        this.serverPort = port;
    }

    public Client(String host) { //This takes address in form ip:serverPort
        String[] tokens = host.split(":");
        if (tokens.length != 2) { //This will be an invalid address.
            return;
        }
        this.ipAddress = tokens[0];
        try {
            this.serverPort = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace(); //This means the serverPort is invalid.
        }
    }


    public boolean connect() {
        try {
            this.serverAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }

        try {
            socket = new DatagramSocket();//This is the client socket. It can have a random serverPort as it will send data before receiving any.
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        sendConnectionPacket(); //Send connection packet to send the server a message, so that the server can get the client addredd
        //Wait for a reply from the server to ensure it has this client serverPort.
        return true;
    }


    private void sendConnectionPacket() {
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(PACKET_TYPE_CONNECT);
        send(writer.getBuffer());
    }

    public void send(byte[] data) {
        assert (socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

