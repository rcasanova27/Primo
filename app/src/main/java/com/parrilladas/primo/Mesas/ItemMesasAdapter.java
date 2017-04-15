package com.parrilladas.primo.Mesas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


//import com.example.jhonker.efood2.orden.Orden;

import com.parrilladas.primo.ItemCompra;
import com.parrilladas.primo.JSON;
import com.parrilladas.primo.Orden.Orden;
import com.parrilladas.primo.Productos.Productos;
import com.parrilladas.primo.R;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;

public class ItemMesasAdapter extends BaseAdapter {
	    protected Activity activity;
	    protected ArrayList<ItemCompra> items;
        public TextView numero_mesas;
        public ImageButton btn_mesas,btn_mesas2,btn_mesas3;
        public ItemCompra item;
	    public static String mesa;
    public TextView nombre_usuario;
    public int acu = 1;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	JSON obip= new JSON();
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;

	public ItemMesasAdapter() {
	}

	public ItemMesasAdapter(Activity activity, ArrayList<ItemCompra> items) {
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

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.item_mesas_pedido, null);

        }
        item = items.get(position);

        String pr = item.getNombre_mesa();

        int prue = Integer.parseInt(pr);

        //numero_mesas = (TextView) vi.findViewById(R.id.txt_numero);
        //numero_mesas.setText(String.valueOf(pr));

       //ESTE ES EL PROBLEMA

        if (position== 0 )
        {
            acu = position + 1;
        }
        else if ((position % 2)== 0) {acu = (position/2) +1;}


        if ((prue % 2) == 0) {

            numero_mesas = (TextView) vi.findViewById(R.id.txt_numero);
            numero_mesas.setText(String.valueOf(acu) + "B");
            acu ++;

        }

        else {

            numero_mesas = (TextView) vi.findViewById(R.id.txt_numero);
            numero_mesas.setText(String.valueOf(acu) + "A");

        }




            btn_mesas = (ImageButton) vi.findViewById(R.id.im_mesa);
            btn_mesas2 = (ImageButton) vi.findViewById(R.id.im_mesa2);
            nombre_usuario = (TextView) vi.findViewById(R.id.txt_usuario_mesa);


            if (item.getEst() == 0) {
                btn_mesas.setVisibility(View.VISIBLE);
                btn_mesas2.setVisibility(View.INVISIBLE);
            } else {
                btn_mesas.setVisibility(View.INVISIBLE);
                btn_mesas2.setVisibility(View.VISIBLE);
            }


        btn_mesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),items.get(position).getNombre_mesa(),Toast.LENGTH_SHORT).show();
				mesa = items.get(position).getNombre_mesa();
				Intent i = new Intent(v.getContext(), Productos.class);
				v.getContext().startActivity(i);

            }
        });

		btn_mesas2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {

                dialog = ProgressDialog.show(v.getContext(), "", "Validando...", true);
                new Thread(new Runnable() {
                    public void run() {
                        sleep(2000);
                        final SharedPreferences dato =  v.getContext().getSharedPreferences("perfil", Context.MODE_PRIVATE);
                        mesa = items.get(position).getNombre_mesa();



                        Thread tr= new Thread(){
                            @Override
                            public void run() {
                                final String resultado=cargarDatos(mesa,dato.getString("p_id",null));
                                if(!resultado.isEmpty()){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int r=obtenerdatosjson(resultado);
                                        if(r>0)
                                        {

                                           Orden o = new Orden();
                                            dialog.dismiss();
                                            o.act=1;
                                            Intent i = new Intent(activity.getApplicationContext(), Orden.class);
                                            activity.startActivity(i);


                                        }
                                        else
                                        {


                                        }
                                    }
                                });
                                }else{

                                    dialog.dismiss();

                                    //showAlert();
                                }
                            }
                        }; tr.start();
                    }
                }).start();

                Toast.makeText(v.getContext(),"La mesa esta Ocupada",Toast.LENGTH_LONG).show();

            }
		});



        return vi;
	}
    public String cargarDatos(String idmesa, String idusuario){
        URL url = null;
        String lineas="";
        int respuesta=0;
        StringBuilder resul=null;

        try {
            url = new URL("http://"+ obip.ip +"PhpAndroid/Consultar_pedido.php?idmesa="+idmesa+"&idusuario="+idusuario+"");
            HttpURLConnection conecction=(HttpURLConnection)url.openConnection();
            respuesta=conecction.getResponseCode();

            resul= new StringBuilder();

                if(respuesta== HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(conecction.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while((lineas = reader.readLine()) != null){
                        resul.append(lineas);
                    }
                }
        }catch (Exception e){

        }
    return resul.toString();
    }

    public void showAlert(){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());
                builder.setTitle("Error De Acceso");
                builder.setMessage("Usuario no Autorizado.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    public int obtenerdatosjson(String response){
        int res=0;
            try {
                JSONArray json = new JSONArray(response);
                if(json.length()>0){
                    res=1;
                    for(int i=0;i<json.length(); i++)
                    {

                        JSONObject obj=json.getJSONObject(i);
                        Orden o = new Orden();
                        o.idpedido=obj.getInt("id_pedido");
                        o.listafinal.add(new ItemCompra(obj.getLong("id_producto"),obj.getString("descripcion"),String.valueOf(obj.getDouble("subtotal")),obj.getInt("cantidad"),String.valueOf(obj.getDouble("pvp")),obj.getInt("stock")));
                       // for(int x =0 ; x<o.listafinal.size() ; x++){
                            o.ids.add(obj.getLong("id_producto"));
                       // }
                        //Log.e("STRING", o.listafinal);
                        Log.e("Lista del pedido","ID"+obj.getLong("id_pedido")+"NOMBRE DEL PLATO "+obj.getString("descripcion")+"CANTIDAD "+obj.getInt("cantidad")+"PRECIO: "+obj.getDouble("pvp")+"SUBTOTAL: "+obj.getDouble("subtotal"));


                    }
                }
            }catch (Exception e){
                Log.e("Error","Error al Cargar los datos: inf =>"+e);
            }
        return res;
    }

}
