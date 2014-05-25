package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;

public class Message  implements Serializable{

	private String Id;
	private String Message;
	private String Ts;
	private boolean Owner;

	public Message() {
		// TODO Auto-generated constructor stub
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
