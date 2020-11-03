/*	
 * To change this license header, choose License Headers in Project Properties.	
 * To change this template file, choose Tools | Templates	
 * and open the template in the editor.	
 */	
package signupsignin.server.dao;	

import exceptions.ErrorConnectingDatabaseException;	
import java.sql.Connection;	
import java.sql.SQLException;	
import java.util.ResourceBundle;	
import javax.sql.DataSource;	
import org.apache.commons.dbcp2.BasicDataSource;	

/**	
 *	
 * @author Imanol	
 */	
public class ConnectionPool {	

    private static BasicDataSource ds = null;	
    private static final ResourceBundle rb = ResourceBundle.getBundle("config.config");	

    public static DataSource getDataSource() {	
        //FIXME: Valores establecidos a machete. Cambiar a archivo de propiedades.	
        if (ds == null) {	
            ds = new BasicDataSource();	
            ds.setDriverClassName(rb.getString("driver"));	
            ds.setUsername(rb.getString("user"));	
            ds.setPassword(rb.getString("password"));	
            ds.setUrl(rb.getString("host"));	
            //Establecer parametros adecuados	
            ds.setMaxTotal(10);	
            ds.setMaxWaitMillis(3000);	
        }	
        return ds;	
    }	

    public static Connection getConnection() throws ErrorConnectingDatabaseException {	
        try {	
            return getDataSource().getConnection();	
        } catch (SQLException ex) {	
            throw new ErrorConnectingDatabaseException();	
        }	
    }	
}