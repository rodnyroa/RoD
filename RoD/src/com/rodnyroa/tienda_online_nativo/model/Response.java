package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
	private String response;
	private ArrayList<RowProducto> products;
	private ArrayList<Message> Messages;
	private String token;
	private ArrayList<Currency> Currency;
	private ArrayList<Categories> Categories;
	private String Id;

	public Response() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public ArrayList<Currency> getCurrency() {
		return Currency;
	}

	public void setCurrency(ArrayList<Currency> currency) {
		Currency = currency;
	}

	public ArrayList<Categories> getCategories() {
		return Categories;
	}

	public void setCategories(ArrayList<Categories> categories) {
		Categories = categories;
	}

	public ArrayList<Message> getMessages() {
		return Messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		Messages = messages;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public ArrayList<RowProducto> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<RowProducto> products) {
		this.products = products;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
