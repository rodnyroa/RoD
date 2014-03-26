package com.rodnyroa.tienda_online_nativo.adapter;

import java.util.ArrayList;

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

import com.rodnyroa.tienda_online_nativo.HomeFragment;
import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.model.RowProducto;
import com.rodnyroa.tienda_online_nativo.util.ImageThreadLoader;
import com.rodnyroa.tienda_online_nativo.util.ImageThreadLoader.ImageLoadedListener;

public class CustomAdapterListadoProductos extends BaseAdapter {

	Context contexto;
	ArrayList<RowProducto> productos;
	ImageThreadLoader imageLoader = new ImageThreadLoader(); 
	
	public CustomAdapterListadoProductos(Context contexto,ArrayList<RowProducto> productos) {
		super();
		this.contexto=contexto;
		this.productos=productos;
	}
	
	/*static view holder class*/
	static class ViewHolder {
		ImageView imagenProducto;
		TextView txtTituloProducto;
		TextView txtCostoProducto;
		ProgressBar pb;
	}

	@Override
	public int getCount() {
		return productos.size();
	}

	@Override
	public Object getItem(int position) {
		return productos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return productos.indexOf(productos.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		LayoutInflater mInflater = (LayoutInflater)
				contexto.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.producto_fila, null);
			holder = new ViewHolder();
			holder.txtTituloProducto = (TextView) convertView.findViewById(R.id.txtTituloProducto);
			holder.txtCostoProducto = (TextView) convertView.findViewById(R.id.txtCostoProducto);
			holder.imagenProducto = (ImageView) convertView.findViewById(R.id.icoProducto);
			holder.pb=(ProgressBar) convertView.findViewById(R.id.pbRowProducto);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		RowProducto producto = (RowProducto) getItem(position);
		
		
		try {
			String urlImg=HomeFragment.URL_IMG+producto.getImg().get(0).getImg();
			
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
				bitMap=null;
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		//holder.imagenProducto.setImageResource
		holder.txtTituloProducto.setText(producto.getMainText());
		holder.txtCostoProducto.setText(producto.getAmount());		
		
		return convertView;
	}
	
	

}
