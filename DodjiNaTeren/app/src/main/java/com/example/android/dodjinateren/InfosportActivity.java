package com.example.android.dodjinateren;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.dodjinateren.MapsActivity.prenos;

public class InfosportActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    ListView listView;
    ArrayList<Teren> arrayList;
    DatabaseReference mojabaza;
    TereniAdapter tereniAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infosport);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayList=new ArrayList<>();
        textView=(TextView)findViewById(R.id.sportnat);
        textView.setText(MapsActivity.prenos);
        imageView=(ImageView)findViewById(R.id.slikkka);
        switch (MapsActivity.prenos)
        {
            case "Kosarka":
                imageView.setImageResource(R.drawable.kosarka);
                break;
            case "Fudbal":
                imageView.setImageResource(R.drawable.fudbal);
                break;
            case "Tenis":
                imageView.setImageResource(R.drawable.tenis);
                break;
            default:
                imageView.setImageResource(R.drawable.plivanje);
        }
        listView=(ListView)findViewById(R.id.listanat);
        mojabaza= FirebaseDatabase.getInstance().getReference("tereni");
        tereniAdapter=new TereniAdapter(this,arrayList);
        listView.setAdapter(tereniAdapter);

        mojabaza.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Teren teren=dataSnapshot.getValue(Teren.class);
                if(MapsActivity.prenos.equals("Kosarka") && teren.getKosarka().equals("ima"))
                {
                    arrayList.add(teren);
                    Collections.sort(arrayList,new KomparatorTeren());
                    tereniAdapter.notifyDataSetChanged();
                }

                if(MapsActivity.prenos.equals("Fudbal") && teren.getFudbal().equals("ima"))
                {
                    arrayList.add(teren);
                    Collections.sort(arrayList,new KomparatorTeren());
                    tereniAdapter.notifyDataSetChanged();
                }
                if(MapsActivity.prenos.equals("Tenis") && teren.getTenis().equals("ima"))
                {
                    arrayList.add(teren);
                    Collections.sort(arrayList,new KomparatorTeren());
                    tereniAdapter.notifyDataSetChanged();
                }
                if(MapsActivity.prenos.equals("Plivanje") && teren.getPlivanje().equals("ima"))
                {
                    arrayList.add(teren);
                    Collections.sort(arrayList,new KomparatorTeren());
                    tereniAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prenos=arrayList.get(position).getIme();
                startActivity(new Intent(InfosportActivity.this,InfoterActivity.class));
            }
        });
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
