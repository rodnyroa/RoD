package com.rodnyroa.tienda_online_nativo;

import com.rodnyroa.tienda_online_nativo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PublishFragment extends Fragment {
	
	public PublishFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_publish, container, false);
         
        return rootView;
    }
}
