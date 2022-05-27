package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Comment;

public class CommentDAO {

	private Connection connection;
	
	public CommentDAO(Connection connection) {
		this.connection = connection;
	}

	public List<Comment> getAllCommentsForImage(String image_path) throws SQLException{
		
		List<Comment> commentsList = new ArrayList<Comment>();
		String query = "SELECT progressive, image_path, user, text FROM comment WHERE image_path = ?";
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, image_path);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Comment comment = new Comment();
				comment.setProgressive(resultSet.getInt("progressive"));
				comment.setImage_path(resultSet.getString("image_path"));
				comment.setUser(resultSet.getString("user"));
				comment.setText(resultSet.getString("text"));
				commentsList.add(comment);
			}
		}
		catch (Exception e) {
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
		return commentsList;
	}
	
	public Comment getCommentFromProgressiveImagePath(int progressive, String image_path) throws SQLException{
		String query = "SELECT progressive, image_path, user, text FROM comment WHERE progressive = ?, image_path = ?";
		ResultSet resultSet = null; 
		Comment resultComment = null;
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, progressive);
			preparedStatement.setString(2, image_path);
			resultSet = preparedStatement.executeQuery();
			//The result set should be at most 1 row
			if(!resultSet.next()) {
				// Comment not found
			}
			else {
				resultComment = new Comment();
				resultComment.setProgressive(resultSet.getInt("progressive"));
				resultComment.setImage_path(resultSet.getString("image_path"));
				resultComment.setUser(resultSet.getString("user"));
				resultComment.setText(resultSet.getString("text"));
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
		return resultComment;
	}
	
	public int createComment(int progressive, String image_path, String user, String text) throws SQLException{
		
		int code = 0;
		String query = "INSERT into comment (progressive, image_path, user, text) values (?, ?, ?, ?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, progressive);
			preparedStatement.setString(2, image_path);
			preparedStatement.setString(3, user);
			preparedStatement.setString(4, text);
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
