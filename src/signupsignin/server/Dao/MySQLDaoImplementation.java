/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author Imanol
 */
public class MySQLDaoImplementation {

    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;
    
    public void doQuery() {

        try {   

            // Obtengo una conexión desde el pool de conexiones.
            con = ConnectionPool.getConnection();
 
            //Establezco el preparedstatement y ejecuto la query. Preguntar ExecuteUpdate.
            ps = con.prepareStatement("SELECT * FROM PAIS");
            rs = ps.executeQuery();
            
            //Compruebo que está todo correcto en la terminal, y que la query funciona.
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("IDIOMA"));
            }
            
            System.out.println("\n=====Releasing Connection Object To Pool=====\n");            
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                // Cerrar ResulSet
                if(rs != null) {
                    rs.close();
                }
                // Cerrar PreparedStatement
                if(ps != null) {
                    ps.close();
                }
                // Cerrar conexión recibida
                if(con != null) {
                    con.close();
                }
            } catch(SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

}
