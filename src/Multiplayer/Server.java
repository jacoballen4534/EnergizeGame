package Multiplayer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
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
    private InetAddress serverAddress;
    private String serverAddressString;

    public Server(String serverAddress, int port) { //Create a new server with the given port number
        this.port = port;
        this.serverAddressString = serverAddress;
    }


    public void start() {
        try {
            serverAddress = InetAddress.getByName(serverAddressString);
            this.socket = new DatagramSocket(this.port, serverAddress);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Server can be reached on: " + serverAddress.getHostAddress() + ":" + this.port);

        StringBuilder consoleMessage = new StringBuilder();

        consoleMessage.append("\033[0;92m\n"); //Set Server print color to green
        try {
            consoleMessage.append("Started server. LocalHost: ").append(InetAddress.getLocalHost()).append("\tHost Name: ").append(InetAddress.getLocalHost().getHostName()).append("\t Port: ").append(this.socket.getLocalPort()).append("...\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        consoleMessage.append("\033[0m"); //Reset the color

        run = new Thread(this, "server");
        run.start();

        consoleMessage.append("\033[0;92m"); //Set Server print color to
        consoleMessage.append("Server is setup and listening...");
        consoleMessage.append("\033[0m"); //Reset the color
        System.out.println(consoleMessage);
    }


    public void process(DatagramPacket packet) {
        byte[] data = packet.getData();
        //The server can get the address and port that the message was sent from. This allows the server to reply to the client.
        InetAddress sendersAddress = packet.getAddress();
        int sendersPort = packet.getPort();

        dumpPacket(packet); //This is for debug
        StringBuilder consoleMessage = new StringBuilder();

        if (data[0] == 0x40 && data[1] == 0x20) { //This is a PACKET_HEADER
            consoleMessage.append("\033[0;92m"); //Set Server print color to green
            switch (data[2]) {
                case 0x01:
                    consoleMessage.append("\n-------------------------\n");
                    consoleMessage.append("Connect Packet Received\n");
                    ServerClient newClient = new ServerClient(sendersAddress, sendersPort);
                    if (!clients.contains(newClient)) {
                        this.clients.add(newClient);
                        //Send welcome message to the new client
                        String welcomeMessage = "Welcome " + sendersAddress + ":" + sendersPort;
                        this.send(welcomeMessage.getBytes(), sendersAddress, sendersPort);

                        consoleMessage.append("\nNew Client Added\n");
                        consoleMessage.append("\tServer Client LIST:\n");
                        for (ServerClient client : clients) {
                            consoleMessage.append("\tConnection Status: ").append(client.status ? "\033[1;32m" + "Connected" : "\033[1;31m" + "Disconnected");
                            consoleMessage.append("\033[0;92m");
                            consoleMessage.append("\tID: ").append(client.userID);
                            consoleMessage.append("\tConnected from : ").append(client.address.getHostAddress()).append(":").append(client.port).append("\n");
                        }

                    } else {
                        consoleMessage.append("Duplicate Client Connection Request\n");
                    }
                    break;
                case 2:
                    //Packet type 2
                    break;
                case 3:
                    //Packet type 3
                    break;
                default:
                    consoleMessage.append("Unknown packet header received:\n");
            }
            consoleMessage.append("-------------------------");

            consoleMessage.append("\033[0m"); //Reset the color
            System.out.println(consoleMessage);
        }

    }

    public void send(final byte[] data, final InetAddress clientAddress, final int clientPort) {
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
        StringBuilder consoleMessage = new StringBuilder();

        consoleMessage.append("\033[0;92m"); //Set Server print color to green
        consoleMessage.append("-------------------------\n");
        consoleMessage.append("SERVER PACKET DUMP:\n");
        consoleMessage.append("\tFrom: " + address.getHostAddress() + ":" + port + "\n\n");
        consoleMessage.append("\tContents:\n");
        consoleMessage.append("\t\t");
        for (int i = 0; i < packet.getLength(); i++) {
            consoleMessage.append(String.format("%x ", data[i]));
            if ((i+1) % 16 == 0) { //Put a 16 bytes per line
                consoleMessage.append("\n\t\t");
            }
        }
        consoleMessage.append("\n-------------------------");

        consoleMessage.append("\033[0m"); //Reset the color
        System.out.println(consoleMessage);


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
