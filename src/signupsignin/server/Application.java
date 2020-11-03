/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import exceptions.UserAlreadyExistException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application class.
 *
 * @author Mikel
 */
public class Application {

    //static ServerSocket variable
    private static ServerSocket serverSocket;
    private static final ResourceBundle rb = ResourceBundle.getBundle("config.config");
    private static final Integer maxConnections = Integer.parseInt(ResourceBundle.getBundle("config.config").getString("MAX_CONNECTIONS"));
    private static Integer currentConnections = 0;

    //socket server port on which it will listen
    public static void main(String args[]) throws IOException {
        try {
            //create the socket server object
            serverSocket = new ServerSocket(Integer.parseInt(rb.getString("SERVER_SOCKET_PORT")));
            //keep listens indefinitely until receives 'exit' call or program terminates
            while (true) {
                //creating socket and waiting for client connection
                if (currentConnections < maxConnections) {
                    Socket socket = serverSocket.accept();
                    new Worker(socket).start();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public synchronized static void sumConnection() {
        currentConnections--;
    }

    public synchronized static void substractConnection() {
        currentConnections++;
    }
}
