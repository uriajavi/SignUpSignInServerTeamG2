/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server;

import interfaces.Signable;
import message.Message;
import message.TypeMessage;
import signupsignin.server.dao.DaoFactory;

/**
 *
 * @author Mikel
 */
public class Worker extends Thread {

    private Message message = null;

    public Worker() {
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void processMessage() {
      this.start();
    }

    @Override
    public void run() {
         Signable dao = DaoFactory.getSignable("mysql");
        switch (this.message.getType()) {
            case SIGN_UP:
                dao.signUp(this.message.getUser());
                break;
        } 
    }
}
