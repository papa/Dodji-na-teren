package com.example.android.dodjinateren;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;




public class TereniAdapter extends ArrayAdapter<Teren> {


    public TereniAdapter (Activity context, ArrayList<Teren> sports)
    {
        super(context,0,sports);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.tereni,parent,false);
        }
        final Teren trenutniTeren=getItem(position);
        TextView imeTerena=(TextView) view.findViewById(R.id.imeTerena);
        imeTerena.setText(trenutniTeren.getIme());
        ImageView im=(ImageView)view.findViewById(R.id.slikaTerena);
        im.setImageResource(R.drawable.terenislika);
        TextView udaljenostTerena=(TextView) view.findViewById(R.id.udaljenostTerena);

        Location terenL = new Location("");
        terenL.setLatitude(trenutniTeren.getLat());
        terenL.setLongitude(trenutniTeren.getLng());

        float razdaljinaUKm =(float) HomeActivity.userLocation.distanceTo(terenL)/(float)1000;


            /*

            float[] udaljenost = new float[1];
            Location.distanceBetween(trenutniTeren.getLat(), trenutniTeren.getLng(),
                    TereniActivity.userLocation.latitude, TereniActivity.userLocation.longitude, udaljenost);


            */

        udaljenostTerena.setText(String.format("%.2f",razdaljinaUKm));


      /*  ListView listView=(ListView)view.findViewById(R.id.sportkorisnici);
        DatabaseReference korisnici= FirebaseDatabase.getInstance().getReference("Korisnici");
        listView.setAdapter(null);
        listView.setAdapter(HomeActivity.naterenuAdapter);

        korisnici.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 Korisnik k=dataSnapshot.getValue(Korisnik.class);
                 if(k.getSport().equals(MapsActivity.prenos) && k.getTeren().equals(trenutniTeren.getIme()))
                 {
                     HomeActivity.korisnici.add(k);
                     HomeActivity.naterenuAdapter.notifyDataSetChanged();
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
        });*/
        return view;
    }
}

