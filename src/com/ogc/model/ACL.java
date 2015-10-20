package com.ogc.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ACL {

	private Long id;
	private Boolean read;
	private Boolean write;

	public ACL() {

	}

	public ACL(Boolean read, Boolean write) {
		super();
		this.read = read;
		this.write = write;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Boolean isWrite() {
		return write;
	}

	public void setWrite(Boolean write) {
		this.write = write;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ACL other = (ACL) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public JSONObject toJSON() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("read", read);
		map.put("write", write);
		return new JSONObject(map);
	}

	public ACL(JSONObject obj) throws JSONException {

		read = obj.getBoolean("read");
		write = obj.getBoolean("write");

	}

	@Override
	public String toString() {
		return "ACL [read=" + read + ", write=" + write + "]";
	}

}
