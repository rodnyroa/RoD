package com.rodnyroa.tienda_online_nativo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		pd.setMessage("Loading..");

		URL_IMG = getResources().getString(R.urls.url_img);
		Log.d("", URL_IMG);

		// listaProductos = (ListView) findViewById(R.id.listadoProductos);
		listaProductos = (ListView) rootView
				.findViewById(R.id.listadoProductos);

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
}
