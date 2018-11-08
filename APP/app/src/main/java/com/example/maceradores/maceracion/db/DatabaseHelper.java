package com.example.maceradores.maceracion.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maceraciones";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper( Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Esta es la isntancia donde se crean las tablas.
        db.execSQL( "CREATE TABLE Maceracion (id INTEGER PRIMARY KEY,nombre varchar(190) UNIQUE)");

        // Defino la tabla experimento igual de como esta en el servidor Raspi.
        db.execSQL("CREATE TABLE Experimento (id INTEGER PRIMARY KEY, " +
                        " fecha DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        " maceracion int, "+
                        " duracion_min int,"+
                        " intervaloMedicionTemp_seg FLOAT," +
                        " intervaloMedicionPH_seg FLOAT," +
                        " FOREIGN KEY (maceracion) REFERENCES Maceracion(id))");
        
        db.execSQL("CREATE TABLE SensedValues( "+
                "id INTEGER PRIMARY KEY," +
                "id_exp INT," +
                "fechayhora DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "temp1 FLOAT," +
                "temp2 FLOAT," +
                "temp3 FLOAT," +
                "temp4 FLOAT," +
                "temp5 FLOAT," +
                "tempPh FLOAT," +
                "tempAmb FLOAT," +
                "humity FLOAT," +
                "pH FLOAT," +
                "FOREIGN KEY (id_exp) REFERENCES Experimento(id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Borro las tablas
        db.execSQL("DROP TABLE IF EXISTS " + "Maceracion");
        db.execSQL("DROP TABLE IF EXISTS " + "Experimento");
        db.execSQL("DROP TABLE IF EXISTS " + "SensedValues");
        //las vuelvo a crear.
        onCreate(db);
    }
}
