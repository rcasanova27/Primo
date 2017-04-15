package com.parrilladas.primo;

import android.app.ProgressDialog;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//import static com.example.jhonker.efood2.Report.Report.ListaR;

/**
 * Created by thespianartist on 23/07/14.
 */

/**
 *@autor  	jhony Guaman & John Morrillo
 *@date		8-febrero-2016
 * Clase JSON permite la conexion con el servidor de Postgres
 * la clase JSON contiene los metodos Obtener Reporte - ObtenerMesas -
 * ObtenerBebidas -ObtenerCaldos -ObtenerPCarta-ObtenerSecos-ObtenerProductosI
 */

public class JSON {
    //public static String ip="192.168.43.239";
    public static String ip="192.168.1.50/efood2/";
    private String json = "";
    private BufferedReader bufferedReader;
    private StringBuilder builder;
    private ProgressDialog dialog;
    ArrayList<ItemCompra> items = new ArrayList<ItemCompra>();
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    /**
     *@autor  	Jhony Guaman &John Morrillo
     *@date		16-Junio-2016
     *@name		ObtenerReport
     *          Metodo Obtener Reporte permite consultar todos los pedidos
     *          que a realizado el mesero
     *@param 			@id
     */
    public ArrayList<ItemCompra> obtenerReport(String id) throws  ParseException{
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            httppost= new HttpPost("http://"+ ip +"PhpAndroid/consultar_ventas.php"); // make sure the url is correct.
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("idusuario",id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(httppost);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("STRING", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                    for (int index = 0; index < jsonArray.length(); index++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(index);
                        long    idpedido=jsonObject.getInt("id");
                        double  totalpedido=jsonObject.getDouble("totalpagado");
                        String  estadopedido=jsonObject.getString("estado");
                        items.add( new ItemCompra(idpedido,totalpedido,estadopedido));
                      //  ListaR.add(new ItemCompra(idpedido,totalpedido,estadopedido));
                    }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }

        return items;
    }
    /**
     *@autor  	Jhony Guaman &John Morrillo
     *@date		10-febrero-2016
     *@name		ObtenerReport
     *          Metodo ObtenerMesas permite obtener las mesas
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de las mesas)
     */
    public ArrayList<ItemCompra> obtenerMesas() throws ParseException {
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_mesas.php");//
            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("STRING", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                    for (int index = 0; index < jsonArray.length(); index++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(index);
                        String pre=jsonObject.getString("numero");
                        int    est=jsonObject.getInt("estado");
                        int    idusu=jsonObject.getInt("id_usuario");
                        items.add( new ItemCompra(pre,est,idusu));
                    }
                       } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }

        return items;

    }
    /**
     *@autor  	Jhony Guaman &John Morrillo
     *@date		13-febrero-2016
     *@name		ObtenerBebidas
     *          Metodo ObtenerBebidas permite obtener todas las bebidas
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de las bebidas)
     */
    public ArrayList<ItemCompra> obtenerBebidas() throws ParseException {
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_bebidas.php");//
            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("bebidas", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    long       idS=jsonObject.getLong("id");
                    String     nombreS=jsonObject.getString("nombreplato");
                    double     precioS=jsonObject.getDouble("precio");
                    int        stock=jsonObject.getInt("cantidad");
                    int       idxcan=jsonObject.getInt("idxcantidad");
                    items.add( new ItemCompra(idS,nombreS,precioS,stock,idxcan));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }
    /**
     *@autor  	Jhony Guaman &John Morrillo
     *@date		13-febrero-2016
     *@name		ObtenerCaldos
     *          Metodo ObtenerCaldos permite obtener todas las caldos
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de las caldos)
     */
    public ArrayList<ItemCompra> obtenerCaldos() throws ParseException {

        String data;

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_caldos.php");//

            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("secos", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    long       idS=jsonObject.getLong("id");
                    String    nombreS=jsonObject.getString("nombreplato");
                    double    precioS=jsonObject.getDouble("precio");
                    int       stock=jsonObject.getInt("cantidad");
                    int       idxcan=jsonObject.getInt("idxcantidad");
                    items.add( new ItemCompra(idS,nombreS,precioS,stock,idxcan));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }
    /**
     *@autor   Jhony Guaman &John Morrillo
     *@date       13-febrero-2016
     *@name       ObtenerPCarta
     *          Metodo ObtenerPCarta permite obtener todas las platos a la Carta
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de las platos a la Carta)
     */
    public ArrayList<ItemCompra> obtenerPCarta() throws ParseException {
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_pCarta.php");//

            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("secos", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    long       idS=jsonObject.getLong("id");
                    String    nombreS=jsonObject.getString("nombreplato");
                    double    precioS=jsonObject.getDouble("precio");
                    int        stock=jsonObject.getInt("cantidad");
                    int       idxcan=jsonObject.getInt("idxcantidad");
                    items.add( new ItemCompra(idS,nombreS,precioS,stock,idxcan));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }
    /**
     *@autor   Jhony Guaman &John Morrillo
     *@date       13-febrero-2016
     *@name       ObtenerSecos
     *          Metodo ObtenerSecos permite obtener todas los secos
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de los secos)
     */
    public ArrayList<ItemCompra> obtenerSecos() throws ParseException {
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_secos.php");//

            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("secos", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    long       idS=jsonObject.getLong("id_producto");
                    String    nombreS=jsonObject.getString("descripcion");
                    double    precioS=jsonObject.getDouble("precio");
                    int        stock=jsonObject.getInt("cantidad");
                    int       idxcan=jsonObject.getInt("cantidad");
                    items.add( new ItemCompra(idS,nombreS,precioS,stock,idxcan));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }
    /**
     *@autor   Jhony Guaman &John Morrillo
     *@date       13-febrero-2016
     *@name       ObtenerProductoI
     *          Metodo ObtenerProductoI permite obtener todas los productos industrializados
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de los productos Industrializados)
     */
    public ArrayList<ItemCompra> obtenerProductoI() throws ParseException {
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_productos.php");//

            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("secos", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    long       idS=jsonObject.getLong("id");
                    String    nombreS=jsonObject.getString("nombreplato");
                    double    precioS=jsonObject.getDouble("precio");
                    int        stock=jsonObject.getInt("cantidad");
                    int       idxcan=jsonObject.getInt("idxcantidad");
                    items.add( new ItemCompra(idS,nombreS,precioS,stock,idxcan));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }
    /**
     *@autor   Jhony Guaman &John Morrillo
     *@date       13-febrero-2016
     *@name       ObtenerProductoI
     *          Metodo ObtenerProductoI permite obtener todas los productos industrializados
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de los productos Industrializados)
     */
    public ArrayList<ItemCompra> obtenerPorciones() throws ParseException {
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_porciones.php");//

            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("porciones", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    long       idS=jsonObject.getLong("id");
                    String    nombreS=jsonObject.getString("nombreplato");
                    double    precioS=jsonObject.getDouble("precio");
                    int        stock=jsonObject.getInt("cantidad");
                    int       idxcan=jsonObject.getInt("idxcantidad");
                    items.add( new ItemCompra(idS,nombreS,precioS,stock,idxcan));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }
    /**
     *@autor   Jhony Guaman &John Morrillo
     *@date       13-febrero-2016
     *@name       ObtenerProductoI
     *          Metodo ObtenerProductoI permite obtener todas los productos industrializados
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de los productos Industrializados)
     */
    public ArrayList<ItemCompra> obtenerAlmuerzos() throws ParseException {
        String data;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip +"PhpAndroid/Consultar_almuerzos.php");//

            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data= EntityUtils.toString(entity);
            Log.e("porciones", data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    long       idS=jsonObject.getLong("id");
                    String    nombreS=jsonObject.getString("nombreplato");
                    double    precioS=jsonObject.getDouble("precio");
                    int        stock=jsonObject.getInt("cantidad");
                    int       idxcan=jsonObject.getInt("idxcantidad");
                    items.add( new ItemCompra(idS,nombreS,precioS,stock,idxcan));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }
}
