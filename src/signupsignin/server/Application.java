/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import message.Message;
import message.TypeMessage;

/**
 *
 * @author Mikel
 */
public class Application {

    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 3333;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while (true) {
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            new Worker(socket).start();
            socket.close();
        }

    }
}
