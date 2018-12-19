package com.example.maceradores.maceracion.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maceradores.maceracion.models.Mash;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maceraciones";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper( Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Esta es la isntancia donde se crean las tablas.

        //Tabla Maceracion - se agrega el tipo de maceracion: Simple, Escalonada o Decoccion.
        // además se incorpora los tiempos de medicion correspondientes a la temperatura y pH
        // Si fueramos pro le agregariamos la constraint para indicar que el tiempo del pH
        // debe ser mayor a 2 minutos (120) y ser multiplo del de temperatura.
        db.execSQL( "CREATE TABLE Maceracion ("+
                "id INTEGER PRIMARY KEY," +
                "nombre VARCHAR(190) UNIQUE," +
                "tipo VARCHAR(64)," +
                "volumen FLOAT,"+
                "densidadObjetivo FLOAT,"+
                "intervaloMedTemp INTEGER, " + //segundos o minutos
                "intervaloMedPh INTEGER)"); //segundos o minutos?

        // Tabla Intervalo - Maceraciones complejas llevan muchos intervalos de medicion.
        db.execSQL("CREATE TABLE Intervalo(" +
                "id INTEGER PRIMARY KEY, " +
                "orden INTEGER,"+
                "duracion INTEGER," +  //minutos. deberia ser un flotante?
                "temperatura FLOAT, " +
                "desvioTemperatura FLOAT,"+
                "ph FLOAT," +
                "desvioPh FLOAT,"+
                "tempDecoccion FLOAT, " +
                "desvioTempDecoccion FLOAT,"+
                "maceracion INTEGER, " +
                "FOREIGN KEY (maceracion) REFERENCES Maceracion(id))");

        // Tabla Grano - Si bien estaria bueno que estuviera unificado para todas las maceraciones.
        // cada maceracion va a tener su propia lista de granos.
        db.execSQL("CREATE TABLE Grano(" +
                "id INTEGER PRIMARY KEY, " +
                "nombre VARCHAR(190), " +
                "cantidad FLOAT, " +
                "extractoPotencial FLOAT, " +
                "maceracion INTEGER," +
                "FOREIGN KEY (maceracion) REFERENCES Maceracion(id))");

        // Tabla Experimento. A diferencia de la del raspberry se saco lo de las mediciones y
        // se agrego el tema de la densidad obtenida.
        db.execSQL("CREATE TABLE Experimento (id INTEGER PRIMARY KEY, " +
                        " fecha DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        " densidad FLOAT," +
                        " maceracion INTEGER, "+
                        " FOREIGN KEY (maceracion) REFERENCES Maceracion(id))");

        // Tabla SensedValues -

        db.execSQL("CREATE TABLE SensedValues( "+
                "id INTEGER PRIMARY KEY," +
                "idRaspi INTEGER, " +
                "id_exp INTEGER," +
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
        db.execSQL("DROP TABLE IF EXISTS " + "Intervalo");
        db.execSQL("DROP TABLE IF EXISTS " + "Grano");
        db.execSQL("DROP TABLE IF EXISTS " + "Experimento");
        db.execSQL("DROP TABLE IF EXISTS " + "SensedValues");
        //las vuelvo a crear.
        onCreate(db);
    }

    public void deleteDatabase(){
        try{
            SQLiteDatabase db = getReadableDatabase();
            db.delete("Maceracion", null, null);
            //db.execSQL("DROP TABLE IF EXISTS " + "Maceracion");
            db.delete("Intervalo", null, null);
            //db.execSQL("DROP TABLE IF EXISTS " + "Intervalo");
            db.delete("Grano", null, null);
            //db.execSQL("DROP TABLE IF EXISTS " + "Grano");
            db.delete("Experimento", null, null);
            //db.execSQL("DROP TABLE IF EXISTS " + "Experimento");
            //db.execSQL("DROP TABLE IF EXISTS " + "SensedValues");
            db.delete("SensedValues", null, null);
            db.close();
        } catch (SQLException e){
            Log.d("Error DB", e.toString());
        }

    }

    public List<Mash> getAllMash(){
        List<Mash> resultados = new ArrayList<Mash>();
        // tengo que hacer una consulta SQL.

        try{
            SQLiteDatabase db = getReadableDatabase();
            String[] projection = {
                    "id",
                    "nombre",
                    "tipo"
            };

            Cursor cursor = db.query("Maceracion", projection, null, null, null, null, null);

            while(cursor.moveToNext()) {
                String itemName = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre"));
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );
                String tipo = cursor.getString(
                        cursor.getColumnIndexOrThrow("tipo")
                );

                resultados.add(new Mash(id, itemName, tipo));
            }
            cursor.close();

        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }

        return resultados;
    }
}
