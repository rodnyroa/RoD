package com.rodnyroa.tienda_online_nativo.adapter;

import java.util.ArrayList;

import com.rodnyroa.tienda_online_nativo.HomeFragment;
import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos.ViewHolder;
import com.rodnyroa.tienda_online_nativo.model.RowProducto;
import com.rodnyroa.tienda_online_nativo.util.ImageThreadLoader;
import com.rodnyroa.tienda_online_nativo.util.ImageThreadLoader.ImageLoadedListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GridLayoutMessagesAdapter extends BaseAdapter {

	Context contexto;
	ArrayList<RowProducto> productos;
	ImageThreadLoader imageLoader = new ImageThreadLoader();

	public GridLayoutMessagesAdapter(Context contexto,
			ArrayList<RowProducto> productos) {
		super();
		this.contexto = contexto;
		this.productos = productos;
	}

	/* static view holder class */
	static class ViewHolder {
		ImageView imagenProducto;
		TextView textView;
		ProgressBar pb;
	}

	public GridLayoutMessagesAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return productos.indexOf(productos.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		RowProducto producto = (RowProducto) getItem(position);
		LayoutInflater mInflater = LayoutInflater.from(contexto);
		
        
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.gridview_item, parent, false);
			holder = new ViewHolder();
			holder.imagenProducto=(ImageView)convertView.findViewById(R.id.picture);
			holder.textView=(TextView)convertView.findViewById(R.id.text);
			holder.pb=(ProgressBar) convertView.findViewById(R.id.pbRowProductoGridView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
			String urlImg=HomeFragment.URL_IMG+producto.getUrl();
			
			Bitmap bitMap = null;
			
			//URL url = new URL(urlImg);
			//bitMap = imageLoader.readBitmapFromNetwork(url);
						
			bitMap =imageLoader.loadImage(urlImg, new ImageLoadedListener() {

				@Override
				public void imageLoaded(Bitmap imageBitmap) {
					notifyDataSetChanged(); 
				}			
			});
			if(bitMap!=null){
				holder.pb.setVisibility(View.GONE);
				holder.imagenProducto.setImageBitmap(bitMap);
				//imageView.setImageBitmap(bitMap);
				bitMap=null;
			}
			
			holder.textView.setText(producto.getUrl());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}

}
