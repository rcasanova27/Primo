package com.parrilladas.primo.Productos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.parrilladas.primo.ItemCompra;
import com.parrilladas.primo.JSON;
import com.parrilladas.primo.Mesas.LoadMesas;
import com.parrilladas.primo.Orden.Orden;
import com.parrilladas.primo.R;

import java.text.ParseException;
import java.util.ArrayList;


public class Productos extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    int number,number1;
    public static String nom1;
    public static double pre2;
    public static long id1;
    public static int stock;
    JSON jsonn = new JSON();
    ItemProductoAdapter s;
    ArrayList<ItemCompra> items=new ArrayList<ItemCompra>();
    ListView products;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        products = (ListView) findViewById(R.id.list_productos);
        swipeContainer= (SwipeRefreshLayout) findViewById(R.id.swipe_re);
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light);
        new LoadAlbums().execute();
swipeContainer.setOnRefreshListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Orden.class);
                view.getContext().startActivity(i);
                finish();

            }
        });
        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).isSeleccionado()) {
                    items.get(position).setSeleccionado(false);
                } else {
                    items.get(position).setSeleccionado(true);
                }
                s.notifyDataSetChanged();
                ItemCompra elegido = (ItemCompra) parent.getItemAtPosition(position);

                Long r = products.getAdapter().getItemId(position);
                number = position;
                number1 = Integer.valueOf(r.intValue());

                // long h=lv.;
                //int ads=Integer.valueOf(r.intValue());
                nom1 = elegido.getNombre_secos();
                //pre1 = elegido.getPrecio();
                pre2 = elegido.getPrecio_secos();
                id1 = elegido.getId();
                int texto1 = elegido.getCant();
                stock = elegido.getStock();

                int idxcanti = elegido.getIdxcantidad();

                // Toast.makeText(getApplicationContext(),"Id"+number1+"numero posicion"+ number+ "lista"+listafinal.size()+"precio"+pre1+"\n cantidad"+texto1, Toast.LENGTH_LONG).show();
                /*if(idxcanti==1){
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    new ListRadioDialog().show(fragmentManager, "ListRadioDialog");
                }else{*/

                if (stock <= 0) {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(Productos.this);
                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("El producto seleccionano se encuentra bajo en stock")
                            .setTitle("Alert!");
                    // 3. Get the AlertDialog from create()
                    builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    mo(nom1);
                }
            }
            //}
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Actualizar el Gridview
                new LoadAlbums().execute();
                // Remove widget .
                swipeContainer.setRefreshing(false);
            }
        }, 1000);
    }


    class LoadAlbums extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Productos.this);
            pDialog.setMessage("Cargando Secos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                items.clear();
                items = jsonn.obtenerSecos();
            } catch (ParseException e){e.printStackTrace();}
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            pDialog.dismiss();
            s = new ItemProductoAdapter(Productos.this,items);
            products.setAdapter(s);
        }

    }

    @SuppressLint("ShowToast")
    public void mo(String nombre){
        final AlertDialog.Builder alert = new AlertDialog.Builder(Productos.this);
        alert.setTitle("Ingrese la candiad de: "+nombre);

        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.AppTheme_Picker);


        final NumberPicker numberPicker = new NumberPicker(cw);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        //numberPicker.setBackgroundColor(getResources().getColor(R.color.button_normal));
        //numberPicker.getBackground().setAlpha(233);








        alert.setView(numberPicker);
        alert.setCancelable(false);

        ///getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED,
        //	WindowManager.LayoutParams.ALPHA_CHANGED);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (stock< numberPicker.getValue()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Productos.this);
                    builder.setMessage("El Producto seleccionado se encuentra bajo en stock solo existen: " + stock)
                            .setTitle("Alerta!");
                    builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }else{
                    int value = numberPicker.getValue();
                    //Toast.makeText(Productos.this, number+"serpa"+listafinalsecos.size(), Toast.LENGTH_LONG);
                    float to=(float)pre2;
                    float tt=value*to;
                    Orden o = new Orden();
                    if(o.listafinal.isEmpty()){
                        o.listafinal.add(new ItemCompra(id1,nom1,String.valueOf(tt),value,String.valueOf(pre2),stock));
                        for(int x =0 ; x<o.listafinal.size() ; x++){
                            o.ids.add(o.listafinal.get(x).getId());
                        }
                        Toast.makeText(Productos.this, "Producto agregado al carrito", Toast.LENGTH_LONG);
                    }else{
                        if(o.ids.contains(id1)){
                            for(int x =0; x<o.ids.size(); x++){
                                if(o.ids.get(x).intValue()==id1){
                                    int current_cant=o.listafinal.get(x).getCant();
                                    float current_precio = Float.parseFloat(o.listafinal.get(x).getPrecio());
                                    o.listafinal.set(x,new ItemCompra(id1,nom1,String.valueOf(tt+current_precio),value+current_cant,String.valueOf(pre2),stock));
                                }
                            }
                            Toast.makeText(Productos.this, "El producto se modifico ", Toast.LENGTH_LONG);
                        }else{
                            o.listafinal.add(new ItemCompra(id1,nom1,String.valueOf(tt),value,String.valueOf(pre2),stock));
                            o.ids.add(id1);
                        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productos, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
               s.filter(searchQuery.toString().trim());
                products.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }
    @Override
    public void onBackPressed() {
        //Toast ds = Toast.makeText(this, "Funcion no Válida", Toast.LENGTH_SHORT);
        Intent iab = new Intent(Productos.this, LoadMesas.class);
        startActivity(iab);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
