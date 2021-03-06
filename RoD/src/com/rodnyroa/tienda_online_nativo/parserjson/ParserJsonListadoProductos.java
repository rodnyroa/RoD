package com.rodnyroa.tienda_online_nativo.parserjson;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rodnyroa.tienda_online_nativo.R.id;
import com.rodnyroa.tienda_online_nativo.model.Categories;
import com.rodnyroa.tienda_online_nativo.model.Currency;
import com.rodnyroa.tienda_online_nativo.model.Imagen;
import com.rodnyroa.tienda_online_nativo.model.Message;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.model.RowProducto;
import com.rodnyroa.tienda_online_nativo.model.User;

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

			try {
				jObjectProductos = jObject.getJSONArray("Products");
			} catch (Exception e) {
				//
			}

			ArrayList<RowProducto> products = null;

			if (jObjectProductos != null) {
				products = this.getListadoProductos(jObjectProductos);
			}

			if (products != null) {
				response.setProducts(products);
			}

			// Array de Messages
			JSONArray jObjectMessages = null;

			try {
				jObjectMessages = jObject.getJSONArray("Messages");
			} catch (Exception e) {
				//
			}

			ArrayList<Message> messages = null;

			if (jObjectMessages != null) {
				messages = this.getMessages(jObjectMessages);
			}

			if (messages != null) {
				response.setMessages(messages);
			}

			// Array de Currency
			JSONArray jObjectCurrency = null;

			try {
				jObjectCurrency = jObject.getJSONArray("Currency");
			} catch (Exception e) {
				//
			}

			ArrayList<Currency> listCurrency = null;
			if (jObjectCurrency != null) {
				listCurrency = this.getCurrency(jObjectCurrency);
			}

			if (listCurrency != null) {
				response.setCurrency(listCurrency);
			}

			// Array de Categories
			JSONArray jObjectCategories = null;

			try {
				jObjectCategories = jObject.getJSONArray("Categories");
			} catch (Exception e) {
				//
			}

			ArrayList<Categories> listCategories = null;
			if (jObjectCurrency != null) {
				listCategories = this.getCategories(jObjectCategories);
			}

			if (listCategories != null) {
				response.setCategories(listCategories);
			}
			
			String id = null;
			try{
				id=jObject.getString("Id");
			}catch(Exception e){
				//
			}
			
			if(id!=null){
				response.setId(id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private ArrayList<Message> getMessages(JSONArray jObjectMessages)
			throws Exception {
		ArrayList<Message> messages = null;
		for (int i = 0; i < jObjectMessages.length(); i++) {
			if (messages == null) {
				messages = new ArrayList<Message>();
			}
			// Objeto message
			JSONObject p = jObjectMessages.getJSONObject(i);

			Message m = new Message();

			try {
				m.setMessage(p.getString("Message"));
			} catch (Exception e) {
				//
			}

			try {
				m.setTs(p.getString("Ts"));
			} catch (Exception e) {
				//
			}

			try {
				m.setOwner(p.getBoolean("Owner"));
			} catch (Exception e) {
				//
			}

			User u = null;
			try {
				JSONObject uo = p.getJSONObject("User");
				u = new User();

				u.setName(uo.getString("Name"));

			} catch (Exception e) {
				//
			}

			if (u != null) {
				m.setUser(u);
			}

			messages.add(m);
		}

		return messages;
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
			JSONArray jObjectUsers = null;

			try {
				Id = p.getString("Id");
			} catch (Exception e) {
				Id = null;
			}

			try {
				MainText = p.getString("MainText");
			} catch (Exception e) {
				MainText = null;
			}

			try {
				Description = p.getString("Description");
			} catch (Exception e) {
				Description = null;
			}

			try {
				Amount = p.getString("Amount");
			} catch (Exception e) {
				Amount = null;
			}

			try {
				User = p.getString("User");
			} catch (Exception e) {
				User = null;
			}

			try {
				UserName = p.getString("UserName");
			} catch (Exception e) {
				UserName = null;
			}

			try {
				Url = p.getString("Url");
			} catch (Exception e) {
				Url = null;
			}

			try {
				jObjectUsers = p.getJSONArray("Users");
			} catch (Exception e) {
				//
			}

			if (Amount != null) {
				rp.setAmount(Amount);
			}

			if (Description != null) {
				rp.setDescription(Description);
			}

			if (Id != null) {
				rp.setId(Id);
			}

			if (MainText != null) {
				rp.setMainText(MainText);
			}

			if (User != null) {
				rp.setUser(User);
			}

			if (UserName != null) {
				rp.setUserName(UserName);
			}

			if (Url != null) {
				rp.setUrl(Url);
			}

			// Array de imagenes
			JSONArray jObjectImagenes = null;

			try {
				jObjectImagenes = p.getJSONArray("Img");
			} catch (Exception e) {
				//
			}

			ArrayList<Imagen> listImg = null;

			if (jObjectImagenes != null) {
				listImg = this.getListadoImg(jObjectImagenes);
			}
			if (listImg != null) {
				rp.setImg(listImg);
			}

			//
			ArrayList<User> listUsers = null;
			if (jObjectUsers != null) {
				listUsers = getUsers(jObjectUsers);
			}

			if (listUsers != null) {
				rp.setUsers(listUsers);
			}

			products.add(rp);

		}
		return products;
	}

	private ArrayList<Categories> getCategories(JSONArray jObjectCategories)
			throws JSONException {
		ArrayList<Categories> l = null;
		for (int i = 0; i < jObjectCategories.length(); i++) {
			if (l == null) {
				l = new ArrayList<Categories>();
			}
			Categories c = new Categories();

			JSONObject u = jObjectCategories.getJSONObject(i);

			String Id = null;
			String Name = null;

			try {
				Id = u.getString("Id");
			} catch (Exception e) {
				//
			}

			try {
				Name = u.getString("Name");
			} catch (Exception e) {
				//
			}

			if (Id != null) {
				c.setId(Id);
			}

			if (Name != null) {
				c.setName(Name);
			}

			l.add(c);
		}
		return l;
	}

	private ArrayList<Currency> getCurrency(JSONArray jObjectCurrency)
			throws JSONException {
		ArrayList<Currency> l = null;
		for (int i = 0; i < jObjectCurrency.length(); i++) {
			if (l == null) {
				l = new ArrayList<Currency>();
			}
			Currency c = new Currency();

			JSONObject u = jObjectCurrency.getJSONObject(i);

			String Id = null;
			String Name = null;

			try {
				Id = u.getString("Id");
			} catch (Exception e) {
				//
			}

			try {
				Name = u.getString("Name");
			} catch (Exception e) {
				//
			}

			if (Id != null) {
				c.setId(Id);
			}

			if (Name != null) {
				c.setName(Name);
			}

			l.add(c);
		}
		return l;
	}

	private ArrayList<User> getUsers(JSONArray jObjectUsers)
			throws JSONException {
		ArrayList<User> listUsers = null;
		for (int i = 0; i < jObjectUsers.length(); i++) {
			if (listUsers == null) {
				listUsers = new ArrayList<User>();
			}
			User user = new User();

			JSONObject u = jObjectUsers.getJSONObject(i);

			String Id = null;
			String CompleteName = null;
			String LastMessage = null;
			JSONArray jObjectMsg = null;
			ArrayList<Message> listMsg = null;

			try {
				Id = u.getString("Id");
			} catch (Exception e) {
				//
			}
			try {
				CompleteName = u.getString("CompleteName");
			} catch (Exception e) {
				//
			}
			try {
				LastMessage = u.getString("LastMessage");
			} catch (Exception e) {
				//
			}

			try {
				jObjectMsg = u.getJSONArray("Messages");
			} catch (Exception e) {
				//
			}

			if (Id != null) {
				user.setId(Id);
			}

			if (CompleteName != null) {
				user.setCompleteName(CompleteName);
			}

			if (LastMessage != null) {
				user.setLastMessage(LastMessage);
			}

			if (jObjectMsg != null) {
				try {
					listMsg = getMsg(jObjectMsg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (listMsg != null) {
				user.setMessages(listMsg);
			}

			listUsers.add(user);
		}
		return listUsers;
	}

	private ArrayList<Message> getMsg(JSONArray jObjectMsg) throws Exception {
		ArrayList<Message> listMsg = null;
		for (int i = 0; i < jObjectMsg.length(); i++) {
			if (listMsg == null) {
				listMsg = new ArrayList<Message>();
			}

			Message msg = new Message();

			JSONObject m = jObjectMsg.getJSONObject(i);

			String id = null;
			String message = null;
			String ts = null;
			boolean owner = false;

			try {
				id = m.getString("Id");
			} catch (Exception e) {
				//
			}

			try {
				message = m.getString("Message");
			} catch (Exception e) {
				//
			}

			try {
				ts = m.getString("Ts");
			} catch (Exception e) {
				//
			}

			try {
				owner = m.getBoolean("Owner");
			} catch (Exception e) {
				//
			}

			if (id != null) {
				msg.setId(id);
			}

			if (message != null) {
				msg.setMessage(message);
			}

			if (ts != null) {
				msg.setTs(ts);
			}

			msg.setOwner(owner);

			listMsg.add(msg);
		}
		return listMsg;
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
