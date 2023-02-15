package com.example.lacteos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText etUsu;
    private EditText etclave;
    private Button btninicio;
    private Spinner spnPerfil;
    private String usuario;
    private String pass;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsu = (EditText) findViewById(R.id.etUsu);
        etclave = (EditText) findViewById(R.id.etclave);
        spnPerfil = (Spinner) findViewById(R.id.spnPerfil);
        String[] opciones = {"SELECCIONE VISTA", "Acopio", "Empleados", "Productos","Producto Maestro","Proveedores"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spnPerfil.setAdapter(adapter);
        //recuperarDatos();
        btninicio = (Button) findViewById(R.id.btninicio);

        btninicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingresar("http://192.168.1.11/bdlacteos/empleados/bubscarAdmin.php",view);
            }
        });

    }

    public void Ingresar(String URL,View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String select = spnPerfil.getSelectedItem().toString();
        usuario = etUsu.getText().toString();
        pass = etclave.getText().toString();
        Cursor fila = bd.rawQuery("Select usuario,password from admin2 where usuario='" + usuario + "' and password='"
                + pass + "'", null);

        if (fila.moveToFirst()) {

            guardarDatos();
            if (select.equals("Acopio")) {
                Intent i = new Intent(getApplicationContext(), Acopio.class);
                startActivity(i);
                finish();
            } else if (select.equals("Proveedores")) {
                Intent i = new Intent(getApplicationContext(), Proveedores.class);
                startActivity(i);
                finish();
            } else if (select.equals("Producto Maestro")) {

                Intent i = new Intent(getApplicationContext(), ProductoMaestro.class);
                startActivity(i);
                finish();
            } else if (select.equals("Productos")) {

                Intent i = new Intent(getApplicationContext(), Productos.class);
                startActivity(i);
                finish();
            } else if (select.equals("Empleados")) {

                Intent i = new Intent(getApplicationContext(), Empleados.class);
                startActivity(i);
                finish();

            } else {

            }
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    guardarDatos();
                    if (select.equals("Acopio")) {
                        Intent i = new Intent(getApplicationContext(), Acopio.class);
                        startActivity(i);
                        finish();
                    } else if (select.equals("Proveedores")) {
                        Intent i = new Intent(getApplicationContext(), Proveedores.class);
                        startActivity(i);
                        finish();
                    } else if (select.equals("Producto Maestro")) {

                        Intent i = new Intent(getApplicationContext(), ProductoMaestro.class);
                        startActivity(i);
                        finish();
                    }else if (select.equals("Productos")) {

                        Intent i = new Intent(getApplicationContext(), Productos.class);
                        startActivity(i);
                        finish();
                    }else if (select.equals("Empleados")) {

                        Intent i = new Intent(getApplicationContext(), Empleados.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    //  activar=false;
                    // bloquear(activar);
                    //customizeSnackbar();

                    Snackbar snackbar=Snackbar.make(v,"Usuario no encontrado", Snackbar.LENGTH_LONG);

                    snackbar.show();

                    //   Toast.makeText(getApplicationContext(), "Usuario no Existente", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombres", usuario);
                parametros.put("cedula", pass);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void guardarDatos() {
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("password", pass);
        editor.putBoolean("sesion", true);
        editor.commit();

    }

}