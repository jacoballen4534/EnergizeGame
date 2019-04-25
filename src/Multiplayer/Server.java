package Multiplayer;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server implements Runnable {
    public static final String PACKET_CONNECT = "/c/";
    public static final String PACKET_DISCONNECT = "/d/";
    public static final String PACKET_PING = "/p/";
    public static final String PACKET_ID = "/id/";
    public static final String PACKET_MESSAGE = "/m/";
    public static final String PACKET_END = "/e/";//Used to indicate the end of a packet


    private int port;
    private Thread run, manage, send, receive;
    private boolean running = false;
    private DatagramSocket socket;
    private List<ServerClient> clients = new ArrayList<>(); //Hold clients in a hash set to quickly get client from name, and to remove any duplicates.
    private InetAddress serverAddress;
    private String serverAddressString;
    private List<Integer> clientResponse = new ArrayList<>();
    private final int MAX_ATTEMPTS = 5;
    private boolean raw = false;

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
        consoleMessage.append("Started server @ ").append(serverAddressString).append(":").append(port).append("...\n");
        consoleMessage.append("\033[0m"); //Reset the color

        run = new Thread(this, "server");
        run.start();

        consoleMessage.append("\033[0;92m"); //Set Server print color to
        consoleMessage.append("Server is setup and listening...\n");
        consoleMessage.append("\033[0m"); //Reset the color
        System.out.println(consoleMessage);
    }

    public void closeServer() {
        for (int i = 0; i < clients.size(); i++) {
            disconnect(clients.get(i).userID, true);
        }
        this.running = false;
        socket.close();
    }


    public void process(DatagramPacket packet) {
        String data = new String(packet.getData());
        //The server can get the address and port that the message was sent from. This allows the server to reply to the client.
        InetAddress sendersAddress = packet.getAddress();
        int sendersPort = packet.getPort();

        if (raw) {
            dumpPacket(packet); //Debug
        }
        StringBuilder consoleMessage = new StringBuilder();


        consoleMessage.append("\033[0;92m"); //Set Server print color to green
        consoleMessage.append("\n-------------------------\n");
        if (data.startsWith(PACKET_CONNECT)) { //Connection packet
            consoleMessage.append("New Client Created\n");

            ServerClient newClient = new ServerClient(sendersAddress, sendersPort);
            this.clients.add(newClient);

            //Send the client their id.
            String message = PACKET_ID + newClient.userID + PACKET_END;
            this.send(message.getBytes(), sendersAddress, sendersPort);
            consoleMessage.append("Sent the new client their id\n");

            consoleMessage.append("-------------------------\033[0m");
            System.out.println(consoleMessage);
        } else if (data.startsWith(PACKET_DISCONNECT)) { //Disconnection packet
            int id = Integer.parseInt(data.split(PACKET_DISCONNECT + "|" + PACKET_END)[1].trim());
            this.disconnect(id, true);

            consoleMessage.append("-------------------------\033[0m");
            System.out.println(consoleMessage);
        } else if (data.startsWith(PACKET_PING)) {
            int id = Integer.parseInt(data.split(PACKET_PING + "|" + PACKET_END)[1].trim());
            clientResponse.add(id);
        } else {
            consoleMessage.append("Unknown packet: ").append(data, 0, packet.getLength()).append("\n");
            consoleMessage.append("-------------------------\033[0m");
            System.out.println(consoleMessage);
        }


    }

    public void send(final byte[] data, final InetAddress clientAddress, final int clientPort) {

        send = new Thread("Server Send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    private void sendToAll(byte[] data) {
        for (int i = 0; i < clients.size(); i++) {
            ServerClient client = clients.get(i);
            send(data, client.address, client.port);
        }
    }

    private void disconnect(int id, boolean status) { //Id of client to disconnected. Status represents if they left or timed out / kicked
        ServerClient serverClient = null;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).userID == id) {
                serverClient = clients.get(i);
                clients.remove(i);
                break;
            }
        }
        String disconnectionMessage = "\033[0;92m";
        if (serverClient != null) {
            if (status) {
                disconnectionMessage += "Client " + id + " @ " + serverClient.address + ":" + serverClient.port + " disconnected.";
            } else {
                disconnectionMessage += "Client " + id + " @ " + serverClient.address + ":" + serverClient.port + " timed out.";
            }
        }
        disconnectionMessage += "\033[0m";
        System.out.println(disconnectionMessage);
    }


    private void dumpPacket(DatagramPacket packet) {
        String data = new String(packet.getData());
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        StringBuilder consoleMessage = new StringBuilder();

        consoleMessage.append("\033[0;92m"); //Set Server print color to green
        consoleMessage.append("-------------------------\n");
        consoleMessage.append("SERVER PACKET DUMP:\n");
        consoleMessage.append("\tFrom: ").append(address.getHostAddress()).append(":").append(port).append("\n\n");
        consoleMessage.append("\tContents:\n");
        consoleMessage.append("\t\t").append(data.trim());
        consoleMessage.append("\n-------------------------");

        consoleMessage.append("\033[0m"); //Reset the color
        System.out.println(consoleMessage);


    }


    public void run() { //Sets up server and then closes
        this.running = true;
        mannageClients();
        receive();
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String text = scanner.nextLine();
            if (!text.startsWith("/")) {
                sendToAll((PACKET_MESSAGE + "From Server: " + text + PACKET_END + "\033[0m").getBytes());
            } else {
                text = text.substring(1);

                if (text.equals("Clients")) {
                    StringBuilder message = new StringBuilder();
                    message.append("\033[0;35m Clients\n====================\n");
                    for (int i = 0; i < clients.size(); i++) {
                        ServerClient client = clients.get(i);
                        message.append(client.userID).append(" @ ").append(client.address).append(":").append(client.port).append("\n");
                    }
                    message.append("\033[0m");
                    System.out.println(message);
                } else if (text.equals("Raw")) {
                    this.raw = !this.raw;
                    if (this.raw) {
                        System.out.println("\033[0;35m Raw Mode On \033[0m");
                    } else {
                        System.out.println("\033[0;35m Raw Mode Off \033[0m");
                    }
                } else if (text.startsWith("Kick")) {
                    String name = text.split(" ")[1];
                    int id = -1;
                    try {
                        id = Integer.parseInt(name);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    boolean found = false;
                    for (int i = 0; i < clients.size(); i++) {
                        if (clients.get(i).userID == id) {
                            disconnect(id, true);
                            found = true;
                            break;
                        }
                    }
                    if (!found) System.out.println("\033[0;35m There is no client with ID: " + id + "\033[0m");
                } else if (text.equals("Help")) {
                    printHelp();
                } else if (text.equals("Close Server")) {
                    closeServer();
                } else {
                    System.out.println("\033[0;35m Unknown Comand \033[0m");
                    printHelp();
                }
            }
        }
    }

    private void printHelp() {
        StringBuilder message = new StringBuilder();
        message.append("\033[0;35m Here Are The Server Commandes:\n==========================================\n");
        message.append("/Raw - Toggles Server datadumps\n");
        message.append("/Clients - Shows a list of all connected clients\n");
        message.append("/Kick [ID] - Kicks the client the with specified Id\n");
        message.append("/Help - Shows this help message\n");
        message.append("/Close Server - Shuts down the server\n");
        message.append("========================================== \033[0m");
        System.out.println(message);
    }

    private void mannageClients() { //Ensure clients are still connected ect...
        this.manage = new Thread("Server Manage Clients") {
            public void run() {
                while(running) {
                    String message = PACKET_PING  + PACKET_END;
                    sendToAll(message.getBytes());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < clients.size(); i++) {
                        ServerClient client = clients.get(i);
                        if (!clientResponse.contains(client.userID)) { //If a client doesnt respond to a ping. try again
                            if (client.attempt >= MAX_ATTEMPTS) {
                                disconnect(client.userID, false);//Didnt respond to 10 pings so disconnect the client
                            } else {
                                client.attempt++;
                            }
                        } else {
                            clientResponse.remove(new Integer(client.userID));
                            client.attempt = 0;
                        }
                    }
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
                    } catch (SocketException e) {
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
