package com.parrilladas.primo.Orden;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.parrilladas.primo.ItemCompra;
import com.parrilladas.primo.JSON;
import com.parrilladas.primo.Mesas.ItemMesasAdapter;
import com.parrilladas.primo.Mesas.LoadMesas;
import com.parrilladas.primo.Productos.Productos;
import com.parrilladas.primo.R;
import com.parrilladas.primo.swipemenulistview.SwipeMenu;
import com.parrilladas.primo.swipemenulistview.SwipeMenuCreator;
import com.parrilladas.primo.swipemenulistview.SwipeMenuItem;
import com.parrilladas.primo.swipemenulistview.SwipeMenuListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static android.os.SystemClock.sleep;

/*
* @autor: jhony Guaman y John Morrillo
* @fecha: 01/04/2016
* @Activity Orden permite cargar toda la lista del pedido
*
* */
public class Orden extends AppCompatActivity {
    /*Listafinal darle un nombre diferente */
    ArrayList<ItemCompra> items = new ArrayList<ItemCompra>();
    public static ArrayList<ItemCompra> listafinal = new ArrayList<ItemCompra>();
    public static ArrayList<Long> ids = new ArrayList<Long>();
    public static ArrayList<String> presa= new ArrayList<String>();
    public static ArrayList<Long> idE = new ArrayList<Long>();
    public static final String NOMBRE_CARPETA_APP = "com.example.jhonker.efood2";
    public static final String GENERADOS = "MisArchivos";
    public static String data, data1;
    JSON obip = new JSON();
    SwipeMenuListView lv;
    ImageButton btn_registrar, btn_cancelar, btn_add, btn_print;
    TextView txt_total, txt_tab2;
    OrdenAdapter adapter;
    String nom1;
    long id1;
    int number, number1, cantbase, idbase, cantidad,stock;
    int cantida_bd=0;
    double pre2, pvp, total = 0;
    float subtobase;
    String det=null;
    String html=null;

    private long mlas = 0;
    private long mTim = 2000;
    public static Float valor_total = (float) 0;
    public static int idpedido;
    public static int act;
    EditText detalle;
    ProgressDialog dialog = null;
    final ItemMesasAdapter jo = new ItemMesasAdapter();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orden);

        setTitle("Mesa #" + jo.mesa);
        Log.e("ACT ",String.valueOf(act));


        lv            = (SwipeMenuListView) findViewById(R.id.listView);
        txt_total     = (TextView) findViewById(R.id.txt_total);
        txt_tab2      = (TextView) findViewById(R.id.tab2_text);
        btn_registrar = (ImageButton) findViewById(R.id.tab2RealizarP);
        btn_cancelar  = (ImageButton) findViewById(R.id.tab1_cancelar);
        btn_add       = (ImageButton) findViewById(R.id.tab3add);
        /*if(act==1){
            btn_print = (ImageButton) findViewById(R.id.tab4print);
            btn_print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("IDPEDIDO", String.valueOf(idpedido));
                    generarPDFOnClick(v);
                }
            });
        }*/
                 /*
        Comprobar la disponibilidad de la Red
         */
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Thread tr= new Thread(){
                    @Override
                    public void run() {
                     html= leerHTML(String.valueOf(idpedido));
                    }
                };tr.start();
            } else {
                Toast.makeText(this, "Error de conexion", Toast.LENGTH_LONG).show();
            }
           } catch (Exception e) {
            e.printStackTrace();
        }

        if (listafinal.isEmpty() != true) {
            adapter = new OrdenAdapter(this, listafinal);
            lv.setAdapter(adapter);
            valor_total = (float) 0;
            //para obtener el total del carrito
            for (ItemCompra item : listafinal) {
                Float sub, cal;
                sub = Float.parseFloat(item.getPreciostatico());
                cal = sub * item.getCant();
                valor_total = valor_total + cal;
                txt_total.setText("$" + String.valueOf(Redondear(valor_total)));
            }
        }
        if(act==1){
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act=1;
                    Intent i = new Intent(v.getContext(), Productos.class);
                    v.getContext().startActivity(i);
                }
            });
        }else{
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act=0;
                    Intent i = new Intent(v.getContext(), Productos.class);
                    v.getContext().startActivity(i);
                }
            });
        }

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Orden.this, "", "Retornando a las mesas...", true);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Thread timer = new Thread() {
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            dialog.dismiss();
                            listafinal.clear();
                            ids.clear();
                            Intent iab = new Intent(Orden.this, LoadMesas.class);
                            startActivity(iab);
                            finish();
                            act = 0;
                        }
                    }
                };timer.start();
            }
        });

        if (act == 1) {
            txt_tab2.setText("Actualizar Pedido");
            btn_registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = ProgressDialog.show(Orden.this, "", "Actualizando Orden...", true);

                    new Thread(new Runnable() {
                        public void run() {
                            actualizar();
                            for (ItemCompra item : listafinal) {
                                subtobase = Float.parseFloat(item.getPrecio());
                                pvp = Float.parseFloat(String.valueOf(item.getPrecio_secos()));
                                cantbase = item.getCant();
                                idbase = (int) item.getId();
                                InputStream is = null;
                                // http post
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                if (idE.isEmpty() != true) {
                                    for (int x = 0; x < idE.size(); x++) {
                                        eliminar(x);
                                    }
                                }
                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("idplato", String.valueOf(idbase)));
                                nameValuePairs.add(new BasicNameValuePair("idpedido", String.valueOf(idpedido)));
                                nameValuePairs.add(new BasicNameValuePair("cantidad", String.valueOf(cantbase)));
                                nameValuePairs.add(new BasicNameValuePair("subtotal", String.valueOf(subtobase)));
                                nameValuePairs.add(new BasicNameValuePair("pvp", String.valueOf(item.getPreciostatico())));
                                try {
                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpPost httppost = new HttpPost("http://" + obip.ip + "PhpAndroid/Actualizar_detallepedido.php");
                                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                    HttpResponse response = httpclient.execute(httppost);
                                    HttpEntity entity = response.getEntity();
                                    data1 = EntityUtils.toString(entity);
                                    Log.e("ultimoid", data1);
                                } catch (Exception e) {
                                    Log.e("log_tag", "Error al registrar el detalle pedido");
                                    Toast.makeText(getApplicationContext(), "Error comunicar a sistema", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                            dialog.dismiss();
                            listafinal.clear();
                            ids.clear();
                            idE.clear();
                            Intent iab = new Intent(Orden.this, LoadMesas.class);
                            startActivity(iab);
                            finish();
                        }
                    }).start();
                    act=0;
                }


            });
        } else {
            btn_registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {


                    if (listafinal.isEmpty() == true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Orden.this);
                        // 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("El pedido no contiene Platos").setTitle("Alert!");
                        builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        // 3. Get the AlertDialog from create()
                        builder.show();
                    } else {

                        dialog = ProgressDialog.show(Orden.this, "", "Validando Orden...", true);

                        new Thread(new Runnable() {
                            public void run() {

                               /* registrar();

                                if (data.equals("OK"))
                                {

                                }

                                for (ItemCompra item : listafinal) {
                                    subtobase = Float.parseFloat(item.getPrecio());
                                    pvp = Float.parseFloat(String.valueOf(item.getPrecio_secos()));
                                    cantbase = item.getCant();
                                    idbase = (int) item.getId();

                                    // http post
                                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                    nameValuePairs.add(new BasicNameValuePair("id_producto", String.valueOf(idbase)));
                                    nameValuePairs.add(new BasicNameValuePair("id_pedido", data));




                                    nameValuePairs.add(new BasicNameValuePair("cantidad", String.valueOf(cantbase)));
                                    nameValuePairs.add(new BasicNameValuePair("subtotal", String.valueOf(subtobase)));
                                    nameValuePairs.add(new BasicNameValuePair("pvp", String.valueOf(item.getPreciostatico())));


                                    try {
                                        HttpClient httpclient = new DefaultHttpClient();
                                        HttpPost httppost = new HttpPost("http://" + obip.ip + "PhpAndroid/registrarpedidodetalle.php");
                                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                        HttpResponse response = httpclient.execute(httppost);
                                        HttpEntity entity = response.getEntity();
                                        data1 = EntityUtils.toString(entity);
                                        Log.e("ultimoid", data1);
                                        //is = entity.getContent();
                                    } catch (Exception e) {
                                        Log.e("log_tag", "Error al registrar el detalle pedido");
                                    }
                                    Log.i("Campos", String.valueOf(subtobase) + " " + String.valueOf(cantbase) + " " + String.valueOf(idbase));
                                    dialog.dismiss();
                                } */

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                subtobase = Float.parseFloat(((String) txt_total.getText()).substring(1));

                                JSONArray tabla = new JSONArray();
                                for (ItemCompra item : listafinal) {
                                    JSONObject ob = new JSONObject();
                                    try {
                                        ob.put("id_producto",item.getId());
                                        ob.put("cantidad",item.getCant());
                                        ob.put("subtotal",item.getPrecio());
                                        ob.put("pvp",item.getPreciostatico());

                                        tabla.put(ob);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                                // http post
                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("id_mesa", String.valueOf(jo.mesa)));
                                nameValuePairs.add(new BasicNameValuePair("subtotal", String.valueOf(subtobase)));
                                nameValuePairs.add(new BasicNameValuePair("totalPagado", String.valueOf(subtobase)));
                                nameValuePairs.add(new BasicNameValuePair("estado", "proceso"));
                                nameValuePairs.add(new BasicNameValuePair("id_usuario", dato.getString("p_id", null)));
                                nameValuePairs.add(new BasicNameValuePair("detalle",tabla.toString()));


                                try {
                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpPost httppost = new HttpPost("http://" + obip.ip + "PhpAndroid/registrarpedido.php");
                                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                    HttpResponse response = httpclient.execute(httppost);
                                    HttpEntity entity = response.getEntity();
                                    //is = entity.getContent();
                                    data = EntityUtils.toString(entity);
                                    Log.e("RESPUESTA", data);
                                    // Toast.makeText(getApplicationContext(), "Su Pedido Fue Registrado Correctamente", Toast.LENGTH_LONG).show();
                                    // Intent iab = new Intent(Registrar.this, AndroidPHPConnectionDemo.class);
                                    //startActivity(iab);
                                } catch (Exception e) {
                                    Log.e("log_tag", "ERROR AL REGISTRAR EL PEDIDO " + e.toString());
                                }
                                dialog.dismiss();
                                html=null;
                                final String id_p=data.replaceAll("\"","");
                                //Log.e("data", data);
                                Log.e("RESPUESTA REPLACE", id_p);
                                //Log.e("idpedido",String.valueOf(idpedido));



                            }
                        }).start();
                        // 1. Instantiate an AlertDialog.Builder with its constructor


                        /*Toast.makeText(getApplicationContext(), "Su Pedido Fue Registrado Correctamente", Toast.LENGTH_SHORT).show();
                            listafinal.clear();
                            ids.clear();
                            Intent iab = new Intent(Orden.this, LoadMesas.class);
                            startActivity(iab);*/

                       AlertDialog.Builder builder = new AlertDialog.Builder(Orden.this);
                        // 2. Chain together various setter methods to set the dialog characteristics
                        builder.setCancelable(false);
                        builder.setMessage("Pedido Realizado con éxito...")
                                .setTitle("Alert!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // generarPDFOnClick1(v);
                                listafinal.clear();
                                ids.clear();
                                Intent iab = new Intent(Orden.this, LoadMesas.class);
                                startActivity(iab);
                                finish();
                            }
                        });
                     /* builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listafinal.clear();
                                ids.clear();
                                Intent iab = new Intent(Orden.this, LoadMesas.class);
                                startActivity(iab);
                            }
                        });*/
                        // 3. Get the AlertDialog from create()
                        builder.show();


                    }

                }
            });
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ItemCompra itemC = listafinal.get(position);

                if (listafinal.get(position).isSeleccionado()) {
                    listafinal.get(position).setSeleccionado(false);
                    Toast.makeText(Orden.this, itemC.getPrecio_secos() + " " + listafinal.get(position).isSeleccionado(), Toast.LENGTH_SHORT).show();

                } else {
                    listafinal.get(position).setSeleccionado(true);
                    Toast.makeText(Orden.this, itemC.getPrecio_secos() + " " + listafinal.get(position).isSeleccionado(), Toast.LENGTH_SHORT).show();

                }
                adapter.notifyDataSetChanged();
                // AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Log.i("mi error", "seleccionastes");
                ItemCompra elegido = (ItemCompra) parent.getItemAtPosition(position);

                Long r = lv.getAdapter().getItemId(position);
                number = position;
                number1 = Integer.valueOf(r.intValue());
                nom1 = itemC.getNombre();
                pre2 = Float.parseFloat(elegido.getPreciostatico());
                id1 = elegido.getId();
                cantidad = elegido.getCant();
                stock = elegido.getStock();
                int texto1 = elegido.getCant();
                Log.e("SOTCK DEL PLATO ", String.valueOf(stock));
                ///BUSCAR LA CANTIDAD AKI
                Log.e("ID DEL PLATO ", String.valueOf(id1));
                // LLAMAR A UNA FUNCION RETORNE ID
                mo2(nom1, cantidad);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(203,
                        14, 42)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(56, 142, 60)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.speech);
                menu.addMenuItem(item2);
            }
        };


        lv.setMenuCreator(creator);
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                //ItemCompra item = listafinal.get(position);
                switch (index) {

                    case 0:

                        if (ids.size() > 1 ) {
                            if (act == 1) {
                                idE.add(listafinal.get(position).getId());
                            }

                            listafinal.remove(position);
                            ids.remove(position);

                            //listafinal.remove(position);
                            adapter.notifyDataSetChanged();
                            valor_total = (float) 0;
                            for (ItemCompra item : listafinal) {
                                Float sub, cal;
                                sub = Float.parseFloat(item.getPreciostatico());
                                cal = sub * item.getCant();
                                valor_total = valor_total + cal;
                            }

                            BigDecimal big = new BigDecimal(valor_total + "");
                            big = big.setScale(2, RoundingMode.HALF_UP);
                            txt_total.setText("$" + String.valueOf(big));
                            for (int x = 0; x < listafinal.size(); x++) {
                                Log.e("Lista Despues", "lista del carrito despues de la eliminacion" + String.valueOf(listafinal.get(x).getId()));
                            }
                            for (int x = 0; x < ids.size(); x++) {
                                Log.e("Delete OK", "lista del idcarrito despues de la eliminacion:" + String.valueOf(ids.get(x)));
                            }
                        }
                        else Toast.makeText(getApplicationContext(), "No puede quedar vacio, Elimine pedido desde la caja", Toast.LENGTH_SHORT).show();


                        break;
                    case 1:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Orden.this);
                        LayoutInflater inflater = Orden.this.getLayoutInflater();
                        View v = inflater.inflate(R.layout.dialog_detalle, null);
                        builder.setView(v);
                         detalle = (EditText) v.findViewById(R.id.txt_detalle);
                        builder.setPositiveButton("Agregar Detalle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int id_p = idpedido;
                                final long id = listafinal.get(position).getId();
                                det = detalle.getText().toString();
                                Log.e("detalle", det);
                                Thread tr= new Thread(){
                                    @Override
                                    public void run() {
                                        agregardetalle(id_p, id, det);
                                    }
                                }; tr.start();
                                Toast.makeText(getApplicationContext(), "Agregar detalle - crerrado dialog" + id_p + "-" + id, Toast.LENGTH_SHORT).show();

                            }
                        });
                        builder.show();
                        break;
                }
                return false;
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orden, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancelar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Orden.this);
            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Esta seguro que desea Cancelar Pedido!").setTitle("Alert!");
            builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog = ProgressDialog.show(Orden.this, "", "Eliminando Pedido espere un momento...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            sleep(2000);
                            CancelarPedido();
                        }

                        }).start();
                    dialog.dismiss();
                    listafinal.clear();
                    ids.clear();
                    Intent iab = new Intent(Orden.this, LoadMesas.class);
                    startActivity(iab);
                    finish();
                }
            });
            builder.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        Toast ds = Toast.makeText(this, "Pulse de Cancelar para Salir", Toast.LENGTH_SHORT);
        long Cur = System.currentTimeMillis();

        if (Cur - mlas > mTim) {

            ds.show();
            mlas = Cur;

        } else {
            ds.cancel();



           // super.onBackPressed();
        }
    }

    public void mo2(String nombre, int cant) {

        AlertDialog.Builder alert = new AlertDialog.Builder(Orden.this);
        alert.setTitle("Ingrese la candiad de:" + nombre);

        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.AppTheme_Picker);


        final NumberPicker numberPicker = new NumberPicker(cw);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setValue(cant);
        numberPicker.setWrapSelectorWheel(false);




        // numberPicker.setBackgroundColor(getResources().getColor(R.color.button_normal));
       // numberPicker.getBackground().setAlpha(233);

        alert.setView(numberPicker);

        ///getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED,
        //	WindowManager.LayoutParams.ALPHA_CHANGED);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                if (stock < numberPicker.getValue()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Orden.this);
                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("El producto seleccionano se encuentra bajo en stock solo existen: "+stock)
                            .setTitle("Alert!");
                    builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    });
                    // 3. Get the AlertDialog from create()
                    builder.show();
                }else{
                    int new_cantidad = numberPicker.getValue();
                    Toast.makeText(Orden.this, number + "serpa" + listafinal.size(), Toast.LENGTH_LONG);
                    float to = (float) pre2;
                    float tt = new_cantidad * to;
                    Orden o = new Orden();
                    int cont = 0;
                    if (o.ids.contains(id1)) {
                        for (int x = 0; x < o.ids.size(); x++) {
                            if (o.ids.get(x).intValue() == id1) {
                                int current_cant = o.listafinal.get(x).getCant();
                                float current_precio = Float.parseFloat(o.listafinal.get(x).getPrecio());
                                if (current_cant > new_cantidad) {
                                    while (new_cantidad < current_cant) {
                                        cont = cont + 1;
                                        new_cantidad = new_cantidad + 1;
                                    }
                                    o.listafinal.set(x, new ItemCompra(id1, nom1, String.valueOf(tt), new_cantidad - cont, String.valueOf(pre2),stock));
                                    Log.e("NEW_CANTIDAD", String.valueOf(new_cantidad));
                                    Log.e("valor de cont", String.valueOf(cont));
                                } else if (current_cant < new_cantidad) {
                                    while (new_cantidad > current_cant) {
                                        cont = cont + 1;
                                        current_cant++;
                                    }
                                    o.listafinal.set(x, new ItemCompra(id1, nom1, String.valueOf(tt), current_cant, String.valueOf(pre2),stock));

                                    Log.e("valor de cont", String.valueOf(cont));
                                    Log.e("CURRENT_CANT", String.valueOf(current_cant));
                                }
                                adapter.notifyDataSetChanged();
                                valor_total = (float) 0;
                                for (ItemCompra item : listafinal) {
                                    Float sub, cal;
                                    sub = Float.parseFloat(item.getPreciostatico());
                                    cal = sub * item.getCant();
                                    valor_total = valor_total + cal;
                                }
                                txt_total.setText("$"+String.valueOf(valor_total));


                            }
                        }
                        Toast.makeText(Orden.this, "El producto se modifico ", Toast.LENGTH_LONG);
                    }
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }// fin de la funcion mo()


    public void CancelarPedido() {


        // http post
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id_pedido", String.valueOf(idpedido)));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + obip.ip + "PhpAndroid/cancelarPedido.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //is = entity.getContent();
            data = EntityUtils.toString(entity);
            Log.e("Actualizo", data);
            // Toast.makeText(getApplicationContext(), "Su Pedido Fue Registrado Correctamente", Toast.LENGTH_LONG).show();
            // Intent iab = new Intent(Registrar.this, AndroidPHPConnectionDemo.class);
            //startActivity(iab);
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }


    }
    public void registrar() {

        subtobase = Float.parseFloat(((String) txt_total.getText()).substring(1));

        JSONArray tabla = new JSONArray();
        for (ItemCompra item : listafinal) {
            JSONObject ob = new JSONObject();
            try {
                ob.put("id_producto",item.getId());
                ob.put("cantidad",item.getCant());
                ob.put("subtotal",item.getPrecio());
                ob.put("pvp",item.getPreciostatico());

                tabla.put(ob);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        // http post
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id_mesa", String.valueOf(jo.mesa)));
        nameValuePairs.add(new BasicNameValuePair("subtotal", String.valueOf(subtobase)));
        nameValuePairs.add(new BasicNameValuePair("totalPagado", String.valueOf(subtobase)));
        nameValuePairs.add(new BasicNameValuePair("estado", "proceso"));
        nameValuePairs.add(new BasicNameValuePair("id_usuario", dato.getString("p_id", null)));
        nameValuePairs.add(new BasicNameValuePair("detalle",tabla.toString()));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + obip.ip + "PhpAndroid/registrarpedido.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //is = entity.getContent();
            data = EntityUtils.toString(entity);
            Log.e("ultimoid", data);
            // Toast.makeText(getApplicationContext(), "Su Pedido Fue Registrado Correctamente", Toast.LENGTH_LONG).show();
            // Intent iab = new Intent(Registrar.this, AndroidPHPConnectionDemo.class);
            //startActivity(iab);
        } catch (Exception e) {
            Log.e("log_tag", "ERROR AL REGISTRAR EL PEDIDO " + e.toString());
        }


    }

    public String agregardetalle(int id1, long id2, String sms) {

        URL url = null;
        String lineas="";
        int respuesta=0;
        StringBuilder resul=null;
        String id_p=sms.replaceAll("","%20");
        Log.e("id_p remplazado",id_p);

        try {
            url = new URL("http://"+ obip.ip +"PhpAndroid/registrardetalle.php?idpedido="+String.valueOf(id1)+"&idplato="+String.valueOf(id2)+"&detalle='"+String.valueOf(sms)+"'");
            HttpURLConnection conecction=(HttpURLConnection)url.openConnection();
            respuesta=conecction.getResponseCode();

            resul= new StringBuilder();

            if(respuesta== HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(conecction.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while((lineas = reader.readLine()) != null){
                    resul.append(lineas);
                    Log.e("html",lineas);
                }
            }
        }catch (Exception e){

        }
        return resul.toString();
    }

    public void actualizar() {

        subtobase = Float.parseFloat(((String) txt_total.getText()).substring(1));


        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        // http post
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("idpedido", String.valueOf(idpedido)));
        nameValuePairs.add(new BasicNameValuePair("subtotal", String.valueOf(subtobase)));
        nameValuePairs.add(new BasicNameValuePair("totalpagado", String.valueOf(subtobase)));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + obip.ip + "PhpAndroid/Actualizar_pedido.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String datam = EntityUtils.toString(entity);
            Log.e("Actualzar", datam);
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }


    }

    public void eliminar(int x) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("idpedido", String.valueOf(idpedido)));
        nameValuePairs.add(new BasicNameValuePair("idpedidoD", String.valueOf(idE.get(x))));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + obip.ip + "PhpAndroid/eliminar_pedidodetalle.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            data1 = EntityUtils.toString(entity);
            Log.e("EliminarPHP", data1);

            //is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error al eliminar el detalle pedido");
            Toast.makeText(getApplicationContext(), "Error comunicar a sistema", Toast.LENGTH_SHORT).show();

        }
    }

    public void generarPDFOnClick(View v) {
        Document document = new Document(PageSize.LETTER);
        String NOMBRE_ARCHIVO = "MiArchivoPDF";
        String TARJETASD = Environment.getExternalStorageDirectory().toString(); // para saber cual es la tarjeta
        File pdfDir = new File(TARJETASD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + GENERADOS);

        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP + File.separator + GENERADOS + File.separator + NOMBRE_ARCHIVO;

        File outputfile = new File(nombre_completo);
        if (outputfile.exists()) {
            outputfile.delete();
        }

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(nombre_completo));
                    /*Crear el documento*/
            document.open();
            document.addAuthor("Efood");
            document.addCreator("Efood2");
            document.addSubject("Reporte deL pedido");
            document.addTitle("Pedido #");

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            /*String htmltoPDF = "<html><head></head><body><h1>Hola mundo</h1></body></html>";*/
            try {
                worker.parseXHtml(pdfWriter, document, new StringReader(html));
                document.close();
                Toast.makeText(this, "PDF esta generado", Toast.LENGTH_LONG).show();
                muestraPDF(nombre_completo, this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void muestraPDF(String archivo, Context context) {
        Toast.makeText(context, "Leyendo Archivo", Toast.LENGTH_LONG).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO tienen una app para abrir este tipo de dato", Toast.LENGTH_LONG).show();
        }
    }
    public String leerHTML(String id) {
        URL url = null;
        String lineas="";
        int respuesta=0;
        StringBuilder resul=null;

        try {
            url = new URL("http://"+ obip.ip +"PhpAndroid/reporte_pedido.php?idpedido="+id);
            HttpURLConnection conecction=(HttpURLConnection)url.openConnection();
            respuesta=conecction.getResponseCode();

            resul= new StringBuilder();

            if(respuesta== HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(conecction.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while((lineas = reader.readLine()) != null){
                    resul.append(lineas);
                    Log.e("html",lineas);
                }
            }
        }catch (Exception e){

        }
        return resul.toString();
    }
    public String leerHTML2(String id) {
        URL url = null;
        String lineas="";
        int respuesta=0;
        StringBuilder resul=null;

        try {
            url = new URL("http://"+ obip.ip +"PhpAndroid/reporte_pedido.php?idpedido="+id);
            HttpURLConnection conecction=(HttpURLConnection)url.openConnection();
            respuesta=conecction.getResponseCode();

            resul= new StringBuilder();

            if(respuesta== HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(conecction.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while((lineas = reader.readLine()) != null){
                    resul.append(lineas);
                    Log.e("html",lineas);
                }
            }
        }catch (Exception e){

        }
        return resul.toString();
    }
    public void generarPDFOnClick1(View v) {
        Document document = new Document(PageSize.LETTER);
        String NOMBRE_ARCHIVO = "MiArchivoPDF";
        String TARJETASD = Environment.getExternalStorageDirectory().toString(); // para saber cual es la tarjeta
        File pdfDir = new File(TARJETASD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + GENERADOS);

        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP + File.separator + GENERADOS + File.separator + NOMBRE_ARCHIVO;

        File outputfile = new File(nombre_completo);
        if (outputfile.exists()) {
            outputfile.delete();
        }

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(nombre_completo));
                    /*Crear el documento*/
            document.open();
            document.addAuthor("Efood");
            document.addCreator("Efood2");
            document.addSubject("Reporte deL pedido");
            document.addTitle("Pedido #");

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            /*String htmltoPDF = "<html><head></head><body><h1>Hola mundo</h1></body></html>";*/
            try {
                worker.parseXHtml(pdfWriter, document, new StringReader(html));
                document.close();
                Toast.makeText(this, "PDF esta generado", Toast.LENGTH_LONG).show();
                muestraPDF1(nombre_completo, this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void muestraPDF1(String archivo, Context context) {
        Toast.makeText(context, "Leyendo Archivo", Toast.LENGTH_LONG).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO tienen una app para abrir este tipo de dato", Toast.LENGTH_LONG).show();
        }
    }



    public double Redondear(double numero)
    {
        return Math.rint(numero*100)/100;
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Orden Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
