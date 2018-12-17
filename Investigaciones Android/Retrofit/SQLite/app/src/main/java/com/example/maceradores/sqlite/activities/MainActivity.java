package com.example.maceradores.sqlite.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.maceradores.sqlite.DatabaseHelper;
import com.example.maceradores.sqlite.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the new activty
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                //intent.putExtra("extra", "manteca");
                startActivity(intent);
            }
        });

        testBD();
    }

    private void testBD(){
        // Make a method which write and read from the database.

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Ahora puedo escribir en la base de datos,
        ContentValues values = new ContentValues();
        values.put("nombre", "test SQLite 6");
        values.put( "tipo", "simple");
        values.put( "frecMedTemp", 1);
        values.put("frecMedPh", 2);

        long newRowId = db.insert("Maceracion", null, values);

        // cuenta la leyenda que en newRowId tengo el id del ultimo valor insertado.
        if( newRowId != -1){
            Toast.makeText(this, "Inserto sin problemas", Toast.LENGTH_SHORT).show();
            // si lo inserto, veamos que pueda obtener lo que acabo de insertar.
            db = dbHelper.getReadableDatabase();
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    "id",
                    "nombre",
                    "tipo"
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = "id = ?";
            String[] selectionArgs = { String.valueOf(newRowId)};
            //String[] selectionArgs = new String[]{"4"};

            Cursor cursor = db.query("Maceracion", projection, selection, selectionArgs, null, null, null);
            //Cursor cursor = db.query("Maceracion", null, selection, selectionArgs, null, null, null);

            List itemNames = new ArrayList<>();
            while(cursor.moveToNext()) {
                String itemName = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre"));
                itemNames.add(itemName);
            }
            cursor.close();

            if(itemNames.size() > 0){
                Toast.makeText(this, itemNames.get(0).toString(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "No pudo leer el valor recien insertado", Toast.LENGTH_SHORT).show();
            }
            //me quedaría eliminarlo para que quede limpia la bd
            int cant_eliminados = db.delete("Maceracion", selection, selectionArgs);
            if( cant_eliminados == 1){
                Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "No se elimino nada", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Hubo problemas", Toast.LENGTH_SHORT).show();
        }

        //al final tengo que cerrar la base de datos. En verdad esto iria en el metodo onDestroy
        dbHelper.close();

    }
}
