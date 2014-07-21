package com.rodnyroa.tienda_online_nativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.rodnyroa.tienda_online_nativo.adapter.GridLayoutPreviewImagesAdapter;
import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.model.RowProducto;
import com.rodnyroa.tienda_online_nativo.parserjson.ParserJsonListadoProductos;

public class DetalleProductoActivity extends Activity implements
		OnCreateContextMenuListener {
	// http://tech.leolink.net/2013/02/create-simple-infinite-carousel-in.html
	RowProducto producto;
	GridView gridLayoutMsg;
	TextView tvMainText, tvDescription, tvAmount;
	String idUser, token;
	Button btnNegociar;
	ProgressDialog pd;
	Response response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_producto);

		pd = new ProgressDialog(DetalleProductoActivity.this);
		pd.setTitle("Processing..");
		pd.setMessage("Please wait..");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		

		producto = (RowProducto) getIntent().getSerializableExtra("Producto");
		Log.i("MainText:", producto.getMainText() + "");
		Log.i("Cantidad Img:", producto.getImg().size() + "");
		idUser = this.getPreferencesByKey("idUser");

		gridLayoutMsg = (GridView) this.findViewById(R.id.gridView1);
		tvMainText = (TextView) this.findViewById(R.id.tvMainText);
		tvAmount = (TextView) this.findViewById(R.id.tvAmount);
		tvDescription = (TextView) this.findViewById(R.id.tvDescription);
		btnNegociar = (Button) this.findViewById(R.id.btnNegociar);

		token = this.getPreferencesByKey("token");

		tvMainText.setText(producto.getMainText());
		tvAmount.setText(producto.getAmount());
		tvDescription.setText(producto.getDescription());

		gridLayoutMsg.setAdapter(new GridLayoutPreviewImagesAdapter(
				getApplicationContext(), producto.getImg()));

		Log.i("idUser:", idUser + "");
		Log.i("idUser Producto:", producto.getUser() + "");
		
		if(idUser==null){
			idUser=producto.getUser();
		}

		if (idUser.equalsIgnoreCase(producto.getUser())) {
			btnNegociar.setVisibility(View.GONE);
		}

		btnNegociar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				 Intent i = new Intent(getApplicationContext(),
//				 NegociarActivity.class);
//				 i.putExtra("IdProducto", producto.getId());
//				 startActivity(i);
//				 finish();
				Log.i("token:", token + "");
				Log.i("idProducto:", producto.getId() + "");
				String[] data = { token, producto.getId() };
				
				if(!haveInternet(getApplicationContext())){
					showMessage("Por favor verifica tu conexión a internet.");
					return;
				}
				
				new GetChat().execute(data);

			}
		});

	}

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

			String urlChat = getResources().getString(R.urls.url_base)
					+ getResources().getString(R.urls.url_get_chat);

			// ANADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("token", arg0[0]));
			data.add(new BasicNameValuePair("producto", arg0[1]));

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
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Sin datos",
						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			if (response.getToken() != null) {
				savePreferences("token", response.getToken());
				
				 Intent i = new Intent(DetalleProductoActivity.this,
				 NegociarActivity.class);
				 i.putExtra("Messages", response.getMessages());
				 i.putExtra("IdProducto", producto.getId());
				 startActivity(i);
				 finish();
			}
			
			pd.dismiss();
		}

	}

	private String getPreferencesByKey(String key) {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"MyPreferences", Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, null);
	}
	
	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_producto, menu);
		return true;
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
		Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}
}
