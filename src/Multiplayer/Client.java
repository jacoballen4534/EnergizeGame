package Multiplayer;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Client {
    private int serverPort;
    private InetAddress serverIPAddress;
    private String serverIPAddressString;
    private DatagramSocket socket;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10]; // Allocate a large array once and re-use it.
    private boolean listening = false;
    private Thread send, receive;
    private int clientID;//The server sends the client an clientID to use for further communication.



    //This is the host and serverPort of the server the client is connecting to.
    public Client(String host, int port) { //This takes params in form host = ip, serverPort = serverPort.
        this.serverIPAddressString = host;
        this.serverPort = port;
    }

    public Client(String host) { //This takes address in form ip:serverPort
        String[] tokens = host.split(":");
        if (tokens.length != 2) { //This will be an invalid address.
            return;
        }
        this.serverIPAddressString = tokens[0];
        try {
            this.serverPort = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace(); //This means the serverPort is invalid.
        }
    }


    public boolean connect() {
        try {
            this.serverIPAddress = InetAddress.getByName(serverIPAddressString);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }

        try {
            socket = new DatagramSocket();//This is the client socket. It can have a random serverPort as it will send data before receiving any.
            socket.connect(serverIPAddress, 4000);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }

        this.listening = true;
        this.receive = new Thread(() -> listen(), "Client Receiving Thread");
        this.receive.start();

        sendConnectionPacket(); //Send connection packet to send the server a message, so that the server can get the client address

        //Wait for a reply from the server to ensure it has this client serverPort.
        return true;
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


    public void process(DatagramPacket packet) { //Process the incoming packet
        String data = new String(packet.getData(), 0, packet.getLength());
        //This will be update instructions from the server.

//        dumpPacket(packet); //Debug
        StringBuilder consoleMessage = new StringBuilder();

            consoleMessage.append("\033[0;94m"); //Set Client print color to blue
            consoleMessage.append("\n-------------------------\n");
        if (data.startsWith(Server.PACKET_ID)) { //This means this client has successfully connected to the server and got given an id
            this.clientID = Integer.parseInt(data.split(Server.PACKET_ID + "|" + Server.PACKET_END)[1].trim());
            consoleMessage.append("Client @ ").append(this.serverIPAddressString).append(":").append(this.serverPort).append(" got given clientID: ").append(this.clientID).append("\n");
            consoleMessage.append("-------------------------\033[0m");
            System.out.println(consoleMessage);

        } else if (data.startsWith(Server.PACKET_PING)) {
            String message = Server.PACKET_PING + this.clientID + Server.PACKET_END;
            send(message.getBytes());
        } else if (data.startsWith(Server.PACKET_MESSAGE)) { //This is a message from the server
            consoleMessage = new StringBuilder();
            consoleMessage.append("\033[0;35m "); //Set Client print color to blue
            data = data.split(Server.PACKET_MESSAGE + "|" + Server.PACKET_END)[1].trim();
            consoleMessage.append(data).append("\n");
            System.out.println(consoleMessage);
        } else {
            consoleMessage.append("Unknown packet: ").append(data, 0, packet.getLength()).append("\n");
            consoleMessage.append("-------------------------\033[0m");
            System.out.println(consoleMessage);

        }

    }




    private void sendConnectionPacket() { //To send the server this clients address and port.
          send((Server.PACKET_CONNECT + Server.PACKET_END).getBytes());
    }

    private void disconnect() {
        send((Server.PACKET_DISCONNECT + this.clientID + Server.PACKET_END).getBytes());
    }


    public void send(final byte[] data) {
        this.send = new Thread("Client Send") {
            public void run() {
                assert (socket.isConnected());
                DatagramPacket packet = new DatagramPacket(data, data.length, serverIPAddress, serverPort);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        this.send.start();


    }



    private void dumpPacket(DatagramPacket packet) {
        String data = new String(packet.getData());
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        StringBuilder consoleMessage = new StringBuilder();

        consoleMessage.append("\033[0;94m"); //Set Client print color to blue
        consoleMessage.append("-------------------------\n");
        consoleMessage.append("CLIENT PACKET DUMP:\n");
        consoleMessage.append("\tFrom: ").append(address.getHostAddress()).append(":").append(port).append("\n\n");
        consoleMessage.append("\tContents:\n");
        consoleMessage.append("\t\t").append(data, 0, packet.getLength());

        consoleMessage.append("\n-------------------------");
        consoleMessage.append("\033[0m"); //Reset the color
        System.out.println(consoleMessage);
    }
}

