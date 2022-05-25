package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

	private Connection connection;
	
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	
	public int createUser(String username, String email, String password) throws SQLException{
		// We need to create the database first, these names might change
		String query = "INSERT into User (username, email, password) values (?, ?, ?)";
		int code = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			code = preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException();
		}
	}
	

}
