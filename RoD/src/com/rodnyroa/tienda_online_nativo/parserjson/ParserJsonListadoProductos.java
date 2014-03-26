package com.rodnyroa.tienda_online_nativo.parserjson;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rodnyroa.tienda_online_nativo.model.Imagen;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.model.RowProducto;

import android.util.Log;

public class ParserJsonListadoProductos {

	private String data;

	public ParserJsonListadoProductos(String data) {
		this.data = data;
	}

	public Response getResponse() {
		Response response = null;

		if (data == null) {
			Log.d("Data: ", "> es null");
			return response;
		}

		try {
			JSONObject jObject = new JSONObject(data);
			response = new Response();

			String sResponse = jObject.getString("Response");

			response.setResponse(sResponse);

			try {
				String token = jObject.getString("Token");
				if (token != null) {
					response.setToken(token);
				}
			} catch (Exception ex) {
				//
			}
			// Array de productos
			JSONArray jObjectProductos = null;
			
			try{
				jObjectProductos = jObject.getJSONArray("Products");
			}catch(Exception e){
				//
			}

			ArrayList<RowProducto> products = null;

			if (jObjectProductos != null) {
				products = this.getListadoProductos(jObjectProductos);
			}

			if (products != null) {
				response.setProducts(products);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private ArrayList<RowProducto> getListadoProductos(
			JSONArray jObjectProductos) throws Exception {
		ArrayList<RowProducto> products = null;
		for (int i = 0; i < jObjectProductos.length(); i++) {

			if (products == null) {
				products = new ArrayList<RowProducto>();
			}

			RowProducto rp = new RowProducto();
			// Objeto producto
			JSONObject p = jObjectProductos.getJSONObject(i);

			String Id;
			String MainText;
			String Description;
			String Amount;
			String User;
			String UserName;
			String Url;

			try {
				Id = p.getString("Id");
			} catch (Exception e) {
				Id=null;
			}
			
			try {
				MainText = p.getString("MainText");
			} catch (Exception e) {
				MainText=null;
			}
			
			try {
				Description = p.getString("Description");
			} catch (Exception e) {
				Description=null;
			}
			
			try {
				Amount = p.getString("Amount");
			} catch (Exception e) {
				Amount=null;
			}
			
			try {
				User = p.getString("User");
			} catch (Exception e) {
				User=null;
			}
			
			try {
				UserName = p.getString("UserName");
			} catch (Exception e) {
				UserName=null;
			}
			
			try {
				Url = p.getString("Url");
			} catch (Exception e) {
				Url=null;
			}
			
			if(Amount!=null){
				rp.setAmount(Amount);
			}

			if(Description!=null){
				rp.setDescription(Description);
			}
			
			if(Id!=null){
				rp.setId(Id);
			}
			
			if(MainText!=null){
				rp.setMainText(MainText);
			}
			
			if(User!=null){
				rp.setUser(User);
			}
			
			if(UserName!=null){
				rp.setUserName(UserName);
			}

			if (Url != null) {
				rp.setUrl(Url);
			}

			// Array de imagenes
			JSONArray jObjectImagenes = p.getJSONArray("Img");

			ArrayList<Imagen> listImg = this.getListadoImg(jObjectImagenes);

			if (listImg != null) {
				rp.setImg(listImg);
			}

			products.add(rp);
		}
		return products;
	}

	private ArrayList<Imagen> getListadoImg(JSONArray jObjectImagenes)
			throws Exception {
		ArrayList<Imagen> listImg = null;
		for (int i = 0; i < jObjectImagenes.length(); i++) {

			if (listImg == null) {
				listImg = new ArrayList<Imagen>();
			}

			Imagen img = new Imagen();
			// Objeto producto
			JSONObject oI = jObjectImagenes.getJSONObject(i);

			String Id = oI.getString("Id");
			String Img = oI.getString("Img");

			img.setId(Id);
			img.setImg(Img);

			listImg.add(img);
		}
		return listImg;
	}
}
