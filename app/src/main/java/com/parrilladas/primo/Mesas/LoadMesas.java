package com.parrilladas.primo.Mesas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.jhonker.efood2.Report.Report;

import com.parrilladas.primo.ItemCompra;
import com.parrilladas.primo.JSON;
import com.parrilladas.primo.Login.Login;
import com.parrilladas.primo.R;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.util.ArrayList;

public class LoadMesas extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    GridView mesas;
    JSON jsonn = new JSON();
    ItemMesasAdapter s;
    private SwipeRefreshLayout swipeContainer;
    ArrayList<ItemCompra> items = new ArrayList<ItemCompra>();
    public static String mesa;
    private long mlas = 0;
    private long mTim = 2000;
public TextView nombre_usuario;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_mesas);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mesas = (GridView) findViewById(R.id.gridView);
        nombre_usuario = (TextView) findViewById(R.id.txt_usuario_mesa);
        SharedPreferences datos =  this.getSharedPreferences("perfil", Context.MODE_PRIVATE);



        if (datos.getString("p_nombre",null) != null) {
            nombre_usuario.setText("   "+datos.getString("p_nombre",null));
        }


        mesas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("STRING", "asd");
                Toast.makeText(LoadMesas.this, items.get(position).getNombre_mesa(), Toast.LENGTH_SHORT).show();
            }
        });


        new LoadAlbums().execute();
        swipeContainer.setOnRefreshListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_mesas, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Funcion no Válida", Toast.LENGTH_SHORT).show();
        /*Snackbar.make(findViewById(R.id.myCoordinatorLayout),"Pulse de nuevo para salir",
                Snackbar.LENGTH_SHORT)
                .show();*/
        /*long Cur = System.currentTimeMillis();

        if (Cur - mlas > mTim) {

            ds.show();
            mlas = Cur;

        } else {
            ds.cancel();
            super.onBackPressed();
        } */
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            da.edit().clear().commit();
            startActivity(new Intent(LoadMesas.this, Login.class));
            finish();
            return true;
        }
        /*else if(id==R.id.action_reporte){

            //startActivity(new Intent(LoadMesas.this, Report.class));

            finish();
            return true;
        }
*/

        return super.onOptionsItemSelected(item);
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LoadMesas Page") // TODO: Define a title for the content shown.
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
    class LoadAlbums extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadMesas.this);
            pDialog.setMessage("Cargando Mesas...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                items.clear();
                items = jsonn.obtenerMesas();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            pDialog.dismiss();
            s = new ItemMesasAdapter(LoadMesas.this, items);
            mesas.setAdapter(s);


        }

    }

}
