package com.example.maceradores.maceracion.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maceradores.maceracion.models.Experiment;
import com.example.maceradores.maceracion.models.Grain;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;
import com.example.maceradores.maceracion.models.SensedValues;
import com.example.maceradores.maceracion.utils.Calculos;

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

    //------------------MASH-----------------------
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
            db.close();

        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }

        return resultados;
    } //end getAllMash

    public int deleteMash(int idMash){
        int cant = 0;

        try{
            SQLiteDatabase db = getReadableDatabase();
            //elimino primero todos los experimentos. Despues la maceracion

            String selection = "maceracion = ?";
            String [] selectionArgs = new String[] { String.valueOf(idMash)};

            //Eliminar los sensedValues.
            db.execSQL("DELETE FROM SensedValues WHERE id_exp IN (SELECT id FROM Experimento WHERE maceracion = ?)", selectionArgs);

            db.delete("Experimento", selection, selectionArgs);

            // Quito los granos
            db.delete("Grano", selection, selectionArgs);

            //Quito los intervalos de medicion
            db.delete("Intervalo", selection, selectionArgs);

            //Ahora elimino la maceracion y vuelvo al main activity
            selection = "id = ?";
            selectionArgs = new String[] { String.valueOf(idMash)};
            cant = db.delete("Maceracion", selection, selectionArgs );

            db.close();

        } catch (SQLException e){
            Log.d( "Error DB", e.toString());
        }

        return cant;
    } //end deleteMash

    public Mash getMash(int idMash){
        Mash mash = new Mash();
        mash.setId(idMash);
        try{

            SQLiteDatabase db = getReadableDatabase();
            String selection = "id = ?";
            String[] selectionArgs = { String.valueOf(idMash)};

            Cursor cursor = db.query("Maceracion", null, selection, selectionArgs, null, null, null);
            if( cursor.moveToFirst()){
                // primero pongo el titulo con el nombre de la maceracion
                mash.setName(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));

                //tipo de maceracion : spinner.
                mash.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));

                // volumen
                mash.setVolumen(cursor.getFloat(cursor.getColumnIndexOrThrow("volumen")));

                //densidad objetivo
                mash.setDensidadObjetivo(cursor.getFloat(cursor.getColumnIndexOrThrow("densidadObjetivo")));

                //medicion temperatura
                mash.setPeriodMeasureTemperature(cursor.getInt(cursor.getColumnIndexOrThrow("intervaloMedTemp")));

                //medicion ph
                mash.setPeriodMeasurePh(cursor.getInt(cursor.getColumnIndexOrThrow("intervaloMedPh")));

            }
            cursor.close();
        } catch (SQLException e){
            Log.d("Error db", e.toString());
        }

        return mash;
    }

    public String getNameMash(int idMash){
        String name = "";

        try{
            SQLiteDatabase db = getReadableDatabase();
            //elimino primero todos los experimentos. Despues la maceracion

            String[] columns = new String[]{"nombre"};
            String selection = "id = ?";
            String [] selectionArgs = new String[] { String.valueOf(idMash)};

            Cursor c = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
            if(c.moveToFirst()){
                name = c.getString(0);
            }
            c.close();
            db.close();
        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }

        return name;
    }

    public int getIntervaloMedicionTemp(int idMash){
        int intervaloMedicion = 0;
        // saber el periodo de medicion.
        //DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {"intervaloMedTemp"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            intervaloMedicion = cursor.getInt(0); //como tengo una sola columna, devuelvo la primera nomas.
        }
        cursor.close();
        db.close();
        if(intervaloMedicion<60)intervaloMedicion=60; // Minimo Intervalo de Medicion es de 30 seg

        return intervaloMedicion;
    }

    public List<Float> getDensities(int idMash){
        List<Float> densities = new ArrayList<>();

        try{
            SQLiteDatabase db = getReadableDatabase();

            String[] columns = {"densidad"};
            String selection = "maceracion = ? AND densidad IS NOT NULL";
            String[] selectionArgs = { String.valueOf(idMash)};

            Cursor cursor = db.query("Experimento", columns, selection, selectionArgs, null, null, null);

            while( cursor.moveToNext()){
                //double yield = Calculos.calcRendimiento(volMosto, cursor.getFloat(0), kgMalta)[2]; //este dos es porque el tercer valor es el rendimiento
                densities.add(cursor.getFloat(0));
            }
            cursor.close();
        } catch ( SQLException e){
            Log.d("Error DB", e.toString());
        }

        return densities;
    }

    public int insertMash(Mash mash){
        int resultado = 1;
        try{
            SQLiteDatabase db = getWritableDatabase();

            ContentValues mashValues = new ContentValues();

            mashValues.put( "nombre", mash.getName()); //el nombre tiene la clausula unique
            mashValues.put( "tipo", mash.getTipo());
            mashValues.put( "volumen", mash.getVolumen());
            mashValues.put( "densidadObjetivo", mash.getDensidadObjetivo());
            mashValues.put( "intervaloMedTemp", mash.getPeriodMeasureTemperature());
            mashValues.put( "intervaloMedPh", mash.getPeriodMeasurePh());

            long newMashId = db.insert("Maceracion", null, mashValues);

            if( newMashId != -1){
                ContentValues grainValues;

                for(int i = 0; i < mash.getGrains().size(); i++){
                    grainValues = new ContentValues();
                    grainValues.put("nombre", mash.getGrains().get(i).getName());
                    grainValues.put("cantidad", mash.getGrains().get(i).getQuantity());
                    grainValues.put("extractoPotencial", mash.getGrains().get(i).getExtractPotential());
                    grainValues.put("maceracion", newMashId);

                    long newGrainId = db.insert("Grano",null, grainValues );
                } // end fir agregado de granos.

                //Ahora agregamos los intervalos de medicion.
                ContentValues intervalValues;

                for( int i = 0; i < mash.getPlan().size(); i++){
                    intervalValues = new ContentValues();

                    intervalValues.put("orden", i+1); // como le quedo definido, los pongo en ese orden
                    //intervalValues.put("duracion", intervals.get(i).getDuration());
                    intervalValues.put("duracion", mash.getPlan().get(i).getDuration());
                    intervalValues.put("temperatura", mash.getPlan().get(i).getMainTemperature());
                    intervalValues.put("desvioTemperatura", mash.getPlan().get(i).getMainTemperatureDeviation());
                    intervalValues.put("ph", mash.getPlan().get(i).getpH());
                    intervalValues.put("desvioPh", mash.getPlan().get(i).getPhDeviation());
                    intervalValues.put("tempDecoccion", mash.getPlan().get(i).getSecondTemperature());
                    intervalValues.put("desvioTempDecoccion", mash.getPlan().get(i).getSecondTemperatureDeviation());
                    intervalValues.put("maceracion", newMashId);

                    long newIntervalId = db.insert("Intervalo", null, intervalValues);
                }

            } else {
                resultado = -1;
            }
            db.close();
        }catch(SQLException e){
            Log.d("Error DB", e.toString());
        }

        return resultado;

    }

    public int getDurationTotal(int idMash){
        //DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        int duracionTotal = 0;
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT SUM(duracion) FROM Intervalo WHERE maceracion = ?", new String[]{String.valueOf(idMash)});

            if(c.moveToFirst()){
                duracionTotal = c.getInt(0);
            }
            c.close();
            db.close();
        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }


        return duracionTotal;
    }

    public List<Integer> getDurationEachInterval(int idMash){
        List<Integer> mediciones = new ArrayList<Integer>();

        try{
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {"duracion"};
            String selection = "maceracion = ?";
            String[] selectionArgs = { String.valueOf(idMash)};

            Cursor cursor = db.query("Intervalo", columns, selection, selectionArgs, null, null, "orden DESC");
            while(cursor.moveToNext()){
                mediciones.add( cursor.getInt(0) );
            }
            cursor.close();
            db.close();

        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }

        return mediciones;
    }
    //------------------MASH-----------------------

    //------------------GRAIN-----------------------
    public List<Grain> getGrains(int idMash){
        List<Grain> grains = new ArrayList<>();
        try{
            SQLiteDatabase db = getReadableDatabase();
            String selection = "maceracion = ?";
            String [] selectionArgs = new String[] { String.valueOf(idMash)};
            Cursor cursor = db.query("Grano", null, selection, selectionArgs, null, null, null);

            while(cursor.moveToNext()){

                String name = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                Float quantity = cursor.getFloat(cursor.getColumnIndexOrThrow("cantidad"));
                Float extract = cursor.getFloat(cursor.getColumnIndexOrThrow("extractoPotencial"));

                Grain grain = new Grain(name, quantity, extract);

                grains.add(grain);
            }//end while

            cursor.close();
            db.close();
        } catch (SQLException e){
            Log.d("Error db", e.toString());
        }

        return grains;
    }
    //------------------GRAIN-----------------------

    //------------------INTERVAL/STAGE-----------------------
    public List<MeasureInterval> getMeasureIntervals(int idMash){
        List<MeasureInterval> intervals = new ArrayList<>();
        try{
            SQLiteDatabase db = getReadableDatabase();
            String selection = "maceracion = ?";
            String [] selectionArgs = new String[] { String.valueOf(idMash)};
            Cursor cursor = db.query("Intervalo", null, selection, selectionArgs, null, null, "orden ASC");

            while(cursor.moveToNext()){
                int order = cursor.getInt(cursor.getColumnIndexOrThrow("orden"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duracion"));

                float temperature = cursor.getFloat(cursor.getColumnIndexOrThrow("temperatura"));
                float temperatureDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioTemperatura"));

                float ph = cursor.getFloat(cursor.getColumnIndexOrThrow("ph"));
                float phDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioPh"));

                float temperatureDecoccion = cursor.getFloat(cursor.getColumnIndexOrThrow("tempDecoccion"));
                float temperatureDecoccionDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioTempDecoccion"));

                MeasureInterval interval = new MeasureInterval(order, temperature, temperatureDeviation, temperatureDecoccion, temperatureDecoccionDeviation, ph, phDeviation, duration);

                intervals.add(interval);
                //mash.addMeasureInterval(interval);
            }//end while

            cursor.close();
            db.close();
        }catch (SQLException e){
            Log.d("Error db", e.toString());
        }
        return intervals;
    }

    public MeasureInterval getMeasureIntervalByOrder(int idMash, int order){
        MeasureInterval measureInterval = null;
        try{
            //DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            SQLiteDatabase db = getReadableDatabase();
            String selection = "maceracion = ? AND orden = ?";
            String[] selectionArgs = { String.valueOf(idMash), String.valueOf(order)};

            Cursor cursor = db.query("Intervalo", null, selection, selectionArgs, null, null, null);
            if(cursor.moveToFirst()){
                float mainTemperature = cursor.getFloat( cursor.getColumnIndexOrThrow("temperatura"));
                float mainTemperatureDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioTemperatura"));
                float secondTemperature = cursor.getFloat(cursor.getColumnIndexOrThrow("tempDecoccion"));
                float secondTemperatureDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioTempDecoccion"));
                float pH = cursor.getFloat(cursor.getColumnIndexOrThrow("ph"));
                float phDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioPh"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duracion"));

                measureInterval = new MeasureInterval(order,mainTemperature, mainTemperatureDeviation, secondTemperature, secondTemperatureDeviation, pH,  phDeviation,  duration);
            }
            cursor.close();
            db.close();
        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }

        return measureInterval;
    }

    //------------------INTERVAL/STAGE-----------------------

    //------------------EXPERIMENT-----------------------
    public long insertNewExperiment(int idMash){
        long newExperimentId = -1;
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues experimentValues = new ContentValues();
            experimentValues.put("maceracion", idMash);
            newExperimentId = db.insert("Experimento", null, experimentValues);


        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }
        return newExperimentId;
    }

    public List<Experiment> getAllExperiments(int idMash) {
        List<Experiment> resultados = new ArrayList<Experiment>();

        try{
            SQLiteDatabase db = getReadableDatabase();

            String[] selectionArgs = { String.valueOf(idMash)};
            Cursor cursor = db.rawQuery("SELECT E.id AS 'id', strftime('%d/%m/%Y %H:%M', E.fecha) AS 'fecha'" +
                    " FROM Experimento AS E "+
                    "WHERE E.maceracion = ? AND densidad IS NOT NULL ORDER BY E.fecha DESC", selectionArgs);

            while(cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );
                String fecha = cursor.getString(
                        cursor.getColumnIndexOrThrow("fecha")
                );

                resultados.add(new Experiment(id, fecha));
            } // end while
            cursor.close();

        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }

        return resultados;
    } //end getAllExperiments

    public int deleteExperiment(int idExp){
        SQLiteDatabase db = getReadableDatabase();
        String [] selectionArgs = new String[] { String.valueOf(idExp)};

        db.delete("SensedValues", "id_exp = ?", selectionArgs);
        int cant = db.delete("Experimento", "id = ?", selectionArgs);
        db.close();
        return cant;
    }

    public int insertDensity(int idExp, float density){
        int cant = 0;
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues experimentValues = new ContentValues();
            experimentValues.put("densidad", density );
            String whereClausule = "id = ?";
            String[] whereClausuleArgs = new String[] {String.valueOf(idExp)};
            cant = db.update("Experimento", experimentValues,whereClausule, whereClausuleArgs);
        } catch(SQLException e){
            Log.d("Error DB", e.toString());
        }
        return cant;
    }

    //------------------EXPERIMENT-----------------------

    //------------------SENSED VALUES-----------------------
    public void updateSensedValue( int idSV, float[] v ){
        //DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues sensedValues = new ContentValues();

        sensedValues.put("temp1", v[0] );
        sensedValues.put("temp2", v[1] );
        sensedValues.put("temp3", v[2] );
        sensedValues.put("temp4", v[3] );
        sensedValues.put("ph", v[4] );

        String whereClausule = "id = ?";
        String[] whereClausuleArgs = new String[] {String.valueOf(idSV)};
        int cant = db.update("SensedValues", sensedValues,whereClausule, whereClausuleArgs);
        //dbHelper.close();
    }

    public SensedValues getLastSensedValue(int idExp){
        SensedValues sv = null;
        try{
            //DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM SensedValues WHERE id_exp = ? AND id = (SELECT MAX(id) FROM SensedValues)", new String[] {String.valueOf(idExp)});

            if(cursor.moveToFirst()){
                int id = cursor.getInt( cursor.getColumnIndexOrThrow("id"));
                int idRaspi = cursor.getInt( cursor.getColumnIndexOrThrow("idRaspi"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("fechayhora"));
                float temp1 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp1"));
                float temp2 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp2"));
                float temp3 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp3"));
                float temp4 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp4"));
                float temp5 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp5"));
                float tempPh = cursor.getFloat(cursor.getColumnIndexOrThrow("tempPh"));
                float tempAmb = cursor.getFloat(cursor.getColumnIndexOrThrow("tempAmb"));
                float humidity = cursor.getFloat(cursor.getColumnIndexOrThrow("humity"));
                float pH= cursor.getFloat(cursor.getColumnIndexOrThrow("pH"));

                sv = new SensedValues(id,idRaspi, date, temp1, temp2, temp3, temp4, temp5, tempPh, humidity, tempAmb, pH);
            }
            cursor.close();
            db.close();
        } catch( SQLException e){
           Log.d("Error DB", e.toString());
        }

        return sv;
    }

    public int amountSensedValues(int idExp){
        SQLiteDatabase db = getReadableDatabase();
        int amount = (int) DatabaseUtils.queryNumEntries(db, "SensedValues", "id_exp=?", new String[] {String.valueOf(idExp)});
        db.close();
        return amount;
    }

    //------------------SENSED VALUES-----------------------

/*
    try{
    } catch(SQLException e){
        Log.d("Error DB", e.toString());
    }


*/
}

