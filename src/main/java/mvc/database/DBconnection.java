package mvc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
			
		Connection conn = null;
		
		String url = "jdbc:mariadb://localhost:3308/market";
		String user = "root";
		String password ="1475";
		
		Class.forName("org.mariadb.jdbc.Driver");
		
		conn = DriverManager.getConnection(url, user, password);
		
		return conn;
	}
}
