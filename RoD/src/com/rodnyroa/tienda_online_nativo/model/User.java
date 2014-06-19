package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User  implements Serializable{

	private String Id;
	private String CompleteName;
	private String LastMessage;
	private String Name;
	private ArrayList<Message> Messages;

	public User() {
		// TODO Auto-generated constructor stub
	}
	

	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getCompleteName() {
		return CompleteName;
	}

	public void setCompleteName(String completeName) {
		CompleteName = completeName;
	}

	public String getLastMessage() {
		return LastMessage;
	}

	public void setLastMessage(String lastMessage) {
		LastMessage = lastMessage;
	}

	public ArrayList<Message> getMessages() {
		return Messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		Messages = messages;
	}

}
