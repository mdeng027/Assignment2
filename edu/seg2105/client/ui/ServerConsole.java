package edu.seg2105.client.ui;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

import java.util.Scanner;


public class ServerConsole implements ChatIF {
    final public static int DEFAULT_PORT = 5556;
    EchoServer server;
    Scanner fromConsole;

    public ServerConsole(int port) {
        try {
            server = new EchoServer(port);
        } catch (Exception exception) {
            System.out.println("ERROR - Failed to setup server!");
            System.exit(1);
        }
        fromConsole = new Scanner(System.in);
    }

    public void accept() {
        try {

            String message;

            while (true) {
                message = fromConsole.nextLine();
                server.handleMessageFromServerUI(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }

    }

    @Override
    public void display(String message) {
        System.out.println("SERVER MSG > " + message);
    }

    /**
     * This method is responsible for the creation of
     * the server instance (there is no UI in this phase).
     *
     * @param args\[0] The port number to listen on.  Defaults to 5555
     *                 if no argument is entered.
     */
    public static void main(String[] args) {
        int port = 0; //Port to listen on

        try {
            port = Integer.parseInt(args[0]); //Get port from command line
        } catch (Throwable t) {
            port = DEFAULT_PORT; //Set port to 5555
        }

        EchoServer sv = new EchoServer(port);

        try {
            sv.listen(); //Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

}
