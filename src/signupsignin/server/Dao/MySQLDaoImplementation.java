/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Imanol
 */
public class MySQLDaoImplementation {

    protected ConnectionPool connectionPool = new ConnectionPool();
    protected Statement stmt;

    public MySQLDaoImplementation() {

    }

}
