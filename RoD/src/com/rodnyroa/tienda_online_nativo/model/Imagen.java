package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;

public class Imagen  implements Serializable{
	private String Id;
	private String Img;

	public Imagen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getImg() {
		return Img;
	}

	public void setImg(String img) {
		Img = img;
	}

}
