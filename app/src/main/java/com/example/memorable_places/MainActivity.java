package com.example.memorable_places;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.memorable_places.MapsActivity.sharedPreferences;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayAdapter adapter;
    static ArrayList<LatLng> longClicks = new ArrayList<>();
   static ArrayList<String> savedPlaces = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.memorable_places", Context.MODE_PRIVATE);

        /* SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();*/

        ListView list = (ListView) findViewById(R.id.listView);

        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<>())));
            Log.i("worked", savedPlaces.toString());
            //places.add("find a new place");
        } catch (Exception e) {
            Log.i("worked", savedPlaces.toString());
        }
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
