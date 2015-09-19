package com.ogc.model;

import java.util.Date;

public class QRSquareUser {
	
	
	private long id;
	private QRSquare square;
	private QRUser user;
	private RoleType role;
	private Date date;
	
	
	public QRSquareUser() {
	}
	public QRSquareUser(QRSquare square, QRUser user, RoleType role) {
		
		this.square = square;
		this.user = user;
		this.role = role;
		this.date = new Date();
	}
	public QRSquare getSquare() {
		return square;
	}
	public void setSquare(QRSquare square) {
		this.square = square;
	}
	public QRUser getUser() {
		return user;
	}
	public void setUser(QRUser user) {
		this.user = user;
	}
	public RoleType getRole() {
		return role;
	}
	public void setRole(RoleType role) {
		this.role = role;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QRSquareUser other = (QRSquareUser) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	
	
}
