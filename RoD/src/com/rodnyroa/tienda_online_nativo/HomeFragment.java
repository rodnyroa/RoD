package com.rodnyroa.tienda_online_nativo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;

import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos;
import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.parserjson.ParserJsonListadoProductos;

public class HomeFragment extends Fragment {

	private ListView listaProductos;
	Response response;
	public static String URL_IMG;
	ProgressDialog pd;
	View rootView;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		pd = new ProgressDialog(getActivity());
		pd.setTitle("Processing..");
		pd.setMessage("Please wait..");
		pd.setCancelable(false);
		pd.setIndeterminate(true);

		URL_IMG = getResources().getString(R.urls.url_base)+getResources().getString(R.urls.url_img);
		Log.d("", URL_IMG);

		// listaProductos = (ListView) findViewById(R.id.listadoProductos);
		listaProductos = (ListView) rootView
				.findViewById(R.id.listadoProductos);

		if(!this.haveInternet(getActivity().getApplicationContext())){
			showMessage("Por favor verifica tu conexión a internet.");
			return rootView;
		}
		// Calling async task to get json
		new GetListadoProductos().execute();

		/*
		 * A mano
		 */

		/*
		 * ArrayList<RowProducto> list = new ArrayList<RowProducto>();
		 * 
		 * for(int i=0; i<15;i++){ RowProducto p = new RowProducto();
		 * 
		 * p.setAmount("12"+i); p.setMainText("Texto principal["+i+"]");
		 * list.add(p); } CustomAdapterListadoProductos calp = new
		 * CustomAdapterListadoProductos( rootView.getContext(), list);
		 * 
		 * listaProductos.setAdapter(calp);
		 */
		
		listaProductos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				String mainText = response.getProducts().get(position).getMainText();
				
				Log.d("mainText: ", "> " + mainText);
				Intent i = new Intent(getActivity(), DetalleProductoActivity.class);
		        i.putExtra("Producto", response.getProducts().get(position));//response.getProducts().get(position).getUsers());
		        //i.putExtra("IdProducto", IdProducto);
		        startActivity(i);
			}
		});

		return rootView;
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetListadoProductos extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			HandlerRequestHttp sh = new HandlerRequestHttp();

			String urlListadoProductos = getResources().getString(
					R.urls.url_base)+getResources().getString(
							R.urls.listado_productos);

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlListadoProductos,
					HandlerRequestHttp.POST);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					ParserJsonListadoProductos parser = new ParserJsonListadoProductos(
							jsonStr);
					response = parser.getResponse();
					Log.d("Response: ", "> " + response.getResponse());
					Log.d("MainText: ", "> "
							+ response.getProducts().get(0).getMainText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("ParserJsonListadoProductos", "no se puede parsear");
					e.printStackTrace();
				}
			} else {
				Log.e("ParserJsonListadoProductos",
						"Couldn't get any data from the url");
				Toast.makeText(rootView.getContext(), "Sin datos",
						Toast.LENGTH_SHORT).show();
				getActivity().findViewById(R.id.frame_container);

				Fragment fragment = new SinDatosFragment();

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pd.dismiss();

			if (response.getResponse().equals("OK")) {
				Log.d("", "" + response.getProducts().size());
				CustomAdapterListadoProductos calp = new CustomAdapterListadoProductos(
						rootView.getContext(), response.getProducts());

				listaProductos.setAdapter(calp);
			}else{
				Toast.makeText(rootView.getContext(), "Sin datos", Toast.LENGTH_SHORT).show();
				getActivity().findViewById(R.id.frame_container);
				
				Fragment fragment = new SinDatosFragment();
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}

		}

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
