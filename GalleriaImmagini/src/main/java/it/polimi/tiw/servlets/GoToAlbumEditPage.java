package it.polimi.tiw.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/AlbumEdit")
public class GoToAlbumEditPage extends HttpServlet {
	
	private Connection connection;
	private static final long serialVersionUID = 1L;
	
	
    public void init() throws ServletException {
		final String DB_URL = getServletContext().getInitParameter("dbUrl");
		final String USER = getServletContext().getInitParameter("dbUser");
//		final String PASS = getServletContext().getInitParameter("dbPasswordGreg");
		final String PASS = getServletContext().getInitParameter("dbPasswordDani");
		final String DRIVER_STRING = getServletContext().getInitParameter("dbDriver");
		
	
		try {
			Class.forName(DRIVER_STRING);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
		}
		catch (ClassNotFoundException e){
			throw new UnavailableException("Can't load db driver");
		}
		catch (SQLException e) {
			throw new UnavailableException("Can't connect to database");
		}
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void destroy() {
		
	}
}
