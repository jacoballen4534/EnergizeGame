package Multiplayer;

import java.io.IOException;
import java.net.*;

public class Client {

    //Packet constants
    private final static byte[] PACKET_HEADER = new byte[] {0x40, 0x20}; //TODO: Make a list of different headers. This can be any identifier.
    private final static byte PACKET_TYPE_CONNECT = 0x01;


    private int serverPort;
    private InetAddress serverIPAddress;
    private String serverIPAddressString;
    private DatagramSocket socket;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10]; // Allocate a large array once and re-use it.
    private boolean listening = false;
    private Thread send, receive;



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
            System.out.println("Client socket connected = " + socket.isConnected());
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }

        StringBuilder consoleMessage = new StringBuilder();

        consoleMessage.append("\033[0;34m"); //Set Client print color to blue
        consoleMessage.append("\n-------------------------\n");
        consoleMessage.append("Connected to server:\n");
        consoleMessage.append("\t").append(this.serverIPAddressString).append(":").append(this.serverPort).append("\n");
        consoleMessage.append("-------------------------");
        consoleMessage.append("\033[0m"); //Reset the color
        System.out.println(consoleMessage);


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
        byte[] data = packet.getData();
        //This will be update instructions from the server.

        dumpPacket(packet); //This is for debug
        StringBuilder consoleMessage = new StringBuilder();

        if (data[0] == 0x40 && data[1] == 0x20) { //This is a PACKET_HEADER
            consoleMessage.append("\033[0;94m"); //Set Client print color to blue
            consoleMessage.append("\n-------------------------\n");
            switch (data[2]) {
                case 1:
                    consoleMessage.append("Instruction type 1\n");
                    //Instruction type 1
                    break;
                case 2:
                    //Instruction type 2
                    break;
                case 3:
                    //Instruction type 3
                    break;
                default:
                    consoleMessage.append("Unknown packet header received:\n");

            }
            consoleMessage.append("-------------------------");
            consoleMessage.append("\033[0m"); //Reset the color
            System.out.println(consoleMessage);
        }
    }




    private void sendConnectionPacket() {
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(PACKET_TYPE_CONNECT);
        send(writer.getBuffer());
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
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        StringBuilder consoleMessage = new StringBuilder();

        consoleMessage.append("\033[0;94m"); //Set Client print color to blue
        consoleMessage.append("-------------------------\n");
        consoleMessage.append("CLIENT PACKET DUMP:\n");
        consoleMessage.append("\tFrom: ").append(address.getHostAddress()).append(":").append(port).append("\n\n");
        consoleMessage.append("\tContents:\n");
        consoleMessage.append("\t\t");
        for (int i = 0; i < packet.getLength(); i++) {
            consoleMessage.append(String.format("%c", data[i]));
        }
        consoleMessage.append("\n-------------------------");
        consoleMessage.append("\033[0m"); //Reset the color
        System.out.println(consoleMessage);
    }
}

