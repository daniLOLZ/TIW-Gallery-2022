package it.polimi.tiw.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.dao.UserDAO;

//@WebServlet("/SignupCheck")
public class SignupCheck extends HttpServlet {


	private Connection connection;
	private static final long serialVersionUID = 1L;

	/**
	 * Opens the database connection for this servlet
	 */
	public void init() throws ServletException{
		
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
		
		// String s1 = getInitParameter("parameter exclusive to the servlet")
		// String s1 = getServletContext().getInitParameter("parameter of the servlet container")
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String repeatPassword = request.getParameter("repeatPassword");
		UserDAO userDAO = new UserDAO(connection);
		
		if(username == null || email == null || password == null || repeatPassword == null || 
		   username.isEmpty()|| email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || 
		   username.isBlank()|| email.isBlank() || password.isBlank() || repeatPassword.isBlank()){
			response.sendRedirect("/?errorId=1"); //Send with error status = 1 (null/invalid inputs (sign up))
			return;
		}
		
		//Check validity email-format
		// Simplified email Regex Format (excludes any special character
		//beside . (dot), doesn't cover all possible emails):
		// ([A-z]|[0-9])+(\.([A-z]|[0-9])+)*@([A-z]|[0-9])+\.([A-z]|[0-9])+
		
		Pattern emailPattern = Pattern.compile(
				"([A-z]|[0-9])+(\\.([A-z]|[0-9])+)*@([A-z]|[0-9])+\\.([A-z]|[0-9])+",
				Pattern.LITERAL);
		Matcher emailMatcher = emailPattern.matcher(email);
		
		if(!emailMatcher.find()) {
			//Match not found, returning error
			response.sendRedirect("/?errorId=2"); //Send with error status = 2 (bad email formatting)
			return;
		}
		
		//Check username not already in use
		try {
			if(userDAO.getUserFromUsername(username) != null) {
				// Username is already present
				response.sendRedirect("/?errorId=3"); //Send with error status = 3 (username already taken)
				return;
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
		}
		
		//Check matching passwords
		if(!password.equals(repeatPassword)) {
			response.sendRedirect("/?errorId=4"); //Send with error status = 4 (passwords not matching)
			return;
		}
		
		// Create start the user and add it to the database
		try {
			userDAO.createUser(username, email, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database user creation");
			
		}
		
		// Add session creation here
		HttpSession session = request.getSession(true);
		//It should always be new, since the session is just now starting after sign up
		if(session.isNew()){
			request.getSession().setAttribute("username", username);
		}
		
		
		// We don't want a refreshing issue, so we redirect
		
		response.sendRedirect("/GoToHomePage"); // add in session information?
		
	}
	
	public void destroy() {
		//Closes the database connection for this servlet
		try {
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			
		}
	}
}