package com.rodnyroa.tienda_online_nativo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.rodnyroa.tienda_online_nativo.handlerrequest.HandlerRequestHttp;
import com.rodnyroa.tienda_online_nativo.model.Categories;
import com.rodnyroa.tienda_online_nativo.model.Currency;
import com.rodnyroa.tienda_online_nativo.model.Response;
import com.rodnyroa.tienda_online_nativo.parserjson.ParserJsonListadoProductos;
import com.loopj.android.http.*;

public class PublishFragment extends Fragment {

	static final int REQUEST_IMAGE_CAPTURE = 1;

	Button button1;
	ImageView[] imageView1, imageView2;
	LinearLayout linearLayoutGroupImages;
	int index = 0;
	// ImageView imageView2;
	ProgressDialog pd;
	Response response;

	EditText editTextQueVendes, editTextDescripcion, editTextPrecio;

	Spinner spTipoMoneda, spCategoria;

	ArrayList<String> valorMoneda = new ArrayList<String>();
	ArrayList<String> idMoneda = new ArrayList<String>();
	ArrayList<String> valorCategoria = new ArrayList<String>();
	ArrayList<String> idCategoria = new ArrayList<String>();

	String token;

	String idProducto;

	public PublishFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_publish, container,
				false);

		pd = new ProgressDialog(getActivity());
		pd.setTitle("Processing..");
		pd.setMessage("Please wait..");
		pd.setCancelable(false);
		pd.setIndeterminate(true);

		ArrayList<String> cadena = new ArrayList<String>();
		cadena.add(new String("1"));
		cadena.add(new String("2"));
		cadena.add(new String("3"));

		editTextQueVendes = (EditText) rootView
				.findViewById(R.id.editTextQueVendes);
		editTextDescripcion = (EditText) rootView
				.findViewById(R.id.editTextDescripcion);
		editTextPrecio = (EditText) rootView.findViewById(R.id.editTextPrecio);

		spTipoMoneda = (Spinner) rootView.findViewById(R.id.spinnerTipoMoneda);
		spCategoria = (Spinner) rootView.findViewById(R.id.spinnerCategoria);

		// spTipoMoneda.setAdapter(new
		// ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cadena));

		if(!haveInternet(getActivity().getApplicationContext())){
			showMessage("Por favor verifica tu conexión a internet.");
			return rootView;
		}
		
		// Calling async task to get json
		token = this.getPreferencesByKey("token");
		String[] data = { token };
		new GetTipoMoneda().execute(data);

		linearLayoutGroupImages = (LinearLayout) rootView
				.findViewById(R.id.linearLayoutGroupImages);

		button1 = (Button) rootView.findViewById(R.id.button1);
		imageView1 = new ImageView[4];
		imageView2 = new ImageView[4];

		imageView1[index] = (ImageView) rootView.findViewById(R.id.imageView1);
		imageView2[index] = (ImageView) rootView.findViewById(R.id.imageView2);

		imageView2[index].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("", "voy por index:" + index);
				loadIntent();
			}
		});

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String queVendes = editTextQueVendes.getText().toString();
				String descripcion = editTextDescripcion.getText().toString();
				String precio = editTextPrecio.getText().toString();
				String tipoMoneda = spTipoMoneda.getSelectedItem().toString();
				String categoria = spCategoria.getSelectedItem().toString();
				int posTipoMoneda = spTipoMoneda.getSelectedItemPosition();
				int posCategoria = spCategoria.getSelectedItemPosition();
				
				if(pathImages.size()==0){
					showMessage("Debe seleccionar al menos una foto.");
					return;
				}
				
				if(queVendes.length()==0){
					showMessage("Debes indicar que vendes");
					if(editTextQueVendes.requestFocus()) {
					    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					}
					return;
				}
				
				if(descripcion.length()==0){
					showMessage("Debes introducir una descripción");
					if(editTextDescripcion.requestFocus()) {
					    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					}
					return;
				}
				
				if(precio.length()==0){
					showMessage("Debes introducir el precio");
					if(editTextPrecio.requestFocus()) {
					    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					}
					return;
				}
				
				Log.i("", "queVendes:" + queVendes);
				Log.i("", "descripcion:" + descripcion);
				Log.i("", "precio:" + precio);
				Log.i("", "tipoMoneda:" + tipoMoneda);
				Log.i("", "categoria:" + categoria);
				Log.i("", "posTipoMoneda:" + posTipoMoneda);
				Log.i("", "posCategoria:" + posCategoria);
				Log.i("", "tamano img:" + pathImages.size());
				for (String path : pathImages) {
					Log.i("", "path:" + path);
				}
				
				if(!haveInternet(getActivity().getApplicationContext())){
					showMessage("Por favor verifica tu conexión a internet.");
					return;
				}

				// Calling async task to get json
				String[] data = { token, idCategoria.get(posCategoria),
						idMoneda.get(posTipoMoneda), precio, descripcion,
						queVendes, "12", "12" };
				new CrearProducto().execute(data);

			}
		});

		return rootView;
	}
	
	private void showMessage(String message){
		Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	private void loadIntent() {
		// Intent intent = new Intent();
		// intent.setType("image/*");
		// intent.setAction(Intent.ACTION_GET_CONTENT);
		// startActivityForResult(Intent.createChooser(intent,
		// "Select Picture"), REQUEST_IMAGE_CAPTURE);
		// actual
		// Intent intent = new Intent(Intent.ACTION_PICK,
		// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

		// TODO Auto-generated method stub
		Intent intent = new Intent();
		// call android default gallery
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		startActivityForResult(
				Intent.createChooser(intent, "Complete action using"),
				REQUEST_IMAGE_CAPTURE);

	}

	private void loadNewImage() {
		if (index < 4) {

			int margin = (int) getResources().getDimension(
					R.dimen.rl_publish_margin);

			RelativeLayout r = new RelativeLayout(getActivity()
					.getApplicationContext());

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			layoutParams.leftMargin = margin;
			layoutParams.rightMargin = margin;
			r.setLayoutParams(layoutParams);

			r.setBackgroundResource(R.color.relative_layout_publish);
			r.requestLayout();

			imageView1[index] = new ImageView(getActivity()
					.getApplicationContext());
			imageView1[index].setImageResource(R.drawable.picture);

			int dimen = (int) getResources().getDimension(R.dimen.img_publish);
			// imageView1[index].setLayoutParams(new LayoutParams(dimen,
			// dimen));
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params2.addRule(RelativeLayout.CENTER_VERTICAL);
			params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params2.width = dimen;
			params2.height = dimen;
			imageView1[index].setLayoutParams(params2);

			imageView2[index] = new ImageView(getActivity()
					.getApplicationContext());
			imageView2[index].setImageResource(android.R.drawable.ic_input_add);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			imageView2[index].setLayoutParams(params);

			imageView2[index].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("", "voy por index:" + index);
					loadIntent();
				}
			});

			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
					100, 100);
			r.addView(imageView1[index]);
			r.addView(imageView2[index]);
			linearLayoutGroupImages.addView(r, layoutParams);

		}
	}

	List<String> pathImages = new ArrayList<String>();

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_IMAGE_CAPTURE
				&& resultCode == getActivity().RESULT_OK) {
			Log.i("", "index onActivityResult:" + index);
			// Bundle extras = data.getExtras();
			// Bitmap imageBitmap = (Bitmap) extras.get("data");
			// imageView1[index].setImageBitmap(imageBitmap);

			// gallery
			Uri _uri = data.getData();
			// launchCropImage(_uri);
			// if (_uri != null) {
			// return;
			// }
			// User had pick an image.

			Cursor cursor = getActivity()
					.getContentResolver()
					.query(_uri,
							new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
							null, null, null);
			cursor.moveToFirst();

			// Link to the image final 
			String imageFilePath =cursor.getString(0);
			cursor.close();
			Log.i("", "imageFilePath:" + imageFilePath);
			pathImages.add(imageFilePath);
			Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
			imageView1[index].setImageBitmap(imageBitmap);
			imageView2[index].setVisibility(View.INVISIBLE);
			index++;
			loadNewImage();

		}
	}

	

	private class GetTipoMoneda extends AsyncTask<String, Void, Void> {

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
					R.urls.url_base)
					+ getResources().getString(R.urls.url_moneda_categoria);

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
//			Toast.makeText(getActivity().getApplicationContext(),
//					"token obtenido:" + response.getToken(), Toast.LENGTH_SHORT)
//					.show();
			if (response.getToken() != null) {
				savePreferences("token", response.getToken());
				/*
				 * Toast.makeText(context, "Guardando token:" +
				 * response.getToken(), Toast.LENGTH_SHORT).show();
				 */
			}

			if (response.getResponse().equals("OK")) {

				for (Currency c : response.getCurrency()) {
					valorMoneda.add(new String(c.getName()));
					idMoneda.add(new String(c.getId()));
				}

				for (Categories c : response.getCategories()) {
					valorCategoria.add(new String(c.getName()));
					idCategoria.add(new String(c.getId()));
				}

				spTipoMoneda.setAdapter(new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_spinner_dropdown_item,
						valorMoneda));
				spCategoria.setAdapter(new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_spinner_dropdown_item,
						valorCategoria));
			}

			if (response.getResponse().equals("KO2")) {
				clearSharedPreferenes();
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

	private String getPreferencesByKey(String key) {
		SharedPreferences sharedPreferences = this.getActivity()
				.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, null);
	}

	/*
	 * public void onActivityResult(int requestCode, int resultCode, Intent
	 * data) {
	 * 
	 * if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==
	 * getActivity().RESULT_OK) { Log.i("", "index:" + index); Bundle extras =
	 * data.getExtras(); Bitmap imageBitmap = (Bitmap) extras.get("data");
	 * imageView1[index].setImageBitmap(imageBitmap); index++; loadNewImage(); }
	 * }
	 */

	private class CrearProducto extends AsyncTask<String, Void, Void> {

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
					R.urls.url_base)
					+ getResources().getString(R.urls.url_crear_producto);

			// AÑADIR PARAMETROS
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("token", arg0[0]));
			data.add(new BasicNameValuePair("categoria", arg0[1]));
			data.add(new BasicNameValuePair("moneda", arg0[2]));
			data.add(new BasicNameValuePair("importe", arg0[3]));
			data.add(new BasicNameValuePair("descripcion", arg0[4]));
			data.add(new BasicNameValuePair("texto_destacado", arg0[5]));
			data.add(new BasicNameValuePair("latitud", arg0[6]));
			data.add(new BasicNameValuePair("longitud", arg0[7]));

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
				Log.e("", "response null ");
				return;
			}
//			Toast.makeText(getActivity().getApplicationContext(),
//					"token obtenido:" + response.getToken(), Toast.LENGTH_SHORT)
//					.show();
			if (response.getToken() != null) {
				token = response.getToken();
				savePreferences("token", response.getToken());
				/*
				 * Toast.makeText(context, "Guardando token:" +
				 * response.getToken(), Toast.LENGTH_SHORT).show();
				 */
			}

			if (response.getId() != null) {
				idProducto = response.getId();
			}

			if (response.getResponse().equals("OK")) {

				Log.i("", "Se creo el producto");
				index=0;
				fileUpload(pathImages.get(index));
			}

			if (response.getResponse().equals("KO2")) {
				clearSharedPreferenes();
			}

			pd.dismiss();
		}

	}

	private void fileUpload(String pathFile) {

		String url = getResources().getString(R.urls.url_base)
				+ getResources().getString(R.urls.url_upload_foto_producto);
		File myFile = new File(pathFile);
		RequestParams params = new RequestParams();
		try {
			params.put("token", token);
			params.put("id", idProducto);
			params.put("MAX_FILE_SIZE", "10000000000000000000000");
			params.put("userfile", myFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				pd.show();
			}

			@Override
			public void onSuccess(String jsonStr) {
				System.out.println(jsonStr);
				Log.i("", "jsonStr:" + jsonStr);
				if (jsonStr != null) {
					try {
						ParserJsonListadoProductos parser = new ParserJsonListadoProductos(
								jsonStr);
						response = parser.getResponse();
						Log.d("Response: ", "> " + response.getResponse());
						Log.d("Response: ", "Token> " + response.getToken());

						if (response.getToken() != null) {
							token = response.getToken();
							savePreferences("token", response.getToken());
						}
						
						if(response.getResponse().equals("OK")){
							index++;
							Log.d("", "index:" + index);
							Log.d("", "pathImages.size():" + pathImages.size());
							if(index<pathImages.size()){
								fileUpload(pathImages.get(index));
							}else{
								Intent intent = getActivity().getIntent();
								getActivity().finish();
								startActivity(intent);
							}
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.e("ParserJsonListadoProductos",
								"no se puede parsear");
						e.printStackTrace();
					}
				} else {
					Log.e("ParserJsonListadoProductos",
							"Couldn't get any data from the url");
				}
			}

			@Override
			public void onFinish() {
				pd.dismiss();
			}
		});
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
