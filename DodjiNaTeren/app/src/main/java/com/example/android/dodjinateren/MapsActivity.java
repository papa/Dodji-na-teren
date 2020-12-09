package com.example.android.dodjinateren;
import java.util.Calendar;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener {

    static GoogleMap mMap;
    DatabaseReference mojabaza;
    static String prenos;
    Marker marker;
    Marker markerNovi;
    DatabaseReference mojabaza2;
    DatabaseReference mojabaza3;
    FirebaseUser user;
    Korisnik k;
    String s123;
    String t;
    static String imek;
    static String prenos2;
    private FirebaseAuth firebaseAuth;
    static String tekst;
    static String id;
    static int minuti;
    static int sati;
    static boolean usoIzMape=false;
    FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mojabaza = FirebaseDatabase.getInstance().getReference("tereni");
        mojabaza2=FirebaseDatabase.getInstance().getReference("Korisnici");
        mojabaza3=FirebaseDatabase.getInstance().getReference("Objave");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        if(HomeActivity.prijava || DodajterenActivity.moze)
            fab.setVisibility(View.GONE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MapsActivity.this, DodajterenActivity.class));
                }

        });
    }

    @SuppressLint("ResourceType")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);


        final LatLng trenutnelok = new LatLng(HomeActivity.userLocation.getLatitude(), HomeActivity.userLocation.getLongitude());
        mMap.clear();
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(trenutnelok));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        HomeActivity.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, HomeActivity.locationListener);

        mojabaza.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (!HomeActivity.prijava) {
                    try {
                        Teren teren1 = dataSnapshot.getValue(Teren.class);
                        LatLng latLng = new LatLng(teren1.getLat(), teren1.getLng());
                        if (teren1.getKorisnici() > 5)
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(teren1.getIme()));
                        else if (teren1.getKorisnici() == 0)
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(teren1.getIme()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        else
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(teren1.getIme()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        if (teren1.getIme().equals(MapsActivity.prenos)&& !DodajterenActivity.moze) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            marker.showInfoWindow();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Teren teren1 = dataSnapshot.getValue(Teren.class);
                        if (Math.abs(HomeActivity.userLocation.getLatitude() - teren1.getLat()) <= 0.01 && Math.abs(HomeActivity.userLocation.getLongitude() - teren1.getLng()) <= 0.01) {
                            LatLng latLng = new LatLng(teren1.getLat(), teren1.getLng());
                            if (teren1.getKorisnici() > 5)
                                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(teren1.getIme()));
                            else if (teren1.getKorisnici() == 0)
                                mMap.addMarker(new MarkerOptions().position(latLng).title(teren1.getIme()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            else
                                mMap.addMarker(new MarkerOptions().position(latLng).title(teren1.getIme()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
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

        if (DodajterenActivity.moze) {

            Button dugmeDodajTeren = (Button) findViewById(R.id.dodajter);
            dugmeDodajTeren.setVisibility(View.VISIBLE);

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    markerNovi.setPosition(marker.getPosition());
                }
            });

            markerNovi = mMap.addMarker(new MarkerOptions().position(trenutnelok).title(DodajterenActivity.editText.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            markerNovi.showInfoWindow();
            markerNovi.setDraggable(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(HomeActivity.userLocation.getLatitude(), HomeActivity.userLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {

                    markerNovi.setPosition(trenutnelok);
                    return false;
                }
            });


            dugmeDodajTeren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String id=mojabaza.push().getKey();
                    Teren t = new Teren(id,DodajterenActivity.editText.getText().toString(),DodajterenActivity.kosarka,DodajterenActivity.fudbal,DodajterenActivity.tenis,DodajterenActivity.plivanje , markerNovi.getPosition().latitude, markerNovi.getPosition().longitude,0,HomeActivity.korisnikuser);
                    mojabaza.child(id).setValue(t);
                    sati = Calendar.getInstance().getTime().getHours();
                    minuti=Calendar.getInstance().getTime().getMinutes();

                    DodajterenActivity.moze=false;
                    Toast.makeText(MapsActivity.this, "Teren je uspesno dodat!",Toast.LENGTH_SHORT).show();
                    Intent PokreniActivity=(new Intent(MapsActivity.this, HomeActivity.class));
                    String ide=mojabaza3.push().getKey();
                    mojabaza3.child(ide).setValue(new Objava(ide,HomeActivity.korisnikUid,DodajterenActivity.editText.getText().toString(),HomeActivity.korisnikuser,"",sati,minuti,"Upravo je dodat novi teren!",HomeActivity.brMin()));
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(PokreniActivity);

                }


            });

        }
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {


        if(!DodajterenActivity.moze)
        if(!HomeActivity.prijava)
        {
            usoIzMape=true;
            startActivity(new Intent(MapsActivity.this,InfoterActivity.class));
            prenos=marker.getTitle();
        }
        else
            {
                           if (HomeActivity.korsniknaterenu.equals("nije")) {
                               new AlertDialog.Builder(MapsActivity.this)
                                       .setIcon(R.drawable.terenislika)
                                       .setTitle("Prijava!")
                                       .setMessage("Da li zelite da se prijavite na teren "+marker.getTitle()+"?")
                                       .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {

                                               prenos2=marker.getTitle();
                                               Toast.makeText(MapsActivity.this, prenos2, Toast.LENGTH_LONG).show();
                                               sati = Calendar.getInstance().getTime().getHours();
                                               minuti=Calendar.getInstance().getTime().getMinutes();
                                               tekst = marker.getTitle();
                                               id = mojabaza3.push().getKey();
                                               startActivity(new Intent(MapsActivity.this,IzaberisportActivity.class));

                                           }
                                       })
                               .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                   }
                               }).show();
                           }

                       }

        return false;
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        if(DodajterenActivity.moze){
            markerNovi.setPosition(latLng);
        }

    }
}