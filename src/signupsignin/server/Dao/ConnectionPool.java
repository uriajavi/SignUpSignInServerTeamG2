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

/**
 *
 * @author Imanol
 */
public class ConnectionPool {
    
    private List<Connection>availableConnections = new ArrayList<Connection>();
    private List<Connection>usedConnections = new ArrayList<Connection>();
    private final int MAX_CONNECTIONS = 5;

	private String URL;
	private String USERID;
	private String PASSWORD;


	/** Initialize all 5 Connections and put them in the Pool **/
	public ConnectionPool(String Url, String UserId, String password) throws SQLException {
		this.URL = Url;
		this.USERID = UserId;
		this.PASSWORD = password;

		for (int count = 0; count <MAX_CONNECTIONS; count++) {
			availableConnections.add(this.createConnection());
		}

	}






/** Private function, 
	used by the Pool to create new connection internally **/

	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(this.URL, this.USERID, this.PASSWORD);
	}




	/** Public function, used by us to get connection from Pool **/
	public Connection getConnection() {
		if (availableConnections.size() == 0) {
			System.out.println("All connections are Used !!");
			return null;
		} else {
			Connection con = 
			availableConnections.remove(
				availableConnections.size() - 1);
			usedConnections.add(con);
			return con;
		}
	}



	/** Public function, to return connection back to the Pool **/
	public boolean releaseConnection(Connection con) {
		if (null != con) {
			usedConnections.remove(con);
			availableConnections.add(con);
			return true;
		}
		return false;
	}





	/** Utility function to check the number of Available Connections **/
	public int getFreeConnectionCount() {
		return availableConnections.size();
	}

}
