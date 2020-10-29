/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server;

import signupsignin.server.dao.DaoFactory;
import exceptions.ErrorConnectingDatabaseException;
import exceptions.QueryException;
import exceptions.UserAlreadyExistException;
import interfaces.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.TypeMessage;
import user.User;

/**
 *
 * @author Mikel
 */
public class Worker extends Thread {

    private Socket socket;
    private Message message = null;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            //convert ObjectInputStream object to Message
            this.message = (Message) ois.readObject();
            Signable dao = DaoFactory.getSignable("mysql");
            switch (this.message.getType()) {
                case SIGN_UP: {
                    try {
                        User user = dao.signUp(this.message.getUser());
                        Message message = new Message(user, TypeMessage.REGISTER_OK);
                    } catch (UserAlreadyExistException ex) {
                        Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                        Message message = new Message(this.message.getUser(), TypeMessage.USER_EXISTS);
                    } catch (ErrorConnectingDatabaseException ex) {
                        Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                        Message message = new Message(this.message.getUser(), TypeMessage.DATABASE_ERROR);
                    } catch (QueryException ex) {
                        Message message = new Message(this.message.getUser(), TypeMessage.QUERY_ERROR);
                        Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            }
            ois.close();
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

