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
import signupsignin.server.Dao.MySQLDaoImplementation;

/**
 *
 * @author Mikel
 */
public class Application {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    private static ServerSocket ss = null;

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        ss = new ServerSocket(6666);
        Socket socket = ss.accept();
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        Message m = (Message) is.readObject();
        System.out.println(m.getUser().getLogin());
        System.out.println(m.getUser().getPassword());
        socket.close();
    }
}
