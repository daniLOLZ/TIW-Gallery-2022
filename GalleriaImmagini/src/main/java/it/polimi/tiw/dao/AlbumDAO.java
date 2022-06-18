package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jni.Time;

import it.polimi.tiw.beans.Album;

public class AlbumDAO {

	private Connection connection;
	
	public AlbumDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Retrieves the album from the database
	 * @param albumId
	 * @return the requested album, or null if the album was not found
	 * @throws SQLException
	 */
	public Album getAlbumFromId(int albumId) throws SQLException {

		String query = "SELECT id, title, date, creator_username "
				+ 		"FROM album "
				+ 		"WHERE id = ?";
		
		ResultSet resultSet = null; 
		Album resultAlbum = null;
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, albumId);
			resultSet = preparedStatement.executeQuery();
			//The result set should be at most 1 row
			if(!resultSet.next()) {
				// Album not found
			}
			else {
				resultAlbum = new Album();
				resultAlbum.setId(resultSet.getInt("id"));
				resultAlbum.setTitle(resultSet.getString("title"));
				resultAlbum.setDate(resultSet.getDate("date"));
				resultAlbum.setCreator_username(resultSet.getString("creator_username"));
			}
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			// The order of closing is, for better safety, result -> statement -> connection
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
		return resultAlbum;
	}
	
	/**
	 * Gets them from newest to oldest
	 */
	public List<Album> getAlbumsOfUser(String username) throws SQLException{
		String query = "SELECT id, title, date, creator_username "
				+ 		"FROM album "
				+ 		"WHERE creator_username = ?"
				+ 		"ORDER BY date DESC";
		
		ResultSet resultSet = null; 
		List<Album> albumList = new ArrayList<Album>();
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Album album = new Album();
				album.setId(resultSet.getInt("id"));
				album.setTitle(resultSet.getString("title"));
				album.setDate(resultSet.getDate("date"));
				album.setCreator_username(resultSet.getString("creator_username"));
				albumList.add(album);
			}
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			// The order of closing is, for better safety, result -> statement -> connection
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
		return albumList;
	}

	/**
	 * Gets them from newest to oldest
	 */
	public List<Album> getAllAlbums() throws SQLException{
		List<Album> albumList = new ArrayList<Album>();
		String query = "SELECT id, title, date, creator_username "
				+ 		"FROM album A "
				+ 		"ORDER BY date DESC";
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Album album = new Album();
				album.setId(resultSet.getInt("id"));
				album.setTitle(resultSet.getString("title"));
				album.setDate(resultSet.getDate("date"));
				album.setCreator_username(resultSet.getString("creator_username"));
				albumList.add(album);
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
		return albumList;
	}
	
	/**
	 * Creates a new album with the given title and username
	 * The date of creation will be the moment of execution of this function
	 * @param title the title of the album to create
	 * @param creator_username the username of the creator
	 * @return A code signaling the result of the operation
	 * @throws SQLException
	 */
	public int createAlbum(String title, String creator_username) throws SQLException {
		String query = "INSERT into album (title, date, creator_username) values (?, ?, ?)";
		int code = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, title);
			Date currentDate = new Date(Time.now());
			preparedStatement.setDate(2, currentDate);
			preparedStatement.setString(3, creator_username);
			code = preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return code;
	}
	
	public int addImageToAlbum(int image_id, int album_id) throws SQLException {
		String query = "INSERT into containment (image_id, album_id) values (?, ?)";
		int code = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, image_id);
			preparedStatement.setInt(2, album_id);
			code = preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return code;
	}
}
