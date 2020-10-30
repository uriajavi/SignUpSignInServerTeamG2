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

/**
 *
 * @author Mikel
 */
public class Application {

    //static ServerSocket variable
    private static ServerSocket serverSocket;
    //socket server port on which it will listen
    private static final int port = 3333;

    public static void main(String args[]) throws IOException {
        try {
            //create the socket server object
            serverSocket = new ServerSocket(port);
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