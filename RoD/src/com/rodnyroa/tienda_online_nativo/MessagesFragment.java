package com.rodnyroa.tienda_online_nativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.adapter.GridLayoutMessagesAdapter;
import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.model.RowProducto;
import com.rodnyroa.tienda_online_nativo.parserjson.ParserJsonListadoProductos;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MessagesFragment extends Fragment {

	View rootView;
	GridView gridLayoutMsg;
	Context context;
	Response response;
	ProgressDialog pd;

	public MessagesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_messages, container,
				false);
		context = rootView.getContext();

		pd = new ProgressDialog(getActivity());
		pd.setTitle("Processing..");
		pd.setMessage("Please wait..");
		pd.setCancelable(false);
		pd.setIndeterminate(true);

		gridLayoutMsg = (GridView) rootView.findViewById(R.id.gridView1);

		response = new Response();

		ArrayList<RowProducto> listado = new ArrayList<RowProducto>();

		for (int i = 1; i <= 5; i++) {
			RowProducto p = new RowProducto();

			p.setId(i + "");
			p.setUrl("13841178924344770.jpeg");

			listado.add(p);
			p = null;
		}
		
		if(!haveInternet(getActivity().getApplicationContext())){
			showMessage("Por favor verifica tu conexión a internet.");
			return rootView;
		}

		// Log.d("listado: ", "> " + listado.size());
		// gridLayoutMsg.setAdapter(new GridLayoutMessagesAdapter(context,
		// listado));
		// http://www.rogcg.com/blog/2013/11/01/gridview-with-auto-resized-images-on-android
		// Calling async task to get json
		String[] data = { this.getPreferencesByKey("token") };
		new GetChat().execute(data);
		 
		 gridLayoutMsg.setOnItemClickListener(new OnItemClickListener() 
		 {
			    
				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					
					Log.i("", "hola"+position);
					if(response==null){
						return;
					}
					Log.i("", "HOLA"+response.getProducts().get(position).getUsers().get(0).getCompleteName());
					Log.i("", "TMO"+response.getProducts().get(position).getUsers().get(0).getMessages().size());
					
					// this 'mActivity' parameter is Activity object, you can send the current activity.
			        Intent i = new Intent(context, MsgChatUsersActivity.class);
			        i.putExtra("Users", response.getProducts().get(position).getUsers());
			        i.putExtra("IdProducto", response.getProducts().get(position).getId());
			        startActivity(i);
					//Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
				}
			});

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
	private class GetChat extends AsyncTask<String, Void, Void> {
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

			String urlChat = getResources().getString(R.urls.url_base)+getResources().getString(R.urls.url_chat);

			// ANADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("token", arg0[0]));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlChat,
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
			Log.d("onPostExecute", "" + response.getResponse());
			Log.d("onPostExecute", "" + response.getToken());
//			Toast.makeText(context, "token obtenido:" + response.getToken(),
//					Toast.LENGTH_SHORT).show();
			if (response.getToken() != null) {
				savePreferences("token", response.getToken());
				/*
				 * Toast.makeText(context, "Guardando token:" +
				 * response.getToken(), Toast.LENGTH_SHORT).show();
				 */
			}

			if (response.getResponse().equals("KO2")) {
				clearSharedPreferenes();
			}

			if (response.getResponse().equals("OK")) {
				gridLayoutMsg.setAdapter(new GridLayoutMessagesAdapter(context,
						 response.getProducts()));
			} else {
				Toast.makeText(rootView.getContext(), "Sin datos",
						Toast.LENGTH_SHORT).show();
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
	
	public static boolean haveInternet(Context ctx) {

	    NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
	            .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

	    if (info == null || !info.isConnected()) {
	        return false;
	    }
	    if (info.isRoaming()) {
	        // here is the roaming option you can change it if you want to
	        // disable internet while roaming, just return false
	        return false;
	    }
	    return true;
	}
	
	private void showMessage(String message) {
		Toast.makeText(getActivity().getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}
}
