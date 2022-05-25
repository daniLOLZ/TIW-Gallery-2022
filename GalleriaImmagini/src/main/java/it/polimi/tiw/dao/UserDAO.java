package it.polimi.tiw.dao;

import java.sql.Connection;

public class UserDAO {

	private Connection connection;
	
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	
	

}
