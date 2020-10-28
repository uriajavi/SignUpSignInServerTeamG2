/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.server.dao;

import exceptions.ErrorConnectingServerException;
import exceptions.ErrorConnectingDatabaseException;
import exceptions.QueryException;
import exceptions.UserAlreadyExistException;
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

    @Override
    public User signIn(User user) {

        return user;
    }

    @Override
    public User signUp(User user) throws UserAlreadyExistException, QueryException,ErrorConnectingDatabaseException {
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
            throw new QueryException();
        }
        return user;
    }

    private void checkifUserExists(User user) throws SQLException, UserAlreadyExistException {

        this.ps = con.prepareStatement(this.insertUser);
        this.ps.setString(1, user.getLogin());
        this.ps.setString(2, user.getEmail());
        this.ps.setString(3, user.getFullName());
        this.ps.setString(4, user.getPassword());
        this.ps.setString(5, user.getStatus().toString());
        this.ps.setString(6, user.getPrivilege().toString());
        this.rs = this.ps.executeQuery();

        while (rs.next()) {
            throw new UserAlreadyExistException(user);
        }

    }

    private void closeConnection() throws SQLException {
        this.rs.close();
        this.ps.close();
        this.con.close();
    }
}
