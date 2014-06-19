package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;

public class Message implements Serializable {

	private String Id;
	private String Message;
	private String Ts;
	private boolean Owner;
	private User User;

	public Message() {
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return User;
	}

	public void setUser(User user) {
		User = user;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getTs() {
		return Ts;
	}

	public void setTs(String ts) {
		Ts = ts;
	}

	public boolean isOwner() {
		return Owner;
	}

	public void setOwner(boolean owner) {
		Owner = owner;
	}

}
