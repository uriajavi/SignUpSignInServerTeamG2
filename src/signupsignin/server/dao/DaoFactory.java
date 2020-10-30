/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.dao;

import interfaces.Signable;

/**
 *
 * @author Mikel
 */
public class DaoFactory {

    private static final String MYSQL = "mysql";

    public static Signable getSignable(String type) {
        Signable signable = null;
        switch (type) {
            case MYSQL:
                signable = (Signable) new MySQLDaoImplementation();
                break;
        }
        return signable;
    }
}