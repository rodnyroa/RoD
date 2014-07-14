package com.rodnyroa.tienda_online_nativo;

import java.util.ArrayList;

import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListUserChat;
import com.rodnyroa.tienda_online_nativo.model.User;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MsgChatUsersActivity extends Activity {
	ArrayList<User> users;
	ListView lvMsgChatUsr;
	String IdProducto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_chat_users);

		lvMsgChatUsr = (ListView) findViewById(R.id.lvMsgChatUsr);
		try {
			users = (ArrayList<User>) getIntent().getSerializableExtra("Users");
			IdProducto= (String)getIntent().getSerializableExtra("IdProducto");
			Log.i("tamano", users.size() + "");
			Log.i("New win2", users.get(0).getCompleteName());

			lvMsgChatUsr.setAdapter(new CustomAdapterListUserChat(this, users));
		} catch (Exception e) {
			e.printStackTrace();
		}

		lvMsgChatUsr.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				Log.i("user", "");
				// this 'mActivity' parameter is Activity object, you can send the current activity.
		        Intent i = new Intent(getApplicationContext(), MsgUser.class);
		        i.putExtra("Msg", users.get(position).getMessages());//response.getProducts().get(position).getUsers());
		        i.putExtra("IdProducto", IdProducto);
		        startActivity(i);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.msg_chat_users, menu);
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
