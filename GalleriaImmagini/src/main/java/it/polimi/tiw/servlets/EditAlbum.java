package it.polimi.tiw.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.beans.Album;
import it.polimi.tiw.beans.Image;
import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.dao.ImageDAO;
import it.polimi.tiw.utility.CheckerUtility;
import it.polimi.tiw.utility.ConnectionUtility;

//@WebServlet("/EditAlbum")
public class EditAlbum extends HttpServlet {

	private Connection connection;
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
    	
		connection = ConnectionUtility.getConnection(getServletContext());
		
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String readAlbumId = request.getParameter("id");
		String albumTitle = request.getParameter("albumTitle");
		String[] readCheckboxes = request.getParameterValues("checkedImages");
		Integer albumId = -1;
		ImageDAO imageDAO = new ImageDAO(connection);
		AlbumDAO albumDAO = new AlbumDAO(connection);
		Album album = null;
		List<Image> userImages = null;
		Map<Integer, Boolean> selectedUserImages = null;
		List<String> listOfReadCheckboxes = null;
		String username = (String)request.getSession().getAttribute("username");
		
		if (!CheckerUtility.checkAvailability(readAlbumId) || !CheckerUtility.checkAvailability(albumTitle)) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}

		try {
			albumId = Integer.parseInt(readAlbumId); // Hidden parameter in ALBUM_EDIT_PAGE that will be submitted on
														// pressing the submit button
		} catch (NumberFormatException e) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}

		//Ensure this album belongs to the user making the request
		try {
			album = albumDAO.getAlbumFromId(albumId); 
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
			return;
		}
		
		if(album == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		
		if(!album.getCreator_username().equals(username)) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		
		
		try {
			userImages = imageDAO.getImagesOfUser(username);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
			return;
		}

		selectedUserImages = new LinkedHashMap<Integer, Boolean>();

		try {
			listOfReadCheckboxes = Arrays.asList(readCheckboxes);
		} catch (NullPointerException e) {
			// If no image was selected, create an empty list
			listOfReadCheckboxes = new ArrayList<String>();
		}
		
		for (Image image : userImages) {
			if (!listOfReadCheckboxes.contains(String.valueOf(image.getId()))) { // This image wasn't selected
				selectedUserImages.put(image.getId(), false);
			} else {
				selectedUserImages.put(image.getId(), true);
			}
		}

		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
			return;
		}

		try { 
			albumDAO.updateTitleOfAlbum(albumTitle, albumId);
			albumDAO.deleteAllImagesInAlbum(albumId);
			for (Integer imageId : selectedUserImages.keySet()) {
				if (selectedUserImages.get(imageId)) {
					albumDAO.addImageToAlbum(imageId, albumId);
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		
		// If everything went smoothly
		try {
			connection.commit();
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database connection");
			return;
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		response.sendRedirect(getServletContext().getContextPath() + "/Home");
	}

	@Override
	public void destroy() {
		try {
			ConnectionUtility.closeConnection(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
