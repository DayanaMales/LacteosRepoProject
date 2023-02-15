package com.example.lacteos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Productos extends AppCompatActivity {
    private EditText etcodpro,etnomproduct,etdescproduct,etunimedid,etelabproduct,etprecioproduct,etvencproduct,etpreciventproduct,etnutriproduct;
    private Button btnguardar2,btnbuscar2,btnmodificar2,btneliminar2,btnregresar2,btnelab,btnvenc;
    RequestQueue requestQueue;
    int dia,mes,anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        etcodpro=(EditText)findViewById(R.id.etcodpro);
        etnomproduct=(EditText)findViewById(R.id.etnomproduct);
        etdescproduct=(EditText)findViewById(R.id.etdescproduct);
        etunimedid=(EditText)findViewById(R.id.etunimedid);
        etelabproduct=(EditText)findViewById(R.id.etelabproduct);
        etprecioproduct=(EditText)findViewById(R.id.etprecioproduct);
        etvencproduct=(EditText)findViewById(R.id.etvencproduct);
        etpreciventproduct=(EditText)findViewById(R.id.etpreciventproduct);
        etnutriproduct=(EditText)findViewById(R.id.etnutriproduct);
        btnguardar2=(Button)findViewById(R.id.btnguardar2);
        btnbuscar2=(Button)findViewById(R.id.btnbuscar2);
        btnmodificar2=(Button)findViewById(R.id.btnmodificar2);
        btneliminar2=(Button)findViewById(R.id.btneliminar2);
        btnregresar2=(Button)findViewById(R.id.btnregresar2);
        btnelab=(Button)findViewById(R.id.btnelab);
        btnvenc=(Button)findViewById(R.id.btnvenc);
        btnelab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario=Calendar.getInstance();
                dia=calendario.get(Calendar.DAY_OF_MONTH);
                mes=calendario.get(Calendar.MONTH);
                anio=calendario.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog=new DatePickerDialog(Productos.this, new DatePickerDialog.OnDateSetListener() {
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
                        etelabproduct.setText(diaFormateado+"/"+mesFormateado+"/"+AnioSeleccionado);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });
        btnvenc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario=Calendar.getInstance();
                dia=calendario.get(Calendar.DAY_OF_MONTH);
                mes=calendario.get(Calendar.MONTH);
                anio=calendario.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog=new DatePickerDialog(Productos.this, new DatePickerDialog.OnDateSetListener() {
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
                        etvencproduct.setText(diaFormateado+"/"+mesFormateado+"/"+AnioSeleccionado);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });
        btnregresar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regresar();
            }
        });

        btnguardar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServico("http://192.168.1.11/bdlacteos/productos/insertardatos.php");
            }
        });
        btnbuscar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscar("http://192.168.1.11/bdlacteos/productos/buscardatos.php?codigo="+etcodpro.getText().toString()+"");
            }
        });
        btnmodificar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar("http://192.168.1.11/bdlacteos/productos/actualizardatos.php?codigo="+etcodpro.getText().toString()+"");
            }
        });
        btneliminar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar("http://192.168.1.11/bdlacteos/productos/borrardatos.php?codigo="+etcodpro.getText().toString()+"");
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
                parametros.put("codigo", etcodpro.getText().toString());
                parametros.put("nombre", etnomproduct.getText().toString());
                parametros.put("descripcion", etdescproduct.getText().toString());
                parametros.put("unidadMedida", etunimedid.getText().toString());
                parametros.put("elaboracion", etelabproduct.getText().toString());
                parametros.put("precio", etprecioproduct.getText().toString());
                parametros.put("vencimineto", etvencproduct.getText().toString());
                parametros.put("preventa", etpreciventproduct.getText().toString());
                parametros.put("valornutri", etnutriproduct.getText().toString());
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
                        etcodpro.setText(jsonObject.getString("codigo"));
                        etnomproduct.setText(jsonObject.getString("nombre"));
                        etdescproduct.setText(jsonObject.getString("descripcion"));
                        etunimedid.setText(jsonObject.getString("unidadMedida"));
                        etelabproduct.setText(jsonObject.getString("elaboracion"));
                        etprecioproduct.setText(jsonObject.getString("precio"));
                        etvencproduct.setText(jsonObject.getString("vencimiento"));
                        etpreciventproduct.setText(jsonObject.getString("preventa"));
                        etnutriproduct.setText(jsonObject.getString("valornutri"));

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
                parametros.put("codigo", etcodpro.getText().toString());
                parametros.put("nombre", etnomproduct.getText().toString());
                parametros.put("descripcion", etdescproduct.getText().toString());
                parametros.put("unidadMedida", etunimedid.getText().toString());
                parametros.put("elaboracion", etelabproduct.getText().toString());
                parametros.put("precio", etprecioproduct.getText().toString());
                parametros.put("vencimineto", etvencproduct.getText().toString());
                parametros.put("preventa", etpreciventproduct.getText().toString());
                parametros.put("valornutri", etnutriproduct.getText().toString());
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
        String regresar= btnregresar2.getText().toString();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("regresar", regresar);
        startActivity(i);
    }
}
