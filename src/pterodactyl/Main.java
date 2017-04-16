package pterodactyl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		new View();
	}
	
	
	
	/*//example code
	 * Connection conn;
	try {

		//This needs to be on the front of your location
		String url = "jdbc:h2:~/workspace\\PterodactylProject\\test";

		//This tells it to use the h2 driver
		Class.forName("org.h2.Driver");

		//creates the connection
		conn = DriverManager.getConnection(url,
				"sa",
				"");
		
		
		String query = "CREATE TABLE IF NOT EXISTS book("
			     + "ID INT PRIMARY KEY,"
			     + "FIRST_NAME VARCHAR(255),"
			     + "LAST_NAME VARCHAR(255),"
			     + "MI VARCHAR(1),"
			     + ");" ;
	
	Statement stmt = conn.createStatement();
	stmt.execute(query);
		
		
	} catch (SQLException | ClassNotFoundException e) {
		//You should handle this better
		e.printStackTrace();
	}*/

}
