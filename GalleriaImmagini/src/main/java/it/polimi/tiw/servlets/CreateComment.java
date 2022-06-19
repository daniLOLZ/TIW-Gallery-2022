package it.polimi.tiw.servlets;

import java.io.IOError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.dao.CommentDAO;
import it.polimi.tiw.utility.CheckerUtility;

//@WebServlet("/CreateComment")
public class CreateComment extends HttpServlet{
    
    private Connection connection;
	private static final long serialVersionUID = 1L;
    
    @Override
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
		String username = (String) request.getSession().getAttribute("username"); // Guaranteed to exist thanks to filters
		String readImageId = request.getParameter("imageId");
		String readAlbumId = request.getParameter("albumId");
		String commentText = request.getParameter("commentText");
		String path = getServletContext().getContextPath();
		CommentDAO commentDAO = new CommentDAO(connection);
		
		Integer imageId;
		Integer albumId;

		if( !CheckerUtility.checkAvailability(readImageId) ||
			!CheckerUtility.checkAvailability(readAlbumId) ||
			!CheckerUtility.checkAvailability(commentText) )
		{
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}

		try {
			imageId = Integer.parseInt(readImageId);
			albumId = Integer.parseInt(readAlbumId);
		} catch (NumberFormatException e) {
			//This might need to go somewhere else, but for now it's easiest to redirect to Home
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}

		try {
			commentDAO.createComment(imageId, username, commentText);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
		}
		
		response.sendRedirect( path + "/GoToAlbumPage?albumId=" + readAlbumId + "&imageId=" + readImageId);
    	
    }

    @Override
    public void destroy() {
        
    }
}
