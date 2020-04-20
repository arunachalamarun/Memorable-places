package com.example.memorable_places;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

//import static com.example.memorable_places.MapsActivity.sharedPreferences;

public class MainActivity<Public> extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayAdapter adapter;
    static ArrayList<LatLng> longClicks = new ArrayList<>();
    static ArrayList<String> savedPlaces = new ArrayList<String>();
    static SharedPreferences sharedPreferences;
    ListView list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        try {
            switch (item.getItemId()) {
                case R.id.delete:
                    change();
                    return true;
                case R.id.help:
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();

        }
    return true;
    }


    public void change() {
        try {
            new AlertDialog.Builder(this)
                    //.setIcon(R.drawable.ic_launcher_background)
                    .setTitle("CONFIRM DELETE ALL").setMessage("Are you sure you want to delete this")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sharedPreferences.edit().clear().commit();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();

                            // SharedPreferences.Editor editor;
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                            editor = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE).edit();
                            editor.clear();
                            editor.commit();
                            //adapter();
                            places.clear();
                            places.add("find a new places");
                            adapter.notifyDataSetChanged();

                            // PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()

                        }
                    })

                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();

                        }
                    })
                    .show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
        }

    }


    public void delete() {
       /* SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
*/
        //  SharedPreferences.Editor editor;
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
      /*  editor = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();*/
        places.add("find a new place");
        //   PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()


        Log.i("erased", "erased");

    }
    /*public void OnCLick(View view){
        Button b1=(Button)findViewById(R.id.delete1);
        delete();
    }*/


    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.memorable_places", Context.MODE_PRIVATE);

        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<>())));
            Log.i("worked", savedPlaces.toString());
        } catch (Exception e) {
            Log.i("worked", savedPlaces.toString());
        }
        if (places.isEmpty()) {
            places.add("find a new place");
            adapter();
        } else {
            adapter();

        }

    }

    public void adapter() {
        list = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, places);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), MapsActivity.class);
                in.putExtra("arg", position);
                startActivity(in);
            }
        });
    }

}
