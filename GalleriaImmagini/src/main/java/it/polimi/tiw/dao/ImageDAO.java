package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Image;

public class ImageDAO {

	private Connection connection;
	
	public ImageDAO(Connection connection) {
		this.connection = connection;
	}
	
	//Creating an image is not necessary for this project
	
	public Image getImageFromPath(String path) throws SQLException {
	
		String query = "SELECT path, title, date, description FROM image WHERE path = ?";
		ResultSet resultSet = null; 
		Image resultImage = null;
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, path);
			resultSet = preparedStatement.executeQuery();
			//The result set should be at most 1 row
			if(!resultSet.next()) {
				// Image not found
			}
			else {
				resultImage = new Image();
				resultImage.setPath(resultSet.getString("path"));
				resultImage.setTitle(resultSet.getString("title"));
				resultImage.setDate(resultSet.getDate("date"));
				resultImage.setDescription(resultSet.getString("description"));
			}
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (Exception e1) {
				throw new SQLException(e1);
			}
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (Exception e2) {
				throw new SQLException(e2);
			}
		}
		return resultImage;
		}
	public List<Image> getImagesInAlbum(int albumId) throws SQLException{
		List<Image> imageList = new ArrayList<Image>();
		String query = "SELECT path, title, date, description "
				+ 		"FROM image I, album A, containment C "
				+ 		"WHERE C.image_path = I.image_path"
				+ 		"AND C.album_id = A.?";
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, albumId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Image image = new Image();
				image.setPath(resultSet.getString("path"));
				image.setTitle(resultSet.getString("title"));
				image.setDate(resultSet.getDate("date"));
				image.setDescription(resultSet.getString("description"));
				imageList.add(image);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		 finally {
				try {
					if (resultSet != null) {
						resultSet.close();
					}
				} catch (Exception e1) {
					throw new SQLException(e1);
				}
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
					}
				} catch (Exception e2) {
					throw new SQLException(e2);
				}
			}
		return imageList;
	}
}
