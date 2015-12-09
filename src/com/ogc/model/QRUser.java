package com.ogc.model;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class QRUser {


	public QRUser() {
	}

	private long id;
	private String firstName;
	private String lastName;
	private Date registrationDate;
	boolean anonymous;

	public long getId() {
		return id;
	}

	public QRUser(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		registrationDate = new Date();
	}
	public QRUser(boolean anonymous) {
		this.anonymous = anonymous;
		registrationDate = new Date();
	}

	public QRUser(long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String param) {
		this.firstName = param;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String param) {
		this.lastName = param;
	}

	public void setRegistrationDate(Date date) {
		this.registrationDate = date;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	public JSONObject toJSON() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("firstName", firstName);
		jsonObject.put("lastName", lastName);
		jsonObject.put("registrationDate", registrationDate);
		jsonObject.put("anonymous", anonymous);
		return jsonObject;
	}

	@Override
	public String toString() {
		return "QRUser [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", registrationDate=" + registrationDate + ", anonymous=" + anonymous + "]";
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
		QRUser other = (QRUser) obj;
		if (id != other.id)
			return false;
		return true;
	}


}