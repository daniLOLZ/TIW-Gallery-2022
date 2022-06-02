package it.polimi.tiw.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.dao.ImageDAO;

//@WebServlet("/CreateEditAlbum")
public class CreateEditAlbum extends HttpServlet{
    
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        String readAlbumId = request.getParameter("albumId");
        String albumTitle = request.getParameter("albumTitle");

        if( readAlbumId == null || albumTitle == null ){
        	// todo go back to the previous screen? back to just the album?
        }

        try{
        	Integer albumId = Integer.parseInt(readAlbumId); //Hidden parameter in ALBUM_EDIT_PAGE that will be submitted on pressing the submit button
        } catch (NumberFormatException e) {
        	// todo go back to the previous screen? back to just the album?
        }
        
        ImageDAO imageDAO = new ImageDAO(connection);
        try {
            imageDAO.getImagesOfUser((String)request.getSession().getAttribute("username"));
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
        }

        //No clue how multiple checkboxes will be seen server-side

    }

    @Override
    public void destroy() {
        
    }
}
