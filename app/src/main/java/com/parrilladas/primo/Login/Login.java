package com.parrilladas.primo.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parrilladas.primo.JSON;
import com.parrilladas.primo.Mesas.LoadMesas;
import com.parrilladas.primo.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText usuario, clave;
    FloatingActionButton ingresar;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    JSON obip= new JSON();
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    public static String contrasena,nombre_usuario,id_usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences dato = this.getSharedPreferences("perfil", Context.MODE_PRIVATE);
        if (dato.getString("p_nombre",null) != null) {
           startActivity(new Intent(Login.this, LoadMesas.class));
            finish();
        }
        else {
            ingresar = (FloatingActionButton) findViewById(R.id.btn_login);
            usuario= (EditText)findViewById(R.id.txt_usuario);
            clave = (EditText) findViewById(R.id.txt_clave);
            ingresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(usuario.getText().toString().isEmpty() && clave.getText().toString().isEmpty()){
                        usuario.setError("Error: Campo Obligatorio");
                        clave.setError("Error: Campo Obligatorio");
                    }else if(usuario.getText().toString().isEmpty() && !clave.getText().toString().isEmpty()){
                        usuario.setError("Error: Campo Obligatorio");
                        clave.setError(null);
                    }else if(!usuario.getText().toString().isEmpty() && clave.getText().toString().isEmpty()){
                        usuario.setError(null);
                        clave.setError("Error: Campo Obligatorio");
                    }
                    else {  usuario.setError(null);
                        clave.setError(null);
                        dialog = ProgressDialog.show(Login.this, "",
                                "Cargando...", true);
                        new Thread(new Runnable() {
                            public void run() {
                                login();
                            }
                        }).start();
                    }
                }
            });
        } }
    /**
     *@autor  	Casanova Ruiz Roberth J.
     *@date		22-Septiembre-2016
     *@name		login
     *          Metodo login permite consultar el usuario
     *          y realizar la correspondiente inicio de sesion
     */
    public void login(){
        try{
            String data;
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://"+ obip.ip +"PhpAndroid/login_meseros.php"); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("clave",clave.getText().toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("usuario",usuario.getText().toString().trim()));// $Edittext_value = $_POST['Edittext_value'];
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            HttpEntity ent= response.getEntity();
            data= EntityUtils.toString(ent);
            try {
                JSONArray json=new JSONArray(data);
                for(int i=0;i<json.length(); i++)
                {
                    JSONObject obj=json.getJSONObject(i);
                    String nombre=obj.getString("usuario");
                    String id=obj.getString("id_usuario");
                    nombre_usuario = nombre;
                    id_usuario = id;
                    Log.e("STRING", nombre);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });
            if (usuario.getText().toString().isEmpty() && clave.getText().toString().isEmpty()) {
                showAlert2();
            }
            else {
                if(!response.isEmpty()){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Login.this, "Usuario Existente",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferences datos = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = datos.edit();

                            editor.putString("p_nombre",nombre_usuario);
                            editor.putString("p_id",id_usuario);

                            editor.commit();


                        }
                    });
                    startActivity(new Intent(Login.this, LoadMesas.class));
                    finish();
                }else{
                    showAlert();
                }
            }
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }

    }
    public void showAlert(){
        Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Error de Ingreso");
                builder.setMessage("Usuario no Registrado.")
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
    public void showAlert2(){
        Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Error de Ingreso");
                builder.setMessage("Rellene los campos.")
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

}
