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
    private static BasicDataSource ds = null;
    
    public static DataSource getDataSource(){
    
        //Establecer valores
    if(ds==null){
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("");
        ds.setUrl("jdbc:mysql://localhost:3306/test");
        ds.setInitialSize(50);
        ds.setMaxIdle(10);
        ds.setMaxTotal(20);
        ds.setMaxWaitMillis(5000);
    }
    return ds;
}
    
    public static Connection getPool() throws SQLException{
        return getDataSource().getConnection();
    }
}
