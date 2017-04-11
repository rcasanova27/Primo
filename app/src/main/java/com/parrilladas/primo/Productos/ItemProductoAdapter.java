package com.parrilladas.primo.Productos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.parrilladas.primo.ItemCompra;
import com.parrilladas.primo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemProductoAdapter extends BaseAdapter {
	protected Activity activity;
	protected ArrayList<ItemCompra> items;
    public TextView nombre_caldo;
	public TextView precio_caldo;
	public TextView stock;
	public ListView lista1;
    //public ImageButton btn_mesas,btn_mesas2;
    public ItemCompra item;
	public List<ItemCompra> parkingList;

	public Context context;
	ArrayList<ItemCompra> arraylist;
	public ItemProductoAdapter(Activity activity, ArrayList<ItemCompra> items) {
		this.activity = activity;
		this.items = items;
        this.parkingList = items;
        this.context = context;
        arraylist = new ArrayList<ItemCompra>();
        arraylist.addAll(parkingList);
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
		VistaItem vistaitem;

        if(convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	vi = inflater.inflate(R.layout.item_productos, null);
			//vistaitem= new VistaItem();
			//vistaitem.chk=(CheckBox) vi.findViewById(R.id.checkpla);
			//vi.setTag(vistaitem);
        }

		else{
			vistaitem=(VistaItem) vi.getTag();
		}
        item = items.get(position);
		nombre_caldo = (TextView) vi.findViewById(R.id.txt_nombre);
		nombre_caldo.setText(String.valueOf(item.getNombre_secos()));
		precio_caldo = (TextView) vi.findViewById(R.id.txt_cantidad);
		precio_caldo.setText(String.valueOf("$" + item.getPrecio_secos()));

		/*stock =(TextView) vi.findViewById(R.id.txt_Cant);
		if(item.getStock()<0){
			stock.setText(String.valueOf(0));
		}else{
			stock.setText(String.valueOf(item.getStock()));
		}*/

        return vi;
	}

	public void filter(String charText) {

		charText = charText.toLowerCase(Locale.getDefault());

		parkingList.clear();

		if (charText.length() == 0) {
			parkingList.addAll(arraylist);

		} else {
			for (ItemCompra postDetail : arraylist) {
				if (charText.length() != 0 && postDetail.getNombre_secos().toLowerCase(Locale.getDefault()).contains(charText)) {
					parkingList.add(postDetail);
				} else if (charText.length() != 0 && String.valueOf(postDetail.getPrecio_secos()).toLowerCase(Locale.getDefault()).contains(charText)) {
					parkingList.add(postDetail);
				}
			}
		}
		notifyDataSetChanged();
	}

	static class VistaItem{
		CheckBox chk;
	}


}
