/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.dao;

import interfaces.Signable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import user.User;

/**
 *
 * @author Imanol
 */
public class MySQLDaoImplementation implements Signable {

    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;
    private final String searchUser = "SELECT * FROM USER WHERE LOGIN=? AND PASSWORD=?";

    @Override
    public User signIn(User user) {

        /*Metodos a implementar:
        - Usuario no existe (select no lanzará incorrecta)
        - Password incorrecta (select sera incongruente)
        - Error de conexión a BDA (probar a acceder sin conexión, a ver que excepcion salta)
        - Hay que insertar la fecha actual en el lastacces, despues de obtener el último access. Un update 
          y controlar la excepcion de inserción.
        - 
        */
        try {
            // Obtengo una conexión desde el pool de conexiones.
            con = ConnectionPool.getConnection();

            
            //Establezco el preparedstatement y ejecuto la query.
            ps = con.prepareStatement(searchUser);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();

            //Guardo en el objeto User el nombre y el ultimo acceso para mostrar en dashboard
            while (rs.next()) {
                user.setFullName(rs.getString("FULLNAME"));
                user.setLastAccess(rs.getDate("LASTACCESS"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                // Cerrar ResulSet
                if (rs != null) {
                    rs.close();
                }
                // Cerrar PreparedStatement
                if (ps != null) {
                    ps.close();
                }
                // Cerrar conexión recibida
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        //Devuelvo user
        return user;
    }

    @Override
    public User signUp(User user) {
        System.out.println("Usuario registrado");
        return null;
    }
}
