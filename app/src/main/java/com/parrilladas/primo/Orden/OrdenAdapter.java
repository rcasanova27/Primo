package com.parrilladas.primo.Orden;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.parrilladas.primo.ItemCompra;
import com.parrilladas.primo.R;
import com.parrilladas.primo.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

/*
* @autor: jhony Guaman y John Morrillo
* @fecha: 01/04/2016
* @Activity OrdenAdapter permite mostrar los componente en la lista de la orden
*
* */
public class OrdenAdapter extends BaseAdapter implements AdapterView.OnItemClickListener  {
	protected Activity activity; double pre2;
	protected ArrayList<ItemCompra> items;
	public TextView nombre_plato;
	public TextView precio_plato;
	public TextView cantidad_plato;
	public SwipeMenuListView lv;

	public OrdenAdapter(Activity activity, ArrayList<ItemCompra> items) {
		this.activity = activity;
		this.items = items;
		
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}


	@SuppressLint({ "InflateParams", "CutPasteId" })
	@Override
	public View getView(final int position, final View convertView, ViewGroup parent) {

		View vi=convertView;
		//final VistaItem vistaitem;
		//VistaItem vistaitem;
		final View we=convertView;
        if(convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	vi = inflater.inflate(R.layout.item_orden_list, null);

		}
		final ItemCompra item= items.get(position);

		nombre_plato = (TextView) vi.findViewById(R.id.txt_nombre);
		cantidad_plato = (TextView) vi.findViewById(R.id.txt_cantidad);
		precio_plato = (TextView) vi.findViewById(R.id.txt_precio);


		nombre_plato.setText(String.valueOf(item.getNombre()));
		cantidad_plato.setText(String.valueOf(item.getCant()));
		precio_plato.setText(String.valueOf(item.getPrecio()));


		return vi;

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.e("gdhdfdhfdfhdhdhdfhhfd", "jhgj");

	}
}
