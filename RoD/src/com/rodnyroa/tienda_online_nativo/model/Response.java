package com.rodnyroa.tienda_online_nativo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable{
	private String response;
	private ArrayList<RowProducto> products;
	private String token;

	public Response() {
		// TODO Auto-generated constructor stub
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
