package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;

public class Currency implements Serializable {

	private String Id, Name;

	public Currency() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
