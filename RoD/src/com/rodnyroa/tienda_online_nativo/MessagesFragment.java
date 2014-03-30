package com.rodnyroa.tienda_online_nativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos;
import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.parserjson.ParserJsonListadoProductos;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		pd.setMessage("Loading..");

		 gridLayoutMsg=(GridView)
		 rootView.findViewById(R.id.GridLayoutMsg);
		
		
		
		// Calling async task to get json
		//String[] data = { this.getPreferencesByKey("token") };
		//new GetChat().execute(data);

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

			String urlChat = getResources().getString(R.urls.url_chat);

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
			Toast.makeText(context, "token obtenido:" + response.getToken(),
					Toast.LENGTH_SHORT).show();
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
}
