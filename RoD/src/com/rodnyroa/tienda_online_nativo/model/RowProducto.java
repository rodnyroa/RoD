package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RowProducto  implements Serializable{
	private String Id;
	private String MainText;
	private String Description;
	private String Amount;
	private String User;
	private String UserName;
	private ArrayList<Imagen> Img;
	private boolean Sold;
	private String Url;
	private ArrayList<User> Users;

	public RowProducto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<User> getUsers() {
		return Users;
	}

	public void setUsers(ArrayList<User> users) {
		Users = users;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public boolean isSold() {
		return Sold;
	}

	public void setSold(boolean sold) {
		Sold = sold;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getMainText() {
		return MainText;
	}

	public void setMainText(String mainText) {
		MainText = mainText;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public ArrayList<Imagen> getImg() {
		return Img;
	}

	public void setImg(ArrayList<Imagen> img) {
		Img = img;
	}

}
