package fr.epita.epitrello.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBActivity 
{
	
	private static Connection getConnection() throws SQLException 
	{

		String url = "jdbc:h2:tcp://localhost/~/test";

		// the user :
		String user = "sa";

		// the password:
		String password = "";

		Connection connection = DriverManager.getConnection(url, user, password);
				
		return connection;
	}
	
	public int DBInsert(String UserName) 
	{

		PreparedStatement preparedStatement;
		try (Connection connection = getConnection()) 
		{
			preparedStatement = connection.prepareStatement("INSERT INTO USER (USER_NAME) VALUES (?)");
			preparedStatement.setString(1, UserName);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return 0;
		
	}

}
