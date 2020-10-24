/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server;

import signupsignin.server.Dao.MySQLDaoImplementation;

/**
 *
 * @author Mikel
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MySQLDaoImplementation prueba = new MySQLDaoImplementation();
        prueba.doQuery();
        
    }
    
}
