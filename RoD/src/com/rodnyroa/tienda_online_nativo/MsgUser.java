package com.rodnyroa.tienda_online_nativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListMsgUser;
import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;
import com.rodnyroa.tienda_online_nativo.model.Message;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.parserjson.ParserJsonListadoProductos;

public class MsgUser extends Activity {

	ArrayList<Message> listMsg;
	ListView lvMsgChatUsr;
	Button btnSendMsg;
	EditText etSendMsg;
	String token, IdProducto;
	ProgressDialog pd;
	Response response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_user);

		lvMsgChatUsr = (ListView) findViewById(R.id.lvMsgUsr);
		btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
		etSendMsg = (EditText) findViewById(R.id.etSendMsg);
		token = this.getPreferencesByKey("token");

		pd = new ProgressDialog(this);
		pd.setTitle("Processing..");
		pd.setMessage("Please wait..");
		pd.setCancelable(false);
		pd.setIndeterminate(true);

		try {
			listMsg = (ArrayList<Message>) getIntent().getSerializableExtra(
					"Msg");
			IdProducto = (String) getIntent()
					.getSerializableExtra("IdProducto");
			Log.i("tamano", listMsg.size() + "");
			Log.i("New win3", listMsg.get(0).getMessage());

			lvMsgChatUsr
					.setAdapter(new CustomAdapterListMsgUser(this, listMsg));
		} catch (Exception e) {
			e.printStackTrace();
		}
		lvMsgChatUsr.setStackFromBottom(true);

		btnSendMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String msg = etSendMsg.getText().toString();
				String idMensaje = listMsg.get(listMsg.size() - 1).getId();

				Log.i("btnSendMsg", "token:" + token);
				Log.i("btnSendMsg", "producto:" + IdProducto);
				Log.i("btnSendMsg", "id_mensaje:" + idMensaje);
				Log.i("btnSendMsg", "mensaje:" + msg);
				
				if(!haveInternet(getApplicationContext())){
					showMessage("Por favor verifica tu conexión a internet.");
					return;
				}

				// Calling async task to get json
				String[] data = { token, IdProducto, idMensaje, msg };
				new sendMessage().execute(data);
			}
		});
	}

	private class sendMessage extends AsyncTask<String, Void, Void> {
		
		

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

			String urlRespMsg = getResources().getString(R.urls.url_base)
					+ getResources().getString(R.urls.url_resp_msg);

			// AÑADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("token", arg0[0]));
			data.add(new BasicNameValuePair("producto", arg0[1]));
			data.add(new BasicNameValuePair("id_mensaje", arg0[2]));
			data.add(new BasicNameValuePair("mensaje", arg0[3]));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlRespMsg,
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
//			Toast.makeText(getApplicationContext(), "token obtenido:" + response.getToken(),
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
				Toast.makeText(getApplicationContext(), "Registro OK",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Sin datos",
						Toast.LENGTH_SHORT).show();
				
			}

			pd.dismiss();
		}

	}

	private String getPreferencesByKey(String key) {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"MyPreferences", Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, null);
	}
	
	private void clearSharedPreferenes() {
		SharedPreferences sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
		this.finish();
		startActivity(this.getIntent());

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
		getMenuInflater().inflate(R.menu.msg_user, menu);
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
