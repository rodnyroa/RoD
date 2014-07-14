package com.rodnyroa.tienda_online_nativo.adapter;

import java.util.ArrayList;

import com.rodnyroa.tienda_online_nativo.HomeFragment;
import com.rodnyroa.tienda_online_nativo.R;
import com.rodnyroa.tienda_online_nativo.adapter.CustomAdapterListadoProductos.ViewHolder;
import com.rodnyroa.tienda_online_nativo.model.Imagen;
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

public class GridLayoutPreviewImagesAdapter extends BaseAdapter {

	Context contexto;
	ArrayList<Imagen> Img;
	ImageThreadLoader imageLoader = new ImageThreadLoader();

	public GridLayoutPreviewImagesAdapter(Context contexto,
			ArrayList<Imagen> Img) {
		super();
		this.contexto = contexto;
		this.Img = Img;
	}

	/* static view holder class */
	static class ViewHolder {
		ImageView imagenProducto;
		TextView textView;
		ProgressBar pb;
	}

	public GridLayoutPreviewImagesAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Img.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Img.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Img.indexOf(Img.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		Imagen i = (Imagen) getItem(position);
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
			String urlImg=HomeFragment.URL_IMG+i.getImg();
			
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
			
			holder.textView.setText("");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}

}
