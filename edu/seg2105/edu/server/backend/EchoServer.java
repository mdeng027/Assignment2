package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object-Oriented Software Engineering" and is issued under the open-source
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
     * The login key of the connecting client.
     */
    private final String loginKey = "loginID";

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
     *
     *
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client.getInfo(loginKey));

        String msgStr = (String) msg;

        if (msgStr.startsWith("#login")) {
            if (!(client.getInfo(loginKey) == null)) { // if loginKey is not null, thus not first time running #login
                try {
                    client.sendToClient("ERROR - #login is only allowed as the first command");
                    client.close(); // disconnect the client connection
                } catch (IOException e) {
                    System.out.println("ERROR - Could not send message to client");
                }
            } else {
                String loginID = msgStr.substring("#login ".length()).trim();
                client.setInfo(loginKey, loginID);
                serverUI.display(loginID + "has logged on");
            }
        } else { //
            String loginID = (String) client.getInfo(loginKey);
            String message = loginID + "> " + msgStr;
            this.sendToAllClients(message);
        }
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
        System.out.println("Client connected");
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
        System.out.println("Client disconnected.");
    }

    /**
     * Implemented the hook method called each time an exception is thrown in a
     * ConnectionToClient thread.
     * The method may be overridden by subclasses but should remain synchronized.
     *
     * @param client    the client that raised the exception.
     * @param exception Throwable the exception thrown.
     */
    @Override
    synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
        clientDisconnected(client);
    }

    public void handleMessageFromServerUI(String message) {
        if (message.startsWith("#")) {
            handleCommand(message);
        } else {
            serverUI.display(message);
            sendToAllClients("SERVER MSG > " + message);
        }
    }

    public void handleCommand(String message) {
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
                    if (args.length > 1) {
                        try {
                            int port = Integer.parseInt(args[1]);
                            super.setPort(port);
                            System.out.println("Port set to " + getPort());
                        } catch (NumberFormatException e) {
                            serverUI.display("ERROR - Invalid port number. Please provide a valid integer.");
                        }
                    } else {
                        serverUI.display("To set port: #setport <port>");
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
    }
}

//Class methods ***************************************************

//End of EchoServer class
