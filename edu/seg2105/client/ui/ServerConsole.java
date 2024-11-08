package edu.seg2105.client.ui;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

import java.io.IOException;
import java.util.Scanner;

public class ServerConsole implements ChatIF {
    // TODO change to 5555 after
    final public static int DEFAULT_PORT = 5556;
    EchoServer server;
    Scanner fromConsole;

    public ServerConsole(int port) {
        server = new EchoServer(port, this);

        try {
            server.listen(); // Start listening for client conncetions
        } catch (IOException e) {
            System.err.println("ERROR - Could not listen for clients!");
        }

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
        System.out.println(message);
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

        ServerConsole console = new ServerConsole(port);
        // console.accept();
        new Thread(console::accept).start();
    }
}
