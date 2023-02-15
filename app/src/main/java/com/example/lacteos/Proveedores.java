package com.example.lacteos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Proveedores extends AppCompatActivity {
    private EditText etcedulaprov,etnombreprov,etapellidoprov,etdireccionprov,etteleprov,etcorreoprov;
    private Button btnguardar1,btnbuscar1,btnmodificar1,btneliminar1,btnregresar1;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);
        etcedulaprov=(EditText)findViewById(R.id.etcedulaprov);
        etnombreprov=(EditText)findViewById(R.id.etnombreprov);
        etapellidoprov=(EditText)findViewById(R.id.etapellidoprov);
        etdireccionprov=(EditText)findViewById(R.id.etdireccionprov);
        etteleprov=(EditText)findViewById(R.id.ettelfprov);
        etcorreoprov=(EditText) findViewById(R.id.etcorreoprov);
        btnguardar1=(Button)findViewById(R.id.btnguardar1);
        btnbuscar1=(Button)findViewById(R.id.btnbuscar1);
        btnmodificar1=(Button)findViewById(R.id.btnmodificar1);
        btneliminar1=(Button)findViewById(R.id.btneliminar1);
        btnregresar1=(Button)findViewById(R.id.btnregresar1);
        btnregresar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regresar();
            }
        });

        btnguardar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServico("http://192.168.1.11/bdlacteos/proveedores/ingresardatos.php");
            }
        });
        btnbuscar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscar("http://192.168.1.11/bdlacteos/proveedores/buscardatos.php?cedula="+etcedulaprov.getText().toString()+"");
            }
        });
        btnmodificar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar("http://192.168.1.11/bdlacteos/proveedores/actualizardatos.php?cedula="+etcedulaprov.getText().toString()+"");
            }
        });
        btneliminar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar("http://192.168.1.11/bdlacteos/proveedores/borrardatos.php?cedula="+etcedulaprov.getText().toString()+"");
            }
        });
    }

    private void ejecutarServico(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("cedula",etcedulaprov.getText().toString());
                parametros.put("nombres", etnombreprov.getText().toString());
                parametros.put("apellidos", etapellidoprov.getText().toString());
                parametros.put("direccion", etdireccionprov.getText().toString());
                parametros.put("telefono", etteleprov.getText().toString());
                parametros.put("correo", etcorreoprov.getText().toString());
                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void buscar(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        etcedulaprov.setText(jsonObject.getString("cedula"));
                        etnombreprov.setText(jsonObject.getString("nombres"));
                        etapellidoprov.setText(jsonObject.getString("apellidos"));
                        etdireccionprov.setText(jsonObject.getString("direccion"));
                        etteleprov.setText(jsonObject.getString("telefono"));
                        etcorreoprov.setText(jsonObject.getString("correo"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error de conexion",Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void modificar(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("cedula",etcedulaprov.getText().toString());
                parametros.put("nombres", etnombreprov.getText().toString());
                parametros.put("apellidos", etapellidoprov.getText().toString());
                parametros.put("direccion", etdireccionprov.getText().toString());
                parametros.put("telefono", etteleprov.getText().toString());
                parametros.put("correo", etcorreoprov.getText().toString());
                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void eliminar(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void regresar() {
        String regresar= btnregresar1.getText().toString();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("regresar", regresar);
        startActivity(i);
    }
}
