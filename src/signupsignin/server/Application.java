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

/**
 * Application class.
 *
 * @author Mikel
 */
public class Application {

    //static ServerSocket variable
    private static ServerSocket serverSocket;
    private static final ResourceBundle rb = ResourceBundle.getBundle("config.config");
    //socket server port on which it will listen
    
    public static void main(String args[]) throws IOException {
        try {
            //create the socket server object
            serverSocket = new ServerSocket(Integer.parseInt(rb.getString("SERVER_SOCKET_PORT")));
            //keep listens indefinitely until receives 'exit' call or program terminates
            while (true) {
                //creating socket and waiting for client connection
                Socket socket = serverSocket.accept();
                new Worker(socket).start();
            }
        } catch (Exception e) {
            
        } finally {
            
        }
        
    }
}
