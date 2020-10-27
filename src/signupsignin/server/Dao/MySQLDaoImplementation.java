/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.Dao;

import interfaces.Signable;
import user.User;

/**
 *
 * @author Mikel
 */
public class MySQLDaoImplementation implements Signable {

    @Override
    public User signIn(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User signUp(User user) {
        System.out.println("Usuario registrado");
        return null;
    }
    
}
