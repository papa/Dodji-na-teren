package com.example.android.dodjinateren;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

public class DodajterenActivity extends AppCompatActivity {


    static EditText editText;
    static boolean moze;
    CheckBox checkBox;
    static String kosarka;
    static String fudbal;
    static String tenis;
    static String plivanje;
    MapView mapView;
    GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);




        editText=(EditText)findViewById(R.id.imet);

        moze=false;
        kosarka="nema";
        fudbal="nema";
        tenis="nema";
        plivanje="nema";
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void dodaj(View vie)
    {
        if(editText.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Unesite ime terena",Toast.LENGTH_LONG).show();
        }
        else
        {
            checkBox=(CheckBox)findViewById(R.id.kosarka);
            if(checkBox.isChecked())
            {
               kosarka="ima";
            }
            checkBox=(CheckBox)findViewById(R.id.fudbal);
            if(checkBox.isChecked())
            {
                fudbal="ima";
            }
            checkBox=(CheckBox)findViewById(R.id.tenis);
            if(checkBox.isChecked())
            {
                tenis="ima";
            }
            checkBox=(CheckBox)findViewById(R.id.plivanje);
            if(checkBox.isChecked())
            {
                plivanje="ima";
            }
            if(kosarka.equals("nema") && fudbal.equals("nema") && tenis.equals("nema") && plivanje.equals("nema"))
            {
                Toast.makeText(this,"Oznacite bar jedan sport",Toast.LENGTH_LONG).show();
            }
            else
            {
               startActivity(new Intent(DodajterenActivity.this,MapsActivity.class));
               moze=true;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}



