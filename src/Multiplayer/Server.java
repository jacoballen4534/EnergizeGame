package Multiplayer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Server implements Runnable {
    private int port;
    private Thread run, manage, send, receive;
    private boolean running = false;
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

        System.out.print("\033[0;92m"); //Set Server print color to green
        System.out.println("Started server on port " + this.port + "...");
        System.out.print("\033[0m"); //Reset the color

        run = new Thread(this, "server");
        run.start();

        System.out.print("\033[0;92m"); //Set Server print color to
        System.out.println("Server is setup and listening...");
        System.out.print("\033[0m"); //Reset the color
    }


    public void process(DatagramPacket packet) {
        byte[] data = packet.getData();
        //The server can get the address and port that the message was sent from. This allows the server to reply to the client.
        InetAddress sendersAddress = packet.getAddress();
        int sendersPort = packet.getPort();

        dumpPacket(packet); //This is for debug

        if (data[0] == 0x40 && data[1] == 0x20) { //This is a PACKET_HEADER
            System.out.print("\033[0;92m"); //Set Server print color to green
            switch (data[2]) {
                case 0x01:
                    System.out.println("\n-------------------------");
                    System.out.println("Adding a new client:\n\tAddress: " + sendersAddress + "\n\tPort: " + sendersPort);
                    System.out.println("-------------------------");
                    this.clients.add(new ServerClient(sendersAddress, sendersPort));
                    System.out.println("\n-------------------------");

                    System.out.println("List of Currently Connected Clients:");
                    for (ServerClient client : clients) {
                        if (client.status) {
                            System.out.println("\tID: " + client.userID);
                            System.out.println("\tConnected on : " + client.address.getHostAddress() + ":" + client.port);
                        }
                    }
                    System.out.println("\n-------------------------");

                    //Send welcome message to the new client
                    String welcomeMessage = "Welcome " + sendersAddress + ":" + sendersPort;
                    this.send(welcomeMessage.getBytes(), sendersAddress, sendersPort);
                    break;
                case 2:
                    //Packet type 2
                    break;
                case 3:
                    //Packet type 3
                    break;
                default:
                    System.out.println("\n-------------------------");
                    System.out.println("Unknown packet header received:");
                    System.out.println("-------------------------");

            }
            System.out.print("\033[0m"); //Reset the color
        }

    }

    public void send(byte[] data, InetAddress clientAddress, int clientPort) {
        assert (socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
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

        System.out.print("\033[0;92m"); //Set Server print color to green
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


    public void run() { //Sets up server and then closes
        this.running = true;
        mannageClients();
        receive();
    }

    private void mannageClients() { //Ensure clients are still connected ect...
        this.manage = new Thread("Server Manage Clients") {
            public void run() {
                while(running) {

                }
            }
        };
        this.manage.start();
    }

    private void receive() {
        this.receive = new Thread("Server Receive") {
            public void run() {
                while(running) {
                    byte[] data = new byte[1024]; //Allow 1kb max
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
                }
            }
        };
        this.receive.start();
    }


}
