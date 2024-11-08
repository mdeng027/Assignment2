package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import edu.seg2105.client.common.ChatIF;
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

    /**
     * The interface type variable.  It allows the implementation of
     * the display method in the server.
     */
    ChatIF serverUI;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public EchoServer(int port, ChatIF serverUI) {
        super(port);
        this.serverUI = serverUI;
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
    @Override
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
    @Override
    synchronized protected void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected: " + client.toString());
    }

    /**
     * Implemented the hook method called each time an exception is thrown in a
     * ConnectionToClient thread.
     * The method may be overridden by subclasses but should remain synchronized.
     *
     * @param client     the client that raised the exception.
     * @param \Throwable the exception thrown.
     */
    @Override
    synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
        System.out.println("Client error: " + client.toString() + "exception" + exception.toString());
    }

    public void handleMessageFromServer(String message) {
        System.out.println("SERVER MSG > " + message);
        this.sendToAllClients("SERVER MSG > " + message);
    }

    public void handleMessageFromServerUI(String message) {
        if (message.startsWith("#")) {
            handleCommand(message);
        } else {
            handleMessageFromServer(message);
        }
    }

    public void handleCommand(String message) {
        if (!message.startsWith("#")) {
            sendToAllClients("SERVER MSG> " + message);
            return;
        } else if (message.startsWith("#")) {
            String[] args = message.split(" ");
            String command = args[0];
            switch (command) {
                case "#quit":
                    try {
                        serverUI.display("Terminating server");
                        close();
                        System.exit(0);
                    } catch (IOException e) {
                        System.exit(0);
                    }
                    break;

                case "#stop":
                    stopListening();
                    break;

                case "#close":
                    serverUI.display("Closing all client connections.");
                    try {
                        close();
                    } catch (IOException e) {
                        System.out.println("ERROR - Could not close connection.");
                    }
                    break;

                case "#start":
                    if (!this.isListening()) {
                        try {
                            listen();
                        } catch (Exception e) {
                            System.out.println("ERROR - Could not start listening for clients.");
                        }
                    } else {
                        System.out.println("ERROR - Already listening for clients.");
                    }
                    break;

                case "#setport":
                    if (!isListening() && getNumberOfClients() == 0) {
                        try {
                            int port = Integer.parseInt(args[1]);
                            setPort(port);
                            System.out.println("Port set to " + port);
                        } catch (Exception e) {
                            System.err.println("ERROR - Invalid port number.");
                        }
                    } else {
                        System.out.println("ERROR - Server must be closed and no clients connected to set port.");
                    }
                    break;

                case "#getport":
                    System.out.println("Current port: " + this.getPort());
                    break;

                default:
                    System.out.println("Invalid command: '" + command + "'");
                    break;
            }
        } else {
            sendToAllClients(message);
        }
    }

    //Class methods ***************************************************
}
//End of EchoServer class
