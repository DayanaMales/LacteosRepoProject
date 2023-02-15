package com.example.lacteos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Empleados extends AppCompatActivity {
    private EditText etcedulaemp,etnombreemp,etapellidoemp,etcargo,etafiliado,etdireccionemp,ettelfemp,etcorreoemp,etcargas,etarea;
    private Button btnguardar4,btnbuscar4,btnmodificar4,btneliminar4,btnregresar4;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);
        etcedulaemp=(EditText)findViewById(R.id.etcedulaemp);
        etnombreemp=(EditText)findViewById(R.id.etnombreprov);
        etapellidoemp=(EditText)findViewById(R.id.etapellidoprov);
        etcargo=(EditText)findViewById(R.id.etcargo);
        etafiliado=(EditText)findViewById(R.id.etafiliado);
        etdireccionemp=(EditText)findViewById(R.id.etdireccionprov);
        ettelfemp=(EditText)findViewById(R.id.ettelfprov);
        etcorreoemp=(EditText) findViewById(R.id.ettelfprov);
        etcargas=(EditText)findViewById(R.id.etcargas);
        etarea=(EditText)findViewById(R.id.etarea);
        btnguardar4=(Button)findViewById(R.id.btnguardar1);
        btnbuscar4=(Button)findViewById(R.id.btnbuscar1);
        btnmodificar4=(Button)findViewById(R.id.btnmodificar1);
        btneliminar4=(Button)findViewById(R.id.btneliminar1);
        btnregresar4=(Button)findViewById(R.id.btnregresar1);
        btnregresar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regresar();
            }
        });

        btnguardar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServico("http://192.168.1.11/bdlacteos/empleados/insertardatos.php");
            }
        });
        btnbuscar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar("http://192.168.1.11/bdlacteos/empleados/buscardatos.php?cedula="+etcedulaemp.getText()+"");
            }
        });
        btnmodificar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar("http://192.168.1.11/bdlacteos/empleados/actualizardatos.php?cedula="+etcedulaemp.getText()+"");
            }
        });
        btneliminar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar("http://192.168.1.11/bdlacteos/empleados/borrardatos.php?cedula="+etcedulaemp.getText()+"");
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
                parametros.put("cedula",etcedulaemp.getText().toString());
                parametros.put("nombres", etnombreemp.getText().toString());
                parametros.put("apellidos", etapellidoemp.getText().toString());
                parametros.put("cargo", etcargo.getText().toString());
                parametros.put("afiliado", etafiliado.getText().toString());
                parametros.put("direccion", etdireccionemp.getText().toString());
                parametros.put("telefono", ettelfemp.getText().toString());
                parametros.put("correo", etcorreoemp.getText().toString());
                parametros.put("cargas", etcargas.getText().toString());
                parametros.put("area", etarea.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
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
                        etcedulaemp.setText(jsonObject.getString("cedula"));
                        etnombreemp.setText(jsonObject.getString("nombres"));
                        etapellidoemp.setText(jsonObject.getString("apellidos"));
                        etcargo.setText(jsonObject.getString("cargo"));
                        etafiliado.setText(jsonObject.getString("afiliado"));
                        etdireccionemp.setText(jsonObject.getString("direccion"));
                        ettelfemp.setText(jsonObject.getString("telefono"));
                        etcorreoemp.setText(jsonObject.getString("correo"));
                        etcargas.setText(jsonObject.getString("cargas"));
                        etarea.setText(jsonObject.getString("area"));
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
                parametros.put("cedula",etcedulaemp.getText().toString());
                parametros.put("nombres", etnombreemp.getText().toString());
                parametros.put("apellidos", etapellidoemp.getText().toString());
                parametros.put("cargo", etcargo.getText().toString());
                parametros.put("afiliado", etafiliado.getText().toString());
                parametros.put("direccion", etdireccionemp.getText().toString());
                parametros.put("telefono", ettelfemp.getText().toString());
                parametros.put("correo", etcorreoemp.getText().toString());
                parametros.put("cargas", etcargas.getText().toString());
                parametros.put("area", etarea.getText().toString());
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
        String regresar= btnregresar4.getText().toString();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("regresar", regresar);
        startActivity(i);
    }
    }


