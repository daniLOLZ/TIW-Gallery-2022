package it.polimi.tiw.beans;

import java.sql.Date;

public class Album {

	private int id;
	private String title;
	private Date date;
	private String creator_username;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCreator_username() {
		return creator_username;
	}
	public void setCreator_username(String creator_username) {
		this.creator_username = creator_username;
	}
	
	
	
}
