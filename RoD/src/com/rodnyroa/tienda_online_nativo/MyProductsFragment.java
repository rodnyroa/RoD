package com.rodnyroa.tienda_online_nativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos;
import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.model.RowProducto;
import com.rodnyroa.tienda_online_nativo.parserjson.ParserJsonListadoProductos;

public class MyProductsFragment extends Fragment implements
		OnCreateContextMenuListener {
	ListView listViewMyProducts;
	Context context;
	Response response;
	public static String URL_IMG;
	ProgressDialog pd;
	View rootView;
	ArrayList<RowProducto> products;
	public static final int MENU_SOLD = 1;
	public static final int MENU_REMOVE = 2;

	public MyProductsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_my_products, container,
				false);
		context = rootView.getContext();
		listViewMyProducts = (ListView) rootView
				.findViewById(R.id.listViewMyProducts);

		pd = new ProgressDialog(getActivity());
		pd.setMessage("Loading..");

		// Calling async task to get json
		String[] data = { this.getPreferencesByKey("token") };
		new GetMyProducts().execute(data);

		/*
		 * A mano
		 */

		/*
		 * ArrayList<RowProducto> list = new ArrayList<RowProducto>();
		 * 
		 * for (int i = 0; i < 10; i++) { RowProducto p = new RowProducto();
		 * 
		 * p.setAmount("12" + i); p.setMainText("Texto principal[" + i + "]");
		 * list.add(p); } CustomAdapterListadoProductos calp = new
		 * CustomAdapterListadoProductos( rootView.getContext(), list);
		 * 
		 * listViewMyProducts.setAdapter(calp);
		 */

		return rootView;
	}

	private String getPreferencesByKey(String key) {
		SharedPreferences sharedPreferences = this.getActivity()
				.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, null);
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetMyProducts extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			// Creating service handler class instance
			HandlerRequestHttp sh = new HandlerRequestHttp();

			String urlListadoProductos = getResources().getString(
					R.urls.url_my_products);

			// AÑADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("token", arg0[0]));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlListadoProductos,
					HandlerRequestHttp.POST, data);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					ParserJsonListadoProductos parser = new ParserJsonListadoProductos(
							jsonStr);
					response = parser.getResponse();
					Log.d("Response: ", "> " + response.getResponse());
					Log.d("Response: ", "Token> " + response.getToken());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("ParserJsonListadoProductos", "no se puede parsear");
					e.printStackTrace();
				}
			} else {
				Log.e("ParserJsonListadoProductos",
						"Couldn't get any data from the url");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (response == null) {
				return;
			}
			Toast.makeText(context, "token obtenido:" + response.getToken(),
					Toast.LENGTH_SHORT).show();
			if (response.getToken() != null) {
				savePreferences("token", response.getToken());
				/*Toast.makeText(context,
						"Guardando token:" + response.getToken(),
						Toast.LENGTH_SHORT).show();*/
			}

			if (response.getResponse().equals("KO2")) {
				clearSharedPreferenes();
			}

			if (response.getResponse().equals("OK")) {
				products = response.getProducts();
				CustomAdapterListadoProductos calp = new CustomAdapterListadoProductos(
						rootView.getContext(), products);

				listViewMyProducts.setAdapter(calp);

				/*
				 * listViewMyProducts.setOnItemLongClickListener(new
				 * OnItemLongClickListener() {
				 * 
				 * @Override public boolean onItemLongClick(AdapterView<?> arg0,
				 * View arg1, int arg2, long arg3) { // TODO Auto-generated
				 * method stub return false; } });
				 */
				registerForContextMenu(listViewMyProducts);
			}else{
				Toast.makeText(rootView.getContext(), "Sin datos", Toast.LENGTH_SHORT).show();
				getActivity().findViewById(R.id.frame_container);
				
				Fragment fragment = new SinDatosFragment();
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}

			pd.dismiss();
		}

	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = this.getActivity()
				.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void clearSharedPreferenes() {
		SharedPreferences sharedPreferences = this.getActivity()
				.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
		this.getActivity().finish();
		startActivity(this.getActivity().getIntent());

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.context_menu_my_products);
		menu.add(Menu.NONE, MENU_SOLD, Menu.NONE, R.string.context_menu_sold);
		menu.add(Menu.NONE, MENU_REMOVE, Menu.NONE,
				R.string.context_menu_remove);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();

		// Toast.makeText(context, "position:"+menuInfo.position,
		// Toast.LENGTH_SHORT).show();

		switch (item.getItemId()) {
		case MENU_REMOVE:
			removeItem(menuInfo.position);
			return true;
		case MENU_SOLD:
			soldItem(menuInfo.position);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void removeItem(int id) {
		Toast.makeText(context,
				"function 1 called " + products.get(id).getId(),
				Toast.LENGTH_SHORT).show();
		// Calling async task to get json
				String[] data = { this.getPreferencesByKey("token"),products.get(id).getId(),R.urls.url_remove_product+"" };
				new updateStatusProduct().execute(data);
	}

	public void soldItem(int id) {
		Toast.makeText(context, "function 2 called", Toast.LENGTH_SHORT).show();
		String[] data = { this.getPreferencesByKey("token"),products.get(id).getId(),R.urls.url_sold_product+"" };
		new updateStatusProduct().execute(data);
	}

	private class updateStatusProduct extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {

			// Creating service handler class instance
			HandlerRequestHttp sh = new HandlerRequestHttp();

			String urlListadoProductos = getResources().getString(
					Integer.parseInt(arg0[2]));
			
			Log.d("", "URL:"+urlListadoProductos);

			// AÑADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("token", arg0[0]));
			data.add(new BasicNameValuePair("producto", arg0[1]));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlListadoProductos,
					HandlerRequestHttp.POST, data);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try{
					JSONObject jObject = new JSONObject(jsonStr);
					response = new Response();

					String sResponse = jObject.getString("Response");

					response.setResponse(sResponse);
					
					try{
						String token = jObject.getString("Token");
						if(token!=null){
							response.setToken(token);
						}
					}catch(Exception ex){
						//
					}

				}catch(Exception e){
					//
				}
			} else {
				Log.e("ParserJsonListadoProductos",
						"Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			if(response==null){
				return;
			}
			
			if(response.getResponse().equals("OK")){
				savePreferences("token", response.getToken());
				
				String[] data = { response.getToken() };
				new GetMyProducts().execute(data);
			}else{
				Toast.makeText(context, "Error vuelva a intentarlo de nuevo", Toast.LENGTH_SHORT).show();
			}

			
		}

	}
}
