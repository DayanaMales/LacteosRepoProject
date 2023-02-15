package com.example.lacteos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.StringPrepParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Acopio extends AppCompatActivity {
    private Spinner spnprov;
    private EditText etlitros, etfecha1;
    private EditText etproyeccion;
    private Button btnguardar,btnmodificar,btnbuscar,btneliminar,btnregresar,btnselectfecha;
    RequestQueue requestQueue;
    int dia,mes,anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acopio);
        spnprov=(Spinner)findViewById(R.id.spnprov);
        String[] opciones = {"SELECCIONE VISTA", "Maria Lema", "Pedro Fernandez", "Olga Quiñonez"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spnprov.setAdapter(adapter);
        etfecha1=(EditText) findViewById(R.id.etfecha1);
       etlitros=(EditText) findViewById(R.id.etlitros);
       etproyeccion=(EditText) findViewById(R.id.etproyeccion);
       btnselectfecha=(Button) findViewById(R.id.btnselectfecha);
       btnguardar=(Button) findViewById(R.id.btnguardar);
        btnmodificar=(Button) findViewById(R.id.btnmodificar);
        btnbuscar=(Button) findViewById(R.id.btnbuscar);
        btneliminar=(Button) findViewById(R.id.btneliminar);
        btnregresar=(Button) findViewById(R.id.btnregresar);
btnselectfecha.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        final Calendar calendario=Calendar.getInstance();
        dia=calendario.get(Calendar.DAY_OF_MONTH);
        mes=calendario.get(Calendar.MONTH);
        anio=calendario.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(Acopio.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int AnioSeleccionado, int MesSeleccionado, int DiaSeleccionado) {
               String diaFormateado,mesFormateado;
                if (DiaSeleccionado<10) {
                    diaFormateado = "0" + String.valueOf(DiaSeleccionado);
                }else {
                    diaFormateado = String.valueOf(DiaSeleccionado);

                }int mes=MesSeleccionado+1;
                if (mes<10) {
                    mesFormateado = "0" + String.valueOf(mes);
                }else {
                    mesFormateado=String.valueOf(mes);
                }
                etfecha1.setText(diaFormateado+"/"+mesFormateado+"/"+AnioSeleccionado);
            }
        },anio,mes,dia);
        datePickerDialog.show();
    }
});
        btnregresar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            regresar();
        }
    });

        btnguardar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ejecutarServico("http://192.168.1.4/bdlacteos/centroacopio/insertardatos.php");
        }
    });
        btnbuscar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buscar("http://192.168.1.4/bdlacteos/centroacopio/buscardatos.php?proveedor="+spnprov.getSelectedItem().toString()+"");
        }
    });
        btnmodificar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            modificar("http://192.168.1.4/bdlacteos/centroacopio/actualizardatos.php?proveedor="+spnprov.getSelectedItem().toString()+"");
        }
    });
        btneliminar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            eliminar("http://192.168.1.4/bdlacteos/centroacopio/borrardatos.php?proveedor="+spnprov.getSelectedItem().toString()+"");
        }
    });
}

    private void ejecutarServico(String URL){
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
                parametros.put("fecha",etfecha1.getText().toString());
                parametros.put("numlitros",etlitros.getText().toString());
                parametros.put("proyeccion",etproyeccion.getText().toString());
                parametros.put("proveedor",spnprov.getSelectedItem().toString());
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
                        etfecha1.setText(jsonObject.getString("fecha"));
                        etlitros.setText(jsonObject.getString("numlitros"));
                        etproyeccion.setText(jsonObject.getString("proyeccion"));
                        String proveed=(jsonObject.getString("proveedor"));
                        setprove(proveed);
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
                parametros.put("fecha",etfecha1.getText().toString());
                parametros.put("numLitros",etlitros.getText().toString());
                parametros.put("proyeccion",etproyeccion.getText().toString());
                parametros.put("proveedor",spnprov.getSelectedItem().toString());
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
        String regresar= btnregresar.getText().toString();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("regresar", regresar);
        startActivity(i);
    }
    private void setprove(String seleccion){
        int p=Integer.parseInt(seleccion);
        spnprov.setSelection(p);
    }
}
