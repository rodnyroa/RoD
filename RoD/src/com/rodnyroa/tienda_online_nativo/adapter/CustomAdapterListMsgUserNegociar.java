package com.rodnyroa.tienda_online_nativo.adapter;

import java.util.ArrayList;

import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos.ViewHolder;
import com.rodnyroa.tienda_online_nativo.model.Message;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomAdapterListMsgUserNegociar extends BaseAdapter {
	Context contexto;
	ArrayList<Message> listMsg;

	public CustomAdapterListMsgUserNegociar(Context contexto,
			ArrayList<Message> listMsg) {
		this.contexto = contexto;
		this.listMsg = listMsg;
	}

	/* static view holder class */
	static class ViewHolder {
		TextView txtNameUser;
		TextView txtMsg;
		TextView txtLastMsg;
	}

	@Override
	public int getCount() {
		int size=0;
		try{
			size=listMsg.size();
		}catch(Exception e){
			
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		return listMsg.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listMsg.indexOf(listMsg.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) contexto
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {

			convertView = mInflater.inflate(
					R.layout.item_msg_negociar_producto, null);

			if (holder == null) {
				holder = new ViewHolder();
			}

			holder.txtNameUser = (TextView) convertView
					.findViewById(R.id.tvNameUsrNegociar);
			holder.txtMsg = (TextView) convertView
					.findViewById(R.id.tvMsgUsrNegociar);
			holder.txtLastMsg = (TextView) convertView
					.findViewById(R.id.tvDateMsgNegociar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Message m = this.listMsg.get(position);

		try {
			holder.txtNameUser.setText(m.getUser().getName());
		} catch (Exception e) {
			holder.txtNameUser.setText("-");
		}
		holder.txtMsg.setText(m.getMessage());
		holder.txtLastMsg.setText(m.getTs());
		
		if(m.isOwner()){
			convertView.setBackgroundColor(Color.LTGRAY);
		}else{
			convertView.setBackgroundColor(Color.WHITE);
		}

		return convertView;
	}

}
