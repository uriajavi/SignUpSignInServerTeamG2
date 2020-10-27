/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.dao;

import exceptions.ErrorClosingDatabaseResources;
import exceptions.ErrorConnectingServerException;
import exceptions.PasswordMissmatchException;
import exceptions.UserNotFoundException;
import interfaces.Signable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import user.User;

/**
 *
 * @author Imanol
 */
public class MySQLDaoImplementation implements Signable {

    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;
    private final String checkUser = "SELECT * FROM USER WHERE LOGIN=?";
    private final String checkPassword = "SELECT * FROM USER WHERE LOGIN=? AND PASSWORD=?";
    private final String insertAccess = "UPDATE USER SET LASTACCESS =? WHERE LOGIN=?";

    @Override
    public User signIn(User user) throws ErrorConnectingServerException, UserNotFoundException, SQLException, PasswordMissmatchException, ErrorClosingDatabaseResources {
        try {
            // Obtengo una conexión desde el pool de conexiones.
            con = ConnectionPool.getConnection();

            //Hago comprobación de que exista el usuario.
            checkUser(user);

            //Establezco el preparedstatement y ejecuto la query. 
            ps = con.prepareStatement(checkPassword);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            
            //Controlo el error de la contraseña.
            if (!rs.next()) {
                throw new PasswordMissmatchException();
            }
            //Obtengo datos que voy a devolver al clente.
            user.setFullName(rs.getString("FULLNAME"));
            user.setLastAccess(rs.getDate("LASTACCESS"));
            
            insertAccesTime(user);
           
            //Control de error de conexión/query incorrecta.
        } catch (SQLException ex1) {
            throw new ErrorConnectingServerException();
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
                //
            } catch (SQLException ex2) {
                throw new ErrorClosingDatabaseResources();
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

    private void checkUser(User user) throws ErrorConnectingServerException, UserNotFoundException {

        try {
            PreparedStatement ps = con.prepareStatement(checkUser);
            ps.setString(1, user.getLogin());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new UserNotFoundException();
            }
            rs.close();
            ps.close();
        } catch (SQLException ex3) {
            throw new ErrorConnectingServerException();
        }
    }

    private void insertAccesTime(User user) throws ErrorConnectingServerException {
        //Falta conseguir introducir Date.
        try {
            java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
            PreparedStatement ps = con.prepareStatement(insertAccess);
            ps.setDate(1, now);
            ps.setString(2, user.getLogin());
            ResultSet rs = ps.executeQuery();
            rs.close();
            ps.close();
        } catch (SQLException ex3) {
            throw new ErrorConnectingServerException();
        }
    }

}



/*host=jdbc:mysql://localhost:3306/users?useSSL=false
user=root
password=
driver=com.mysql.cj.jdbc.Driver*/