/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server;

import exceptions.ErrorClosingDatabaseResources;
import exceptions.ErrorConnectingDatabaseException;
import exceptions.ErrorConnectingServerException;
import exceptions.PasswordMissmatchException;
import exceptions.QueryException;
import exceptions.UserAlreadyExistException;
import exceptions.UserNotFoundException;
import interfaces.Signable;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.TypeMessage;
import user.User;
import java.io.IOException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import message.Message;
import java.net.Socket;
import java.util.ResourceBundle;
import signupsignin.server.dao.DaoFactory;

/**
 *
 * @author Mikel, Imanol
 */
public class Worker extends Thread {

    private Socket socket;
    private Message message = null;
    private ObjectInputStream ois;
    private ResourceBundle rb = ResourceBundle.getBundle("config.config");

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
        Application.sumConnection();
        try {
            //read from socket to ObjectInputStream object
            ois = new ObjectInputStream(this.socket.getInputStream());
            //convert ObjectInputStream object to Message
            this.message = (Message) ois.readObject();
            Signable dao = DaoFactory.getSignable(rb.getString("DATABASE_TYPE"));
            switch (this.message.getType()) {
                case SIGN_UP: 
                    try {
                    User user = dao.signUp(this.message.getUser());
                    message = new Message(user, TypeMessage.REGISTER_OK);
                } catch (UserAlreadyExistException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    message = new Message(this.message.getUser(), TypeMessage.USER_EXISTS);
                } catch (ErrorConnectingDatabaseException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    message = new Message(this.message.getUser(), TypeMessage.DATABASE_ERROR);
                } catch (QueryException ex) {
                    message = new Message(this.message.getUser(), TypeMessage.QUERY_ERROR);
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ErrorConnectingServerException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                case SIGN_IN: 
                    try {
                    User user = dao.signIn(this.message.getUser());
                    message = new Message(user, TypeMessage.REGISTER_OK);
                } catch (ErrorConnectingDatabaseException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    message = new Message(this.message.getUser(), TypeMessage.DATABASE_ERROR);
                } catch (QueryException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    message = new Message(this.message.getUser(), TypeMessage.QUERY_ERROR);
                } catch (UserNotFoundException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    message = new Message(this.message.getUser(), TypeMessage.USER_DOES_NOT_EXIST);
                } catch (PasswordMissmatchException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    message = new Message(this.message.getUser(), TypeMessage.LOGIN_ERROR);
                } catch (ErrorClosingDatabaseResources ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    message = new Message(this.message.getUser(), TypeMessage.STOP_SERVER);
                } catch (ErrorConnectingServerException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(this.message);
                oos.close();
                ois.close();
                this.socket.close();
                Application.sumConnection();
            } catch (IOException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
