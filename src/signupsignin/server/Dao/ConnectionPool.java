/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Imanol
 */
public class ConnectionPool {

    public ConnectionPool() {
    }

    private static BasicDataSource ds = null;

    public static DataSource getDataSource() {
        //Establecer valores
        if (ds == null) {
            ds = new BasicDataSource();
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUsername("root");
            ds.setPassword("root");
            ds.setUrl("jdbc:mysql://localhost/saludodb?autoReconnect=true&useSSL=false");
            ds.setMinIdle(1);
            ds.setMaxIdle(6);
            ds.setMaxTotal(10);
            ds.setMaxWaitMillis(4000);
        }
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
