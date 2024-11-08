// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
    //Instance variables **********************************************

    /**
     * The interface type variable.  It allows the implementation of
     * the display method in the client.
     */
    ChatIF clientUI;


    //Constructors ****************************************************

    /**
     * Constructs an instance of the chat client.
     *
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */

    public ChatClient(String host, int port, ChatIF clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
    }


    //Instance methods ************************************************

    /**
     * This method handles all data that comes in from the server.
     *
     * @param msg The message from the server.
     */
    public void handleMessageFromServer(Object msg) {
        clientUI.display(msg.toString());


    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(String message) {
        try {
            if (message.startsWith("#")) {
                handleCommand(message);
            } else {
                sendToServer(message);
            }
        } catch (IOException e) {
            clientUI.display
                    ("Could not send message to server.  Terminating client.");
            quit();
        }
    }

    public void handleCommand(String message) {
        String[] args = message.split(" ");
        String command = args[0];
        switch (command) {
            case "#quit":
                clientUI.display("Terminating connection.");
                quit();
                break;

            case "#logoff":
                try {
                    clientUI.display("Logging off.");
                    closeConnection();
                } catch (IOException e) {
                    clientUI.display("ERROR - Could not close connection!");
                }
                break;

            case "#login":
                if (this.isConnected()) {
                    clientUI.display("Client is already connected.");
                } else {
                    try {
                        clientUI.display("Logging in.");
                        this.openConnection();
                        connectionOpen();
                    } catch (IOException e) {
                        clientUI.display("ERROR - Could not open connection!");
                    }
                }
                break;

            case "#sethost":
                if (this.isConnected()) {
                    clientUI.display("Cannot set host if client is still connected.");
                } else if (args.length > 1) {
                    super.setHost(args[1]);
                    System.out.println("Host set to " + getHost());
                } else {
                    clientUI.display("To sethost: #sethost <host>");
                }
                break;

            case "#setport":
                if (this.isConnected()) {
                    clientUI.display("Cannot set port if client is still connected.");
                } else if (args.length > 1) {
                    try {
                        super.setPort(Integer.parseInt(args[1]));
                        System.out.println("Port set to " + getPort());
                    } catch (NumberFormatException e) {
                        clientUI.display("Invalid port number. Please provide a valid integer.");
                    }
                } else {
                    clientUI.display("To setport: #setport <port>");
                }
                break;

            case "#gethost":
                clientUI.display("Current host is " + this.getHost());
                break;

            case "#getport":
                clientUI.display("Current port is " + this.getPort());
                break;

            default:
                clientUI.display("Invalid command: '" + command + "'");
                break;
        }
    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) {
            System.out.println("ERROR - Could not close connection!");
        }
        System.exit(0);
    }

    /**
     * Implements the hook method called each time an exception is thrown by the client's
     * thread that is waiting for messages from the server. The method may be
     * overridden by subclasses.
     *
     * @param exception the exception raised.
     */
    @Override
    protected void connectionException(Exception exception) {
        clientUI.display("The server has shut down");
        System.exit(0);
    }

    /**
     * Implemented the hook method called after the connection has been closed. The default
     * implementation does nothing. The method may be overriden by subclasses to
     * perform special processing such as cleaning up and terminating, or
     * attempting to reconnect.
     */
    @Override
    protected void connectionClosed() {
        clientUI.display("Connection closed");
    }

    protected void connectionOpen() {
        clientUI.display("Connection open");
    }
}
//End of ChatClient class
