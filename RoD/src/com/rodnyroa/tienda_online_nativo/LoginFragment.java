package com.rodnyroa.tienda_online_nativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;

public class LoginFragment extends Fragment {

	EditText txtEmail, txtPwd, txtNombre, txtApellido, txtEmailNewUser,
			txtPwdNewUser, txtRePwd;
	Button btnIngrear, btnNewUser;
	Context context;

	ProgressDialog pd;

	ListView mDrawerList;

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("", "oncreateView");
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		context = rootView.getContext();

		Resources res = getResources();

		TabHost tabs = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec spec = tabs.newTabSpec("miTab1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Ingresar",
				res.getDrawable(android.R.drawable.ic_lock_lock));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("miTab2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Nuevo Usuario",
				res.getDrawable(android.R.drawable.ic_input_add));
		tabs.addTab(spec);

		tabs.setCurrentTab(0);

		/*
		 * tabs.setOnTabChangedListener(new OnTabChangeListener() {
		 * 
		 * @Override public void onTabChanged(String tabId) {
		 * 
		 * Toast.makeText(getApplicationContext(),
		 * "El id del TAb seleccionado es: " + tabId ,
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * } });
		 */

		//
		this.btnIngrear = (Button) rootView.findViewById(R.id.btnIngrear);
		this.btnNewUser = (Button) rootView.findViewById(R.id.btnNewUser);

		this.txtEmail = (EditText) rootView.findViewById(R.id.txtEmail);
		this.txtPwd = (EditText) rootView.findViewById(R.id.txtPwd);
		this.txtNombre = (EditText) rootView.findViewById(R.id.txtNombre);
		this.txtApellido = (EditText) rootView.findViewById(R.id.txtApellido);
		this.txtPwdNewUser = (EditText) rootView
				.findViewById(R.id.txtPwdNewUser);
		this.txtEmailNewUser = (EditText) rootView
				.findViewById(R.id.txtEmailNewUser);
		this.txtRePwd = (EditText) rootView.findViewById(R.id.txtRePwd);

		this.btnIngrear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnIngresarOnClick(v);
			}
		});

		pd = new ProgressDialog(getActivity());
		pd.setTitle("Processing..");
		pd.setMessage("Please wait..");
		pd.setCancelable(false);
		pd.setIndeterminate(true);

		// ListView mDrawerList =(ListView)
		// rootView.findViewById(R.id.list_slidermenu);

		this.btnNewUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnNewUser(v);
			}
		});

		return rootView;
	}

	private void showMessage(String message) {
		Toast.makeText(getActivity().getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}

	private void setFocus(EditText e) {
		if (e.requestFocus()) {
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	public void btnNewUser(View v) {

		if (this.txtNombre.getText().toString().length() == 0) {
			showMessage("Ingresa el nombre");
			setFocus(this.txtNombre);
			return;
		}

		if (this.txtApellido.getText().toString().length() == 0) {
			showMessage("Ingresa el apellido");
			setFocus(this.txtApellido);
			return;
		}

		if (this.txtEmailNewUser.getText().toString().length() == 0) {
			showMessage("Ingresa el email");
			setFocus(this.txtEmailNewUser);
			return;
		}

		if (this.txtPwdNewUser.getText().toString().length() == 0) {
			showMessage("Ingresa tu password");
			setFocus(this.txtPwdNewUser);
			return;
		}

		if (this.txtRePwd.getText().toString().length() == 0) {
			showMessage("Confirma tu password");
			setFocus(this.txtRePwd);
			return;
		}

		if (!this.txtPwdNewUser.getText().toString()
				.equals(this.txtRePwd.getText().toString())) {
			showMessage("Password no coincide");
			setFocus(this.txtPwdNewUser);
			this.txtPwdNewUser.setText("");
			this.txtRePwd.setText("");
			return;
		}
		String[] data = { this.txtNombre.getText().toString(),
				this.txtApellido.getText().toString(),
				this.txtEmailNewUser.getText().toString(),
				this.txtPwdNewUser.getText().toString(),
				this.txtRePwd.getText().toString() };
		
		if(!haveInternet(getActivity().getApplicationContext())){
			showMessage("Por favor verifica tu conexión a internet.");
			return;
		}

		new NewUser().execute(data);
	}

	public void btnIngresarOnClick(View view) {
		// Log.d("", "ingresar!!");
		// Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show();
		if (this.txtEmail.getText().toString().length() == 0) {
			Toast.makeText(context, getResources().getString(R.msgForm.email),
					Toast.LENGTH_SHORT).show();
			if (txtEmail.requestFocus()) {
				getActivity()
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
			return;
		}

		if (this.txtPwd.getText().toString().length() == 0) {
			Toast.makeText(context, getResources().getString(R.msgForm.email),
					Toast.LENGTH_SHORT).show();
			if (txtPwd.requestFocus()) {
				getActivity()
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
			return;
		}
		
		if(!haveInternet(getActivity().getApplicationContext())){
			showMessage("Por favor verifica tu conexión a internet.");
			return;
		}
		String[] data = { this.txtEmail.getText().toString(),
				this.txtPwd.getText().toString() };
		// Calling async task to get login
		new LoginService().execute(data);
	}

	/*
	 * Ingrear
	 */
	private class LoginService extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			HandlerRequestHttp sh = new HandlerRequestHttp();

			String urlLogin = getResources().getString(R.urls.url_base)
					+ getResources().getString(R.urls.url_login);
			Log.d("", "url_login:" + urlLogin);
			String email = params[0];
			String password = params[1];

			Log.d("", "email:" + email);
			Log.d("", "pwd:" + password);

			// AÑADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("email", email));
			data.add(new BasicNameValuePair("password", password));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlLogin,
					HandlerRequestHttp.POST, data);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				// {"Response":"OK","Token":"874581b27702798734ca3b89f0b331c2","Name":"PEPE PEREZ","IdUser":"15"}
				try {
					JSONObject jObject = new JSONObject(jsonStr);
					String response = jObject.getString("Response");
					if (response.equalsIgnoreCase("OK")) {
						String token = jObject.getString("Token");
						String name = jObject.getString("Name");
						String idUser = jObject.getString("IdUser");
						savePreferences("token", token);
						savePreferences("name", name);
						savePreferences("idUser", idUser);
						// Log.i("idUser:", idUser + "");

						/*
						 * Fragment fragment = new HomeFragment();
						 * 
						 * FragmentManager fragmentManager =
						 * getFragmentManager();
						 * fragmentManager.beginTransaction()
						 * .replace(R.id.frame_container, fragment).commit();
						 */
						// Intent i = new Intent(context, MainActivity.class);
						// startActivity(i);

						getActivity().finish();
						startActivity(getActivity().getIntent());

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pd.dismiss();
		}

	}

	private class NewUser extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			HandlerRequestHttp sh = new HandlerRequestHttp();

			String urlLogin = getResources().getString(R.urls.url_base)
					+ getResources().getString(R.urls.url_nuevo_usuario);
			Log.d("", "url_nuevo_usuario:" + urlLogin);
			String txtNombre = params[0];
			String txtApellido = params[1];
			String txtEmailNewUser = params[2];
			String txtPwdNewUser = params[3];
			String txtRePwd = params[4];

			// AÑADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("nombre", txtNombre));
			data.add(new BasicNameValuePair("apellido", txtApellido));
			data.add(new BasicNameValuePair("email", txtEmailNewUser));
			data.add(new BasicNameValuePair("password", txtPwdNewUser));
			data.add(new BasicNameValuePair("re_password", txtRePwd));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlLogin,
					HandlerRequestHttp.POST, data);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				// {"Response":"OK","Token":"874581b27702798734ca3b89f0b331c2","Name":"PEPE PEREZ","IdUser":"15"}
				try {
					JSONObject jObject = new JSONObject(jsonStr);
					String response = jObject.getString("Response");
					if (response.equalsIgnoreCase("OK")) {
						String[] data2 = { txtEmailNewUser, txtPwdNewUser };
						// Calling async task to get login
						new LoginService().execute(data2);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

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
	
}
