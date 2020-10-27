/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server;

import exceptions.ErrorClosingDatabaseResources;
import exceptions.ErrorConnectingServerException;
import exceptions.PasswordMissmatchException;
import exceptions.UserNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.Message;
import message.TypeMessage;
import signupsignin.server.dao.MySQLDaoImplementation;
import user.User;

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
        /*User user = new User();
        user.setLogin("DEMO");
        user.setPassword("demo");
        MySQLDaoImplementation dao = new MySQLDaoImplementation();
        try {
            user=dao.signIn(user);
            System.out.println(user.getFullName());
        } catch (ErrorConnectingServerException ex) {
            System.out.println("Hijo puta");
        } catch (UserNotFoundException ex) {
            System.out.println("No está");
        } catch (SQLException ex) {
            System.out.println("NO CONECTA!");
        } catch (PasswordMissmatchException ex) {
            System.out.println("Error contraseña.");
        } catch (ErrorClosingDatabaseResources ex) {
            System.out.println("Fatal error.");
        }
        
    }*/
        
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while (true) {
            

            //creating socket and waiting for client connection
            Socket socket = server.accept();

            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //convert ObjectInputStream object to Message
            Message message = (Message) ois.readObject();
            System.out.println("Message Received: " + "Nuevo objeto");

            Worker worker = new Worker();
            worker.setMessage(message);
            worker.processMessage();

            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
            oos.writeObject("Hi Client ");
            //close resources
            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client sends exit request
            if (message.getType().equals(TypeMessage.STOP_SERVER)) {
                break;
            }
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    }
}
