package Multiplayer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private int port;
    private Thread listenThread;
    private boolean listening = false;
    private DatagramSocket socket;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10]; // Allocate a large array once and re-use it.
    private Set<ServerClient> clients = new HashSet<ServerClient>(); //Hold clients in a hash set to quickly get client from name, and to remove any duplicates.

    public Server(int port) { //Create a new server with the given port number
        this.port = port;
    }


    public void start() {
        try {
            this.socket = new DatagramSocket(this.port);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        System.out.print("\033[0;32m"); //Set Server print color to
        System.out.println("Started server on port 8192...");


        this.listening = true;
        this.listenThread = new Thread(() -> listen(), "Server Listen Thread");
        this.listenThread.start();

        System.out.println("Server is listening...");
        System.out.print("\033[0m"); //Reset the color


    }

    public void listen() {
        while (this.listening) {
            DatagramPacket packet = new DatagramPacket(this.receivedDataBuffer, MAX_PACKET_SIZE);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            process(packet);
        }
    }

    public void process(DatagramPacket packet) {
        byte[] data = packet.getData();
        //The server can get the address and port that the message was sent from. This allows the server to reply to the client.
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        dumpPacket(packet); //This is for debug

        if (data[0] == 0x40 && data[1] == 0x40) { //This is a PACKET_HEADER
            switch (data[2]) {
                case 0x01:
                    clients.add(new ServerClient(packet.getAddress(), packet.getPort()));
                    break;
                case 2:
                    //Packet type 2
                    break;
                case 3:
                    //Packet type 3
                    break;
            }
        }

    }

    public void send(byte[] data, InetAddress address, int port) {
        assert (socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void dumpPacket(DatagramPacket packet) {
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        System.out.print("\033[0;32m"); //Set Server print color to

        System.out.println("-------------------------");
        System.out.println("PACKET:");
        System.out.println("\tFrom: " + address.getHostAddress() + ":" + port + "\n");
        System.out.println("\tContents:");
        System.out.print("\t\t");
        for (int i = 0; i < packet.getLength(); i++) {
            System.out.printf("%x ", data[i]);
            if ((i+1) % 16 == 0) { //Put a 16 bytes per line
                System.out.print("\n\t\t");
            }
        }
        System.out.println();
        System.out.println("-------------------------");

        System.out.print("\033[0m"); //Reset the color


    }


}
