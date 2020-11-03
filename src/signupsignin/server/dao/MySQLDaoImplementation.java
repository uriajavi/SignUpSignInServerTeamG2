/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.dao;

import exceptions.ErrorClosingDatabaseResources;
import exceptions.ErrorConnectingServerException;
import exceptions.ErrorConnectingDatabaseException;
import exceptions.PasswordMissmatchException;
import exceptions.QueryException;
import exceptions.UserAlreadyExistException;
import exceptions.UserNotFoundException;
import interfaces.Signable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final String searchUser = "SELECT * FROM USER WHERE LOGIN=? AND PASSWORD=?";
    private final String insertUser = "INSERT INTO user(login,email,fullname,password,status,privilege) VALUES(?,?,?,?,?,?)";
    private final String checkUser = "SELECT * FROM USER WHERE LOGIN=?";
    private final String checkPassword = "SELECT * FROM USER WHERE LOGIN=? AND PASSWORD=?";
    private final String insertAccess = "UPDATE USER SET LASTACCESS =? WHERE LOGIN=?";
    private final String checkIfUserExists = "SELECT * FROM USER WHERE LOGIN=? OR EMAIL=?";

    @Override
    public User signIn(User user) throws ErrorConnectingDatabaseException, UserNotFoundException, PasswordMissmatchException, ErrorClosingDatabaseResources, QueryException {
        try {
            // Obtengo una conexión desde el pool de conexiones.
            con = ConnectionPool.getConnection();

            //Hago comprobación de que exista el usuario.
            checkUser(user);

            //Establezco el preparedstatement y ejecuto la query. 
            ps = con.prepareStatement(checkPassword);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            rs = ps.executeQuery();

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
            throw new QueryException();
        } finally {
            try {
                closeConnection();
            } catch (SQLException ex) {
                throw new ErrorClosingDatabaseResources();
            }
        }

        //Devuelvo user
        return user;
    }

    @Override
    public User signUp(User user) throws UserAlreadyExistException, QueryException, ErrorConnectingDatabaseException {
        try {
            this.con = ConnectionPool.getConnection();
            this.checkifUserExists(user);

            this.ps = con.prepareStatement(this.insertUser);
            this.ps.setString(1, user.getLogin());
            this.ps.setString(2, user.getEmail());
            this.ps.setString(3, user.getFullName());
            this.ps.setString(4, user.getPassword());
            this.ps.setString(5, user.getStatus().toString());
            this.ps.setString(6, user.getPrivilege().toString());
            this.ps.execute();
            this.closeConnection();

        } catch (SQLException ex) {

            ex.printStackTrace();
            throw new QueryException();
        } finally {
            try {
                this.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    }

    private void checkifUserExists(User user) throws SQLException, UserAlreadyExistException {

        this.ps = con.prepareStatement(this.checkIfUserExists);
        this.ps.setString(1, user.getLogin());
        this.ps.setString(2, user.getEmail());
        this.rs = this.ps.executeQuery();

        while (rs.next()) {
            throw new UserAlreadyExistException(user);
        }

    }

    private void checkUser(User user) throws UserNotFoundException, SQLException {
        ps = con.prepareStatement(checkUser);
        ps.setString(1, user.getLogin());
        rs = ps.executeQuery();
        if (!rs.next()) {
            throw new UserNotFoundException();
        }
        rs.close();
        ps.close();

    }

    private void insertAccesTime(User user) throws SQLException {
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        PreparedStatement ps = con.prepareStatement(insertAccess);
        ps.setTimestamp(1, date);
        ps.setString(2, user.getLogin());
        ps.executeUpdate();
        ps.close();
    }

    private void closeConnection() throws SQLException {
        this.rs.close();
        this.ps.close();
        this.con.close();
    }
}
