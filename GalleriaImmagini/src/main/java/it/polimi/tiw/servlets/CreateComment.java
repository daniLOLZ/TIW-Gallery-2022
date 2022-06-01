package it.polimi.tiw.servlets;

import java.io.IOError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.dao.CommentDAO;

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
		String readAlbumId = request.getParameter("albumId");
		String readImageId = request.getParameter("imageId");
		String commentText = request.getParameter("commentText");

		if( readImageId == null || readImageId.isEmpty() ||
			commentText == null || commentText.isEmpty() 
			|| readAlbumId == null || readAlbumId.isEmpty() 
			){
			response.sendRedirect("/GoToHomePage");
		}

		Integer imageId = Integer.parseInt(readImageId);
		CommentDAO commentDAO = new CommentDAO(connection);

		try {
			commentDAO.createComment(imageId, username, commentText);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
		}

		response.sendRedirect("/GoToAlbumPage?albumId=" + readAlbumId + "&imageId=" + readImageId);
    	
    }

    @Override
    public void destroy() {
        
    }
}
