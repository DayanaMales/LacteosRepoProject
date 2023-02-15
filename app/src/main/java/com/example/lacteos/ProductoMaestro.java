package com.example.lacteos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class ProductoMaestro extends AppCompatActivity {
    private EditText etnompromaestro,etdescripcion,etpeso,etacides;
    private Spinner spnSelec;
    private Button btnguardar3,btnbuscar3,btnmodificar3,btneliminar3,btnregresar3;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_maestro);
        spnSelec=(Spinner)findViewById(R.id.spnSelec);
        String[] opciones = {"SELECCIONE VISTA", "Maria Lema", "Pedro Fernandez", "Olga Quiñonez"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spnSelec.setAdapter(adapter);
        etnompromaestro=(EditText)findViewById(R.id.etnompromaestro);
        etdescripcion=(EditText)findViewById(R.id.etdescripcion);
        etpeso=(EditText)findViewById(R.id.etpeso);
        etacides=(EditText)findViewById(R.id.etacides);
        btnguardar3=(Button)findViewById(R.id.btnguardar3);
        btnbuscar3=(Button)findViewById(R.id.btnbuscar3);
        btnmodificar3=(Button)findViewById(R.id.btnmodificar3);
        btneliminar3=(Button)findViewById(R.id.btneliminar3);
        btnregresar3=(Button)findViewById(R.id.btnregresar3);
        btnregresar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regresar();
            }
        });

        btnguardar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServico("http://192.168.1.2/formulario1/producto/insertardatos.php");
            }
        });
        btnbuscar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscar("http://192.168.1.2/formulario1/producto/buscardatos.php?proveedor="+spnSelec.getSelectedItemPosition()+"");
            }
        });
        btnmodificar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar("http://192.168.1.2/formulario1/producto/actualizardatos.php?proveedor="+spnSelec.getSelectedItemPosition()+"");
            }
        });
        btneliminar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar("http://192.168.1.2/formulario1/producto/borrardatos.php?proveedor="+spnSelec.getSelectedItemPosition()+"");
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
                parametros.put("proveedor",spnSelec.getSelectedItem().toString());
                parametros.put("nombre", etnompromaestro.getText().toString());
                parametros.put("descripcion", etdescripcion.getText().toString());
                parametros.put("peso", etpeso.getText().toString());
                parametros.put("acides", etacides.getText().toString());
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
                        String proveedor=(jsonObject.getString("proveedor"));
                        etnompromaestro.setText(jsonObject.getString("nombre"));
                        etdescripcion.setText(jsonObject.getString("descripcion"));
                        etpeso.setText(jsonObject.getString("peso"));
                        etacides.setText(jsonObject.getString("acides"));

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
                parametros.put("proveedor",spnSelec.getSelectedItem().toString());
                parametros.put("nombre", etnompromaestro.getText().toString());
                parametros.put("descripcion", etdescripcion.getText().toString());
                parametros.put("peso", etpeso.getText().toString());
                parametros.put("acides", etacides.getText().toString());
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
        String regresar= btnregresar3.getText().toString();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("regresar", regresar);
        startActivity(i);
    }
}
