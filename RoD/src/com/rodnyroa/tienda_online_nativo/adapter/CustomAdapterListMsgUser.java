package com.rodnyroa.tienda_online_nativo.adapter;

import java.util.ArrayList;

import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos.ViewHolder;
import com.rodnyroa.tienda_online_nativo.model.Message;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomAdapterListMsgUser extends BaseAdapter {
	Context contexto;
	ArrayList<Message> listMsg;

	public CustomAdapterListMsgUser(Context contexto, ArrayList<Message> listMsg) {
		this.contexto = contexto;
		this.listMsg = listMsg;
	}

	/* static view holder class */
	static class ViewHolder {
		TextView txtMsg;
		TextView txtLastMsg;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMsg.size();
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

			convertView = mInflater.inflate(R.layout.item_msg_usr, null);
			
			if(holder==null){
				holder=new ViewHolder();
			}

			holder.txtMsg = (TextView) convertView
					.findViewById(R.id.tvMsgUsr);
			holder.txtLastMsg = (TextView) convertView
					.findViewById(R.id.tvDateMsg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Message m = this.listMsg.get(position);

		holder.txtMsg.setText(m.getMessage());
		holder.txtLastMsg.setText(m.getTs());

		return convertView;
	}

}
