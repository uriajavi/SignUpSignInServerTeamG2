/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Imanol
 */
public class ConnectionPool {

    private static BasicDataSource ds = null;

    public static DataSource getDataSource() {
        //FIXME: Valores establecidos a machete. Cambiar a archivo de propiedades.
        if (ds == null) {
            ds = new BasicDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUsername("root");
            ds.setPassword("");
            ds.setUrl("jdbc:mysql://localhost:3306/saludodb?autoReconnect=true&useSSL=false");
            //Establecer parametros adecuados
            ds.setMaxTotal(10);
            ds.setMaxWaitMillis(3000);
        }
        return ds;
    }

    public static synchronized Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
