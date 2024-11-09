package edu.seg2105.client.ui;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

import java.util.Scanner;

public class ServerConsole implements ChatIF {
    EchoServer server;
    final public static int DEFAULT_PORT = 5555;
    Scanner fromConsole;

    public ServerConsole(int port) {
        server = new EchoServer(port, this);

        // Create scanner object to read from console
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
        System.out.println("> " + message);
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

        ServerConsole sv = new ServerConsole(port);

        try {
            sv.server.listen(); //Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
        sv.accept();

    }
}
