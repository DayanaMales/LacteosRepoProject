package com.example.lacteos;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table admin2(usuario text primary key,password text)");
        String usuario="admin";
        String password="12345";
        ContentValues datosAdmin= new ContentValues();
        datosAdmin.put("usuario",usuario);
        datosAdmin.put("password",password);
        db.insert("admin2","(usuario,password)",datosAdmin);
        db.execSQL("create table personas(cedula int primary key,nombres text,apellidos text,sexo text," +
                "pais text,provincia text,direccion text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
