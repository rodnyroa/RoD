package com.rodnyroa.tienda_online_nativo.adapter;

import java.util.ArrayList;

import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos.ViewHolder;
import com.rodnyroa.tienda_online_nativo.model.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomAdapterListUserChat extends BaseAdapter {
	Context contexto;
	ArrayList<User> users;

	public CustomAdapterListUserChat(Context contexto, ArrayList<User> users) {
		this.contexto = contexto;
		this.users = users;
	}

	/* static view holder class */
	static class ViewHolder {
		TextView txtCompleteName;
		TextView txtLastMsg;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return users.indexOf(users.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) contexto
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.item_msg_chat_usr, null);
			
			if(holder==null){
				holder=new ViewHolder();
			}

			holder.txtCompleteName = (TextView) convertView
					.findViewById(R.id.tvNameUsr);
			holder.txtLastMsg = (TextView) convertView
					.findViewById(R.id.tvDateMsg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		User u = this.users.get(position);

		holder.txtCompleteName.setText(u.getCompleteName());
		holder.txtLastMsg.setText(u.getLastMessage());

		return convertView;
	}

}
