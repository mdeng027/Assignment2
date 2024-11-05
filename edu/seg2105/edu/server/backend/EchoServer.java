package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;

import java.io.IOException;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
    //Class variables *************************************************

    /**
     * The default port to listen on.
     */
    final public static int DEFAULT_PORT = 5556;
    // TODO change to 5555 after

    //Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public EchoServer(int port) {
        super(port);
    }


    //Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient
    (Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);
        this.sendToAllClients(msg);
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    /**
     * Implemented the hook method called each time a new client connection is
     * accepted. The default implementation does nothing.
     *
     * @param client the connection connected to the client.
     */
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client connected: " + client.toString());
    }

    /**
     * Implemented the hook method called each time a client disconnects.
     * The default implementation does nothing. The method
     * may be overridden by subclasses but should remain synchronized.
     *
     * @param client the connection with the client.
     */
    synchronized protected void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected: " + client.toString());
    }

    public void handleMessageFromServer(String message) {
    }

    public void handleMessageFromServerUI(String message) {
        try {
            if (message.startsWith("#")) {
                handleCommand(message);
            } else {
                handleMessageFromServer(message);
            }
        } catch (IOException e) {
            System.out.println("ERROR - Could not send message to server.");
        }
    }

    private void handleCommand(String message) {
        String[] args = message.split(" ");
        String command = args[0];
        switch (command) {
            case "#quit":
                this.close();
                break;

            case "#stop":
                this.stopListening();
                break;

            case "#close":
                this.close();
                break;

            case "#start":
                this.listen();
                break;

            case "#setport":
                try {
                    this.setPort(Integer.parseInt(args[1]));
                    System.out.println("Port set to " + getPort());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number. Please provide a valid integer.");
                }
                break;

            case "#getport":
                System.out.println("Current port: " + this.getPort());
                break;

            default:
                System.out.println("Invalid command: '" + command + "'");
                break;
        }
    }


//Class methods ***************************************************


}
//End of EchoServer class
