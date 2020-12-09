package com.example.android.dodjinateren;

import android.*;
import android.Manifest;
import android.app.AlertDialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    //OPTIMIZOVANO!!!!!!
    private FirebaseAuth firebaseAuth;
    static DatabaseReference mojabaza;
     static FirebaseUser user;
     static ArrayList<Objava> arrayList;
     ListView listView;
     static ObjavaAdapter adapter;
     static DatabaseReference mojabaza2;
     FirebaseDatabase baza;
     static DatabaseReference mojabaza3;
     static String n;
     static String korisnikUid;
     static boolean prijava;
     TextView textView;
     String username;
     Korisnik k;
    static String usersvuda;
    static String korisnikuser;
    static String korisnikmail;
    static String korisniksport;
    static String korisnikteren;
    static String korsniknaterenu;
    static DatabaseReference mojabaza1;
    private Boolean exit = false;
    static String iden;
    MenuItem check;
    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;
    static Set<String> set = new HashSet<String>();
    static LocationManager locationManager;
    static Location userLocation;
    static LocationListener locationListener;
    static int sat;
    static int minut;
    static int dan;
    static int mesec;
    static int godina;
    static int red;
    ProgressDialog progressdialog;
    static boolean trazenjeLokacie=false;
    static boolean homeVisible;
    SwipeRefreshLayout swipeRefreshLayout;
    Teren tereNaKomeJeKorisnik;
    boolean ucitava=true;
    static StorageReference mStorage;

    private static final int GALLERY_INTENT = 2;
     @Override
    protected void onStart() {
        super.onStart();
        DodajterenActivity.moze=false;
        homeVisible=true;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {
                        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null)
                        {
                            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            userLocation=lastKnownLocation;
                        }
                        else
                        {
                            if(!trazenjeLokacie) {
                                progressdialog.setMessage("Trazenje lokacije...");
                                progressdialog.show();
                                trazenjeLokacie = true;
                            }
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    }

                }

            }

        }

    }
     static public void generisiDatum()
     {
         sat=Calendar.getInstance().get(Calendar.HOUR);
         minut=Calendar.getInstance().get(Calendar.MINUTE);
         dan=Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
         godina=Calendar.getInstance().get(Calendar.YEAR);
     }

    static ArrayList<Korisnik> korisnici;
    static NaterenuAdapter naterenuAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        korisnici=new ArrayList<Korisnik>();
        naterenuAdapter=new NaterenuAdapter(this,korisnici);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.rifres);


        generisiDatum();
        firebaseAuth = FirebaseAuth.getInstance();
        InfoterActivity.lociraj=false;
        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            Intent PokreniActivity=(new Intent(HomeActivity.this, LoginActivity.class));
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(PokreniActivity);
        }
        else {
            homeVisible=true;

            progressdialog = new ProgressDialog(this);

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if( activeNetworkInfo == null ||  !activeNetworkInfo.isConnected())
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this)
                        .setMessage("Internet nije dostupan")
                        .setPositiveButton("Ukljuci internet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                // TODO Auto-generated method stub
                                Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                HomeActivity.this.startActivity(myIntent);

                            }
                        });

                dialog.show();
            }
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    userLocation =location;
                    if(userLocation!=null){
                        if (progressdialog != null && trazenjeLokacie) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Lokacija je pronadjena!",Toast.LENGTH_SHORT).show();
                        }
                        trazenjeLokacie = false;

                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {


                }

                @Override
                public void onProviderDisabled(String provider) {
                    if(homeVisible) {


                        if(!trazenjeLokacie) {

                            progressdialog.setMessage("Trazenje lokacije...");
                            progressdialog.show();

                        }
                        trazenjeLokacie = true;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this)
                                .setMessage("Lokacija nije dostupna")
                                .setPositiveButton("Ukljuci lokaciju", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        // TODO Auto-generated method stub
                                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        HomeActivity.this.startActivity(myIntent);
                                        //get gps
                                    }
                                });

                        dialog.show();


                    }
                    else
                    {
                        Intent PokreniActivity=(new Intent(getApplicationContext(), HomeActivity.class));
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(PokreniActivity);


                        AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this)
                                .setMessage("Lokacija nije dostupna")
                                .setPositiveButton("Ukljuci lokaciju", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        // TODO Auto-generated method stub
                                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        HomeActivity.this.startActivity(myIntent);

                                        //get gps

                                    }
                                });

                        dialog.show();

                        if(!trazenjeLokacie) {
                            progressdialog.setMessage("Trazenje lokacije...");
                            progressdialog.show();
                            trazenjeLokacie = true;
                        }

                    }
                }
            };




            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


            if (Build.VERSION.SDK_INT < 23) {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null)
                {
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    userLocation=lastKnownLocation;
                }
                else {
                    if(!trazenjeLokacie) {
                        progressdialog.setMessage("Trazenje lokacije...");
                        progressdialog.show();
                        trazenjeLokacie = true;
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


                } else {
                    if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null)
                    {
                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        userLocation=lastKnownLocation;
                    }
                    else {
                        if(!progressdialog.isShowing()) {
                            progressdialog.setMessage("Trazenje lokacije...");
                            progressdialog.show();
                            trazenjeLokacie = true;
                        }
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



                }


            }


            sharedPref = this.getSharedPreferences("Sub", this.MODE_PRIVATE);
            editor = sharedPref.edit();
            final NotificationManager NotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            DodajterenActivity.moze = false;
            prijava = false;
            final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);



            user = firebaseAuth.getCurrentUser();

            baza = FirebaseDatabase.getInstance();
            mojabaza = baza.getReference("Korisnici");
            mojabaza2 = baza.getReference("Objave");
            mojabaza3 = baza.getReference("Korisnici").child(user.getUid());
            mojabaza1=baza.getReference("tereni");


            View headerView = navigationView.getHeaderView(0);

            final TextView navUsername = (TextView) headerView.findViewById(R.id.username);

            textView=(TextView)headerView.findViewById(R.id.email);


            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            mStorage = storageReference.child("Photos").child(user.getUid());


            final CircleImageView circleImageView=(CircleImageView)headerView.findViewById(R.id.profileImage);

//            StorageReference storageRef =
//                    FirebaseStorage.getInstance().getReference();
            StorageReference filepath = mStorage.child("Photos").child(user.getUid());
            filepath.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.with(HomeActivity.this).load(uri).fit().centerCrop().into(circleImageView);

                            // Got the download URL for 'users/me/profile.png'
                        }})
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {
//                                                       Handle any errors
                        }
                    });


      /*      circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_PICK);

                    intent.setType("image/*");

                    startActivityForResult(intent, GALLERY_INTENT);

                }
            });*/

            mojabaza3.addValueEventListener(new ValueEventListener() {

                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {

                    k = dataSnapshot.getValue(Korisnik.class);
                    username = k.getUsername();
                    korisnikUid=k.getId();
                    korisnikuser=username;
                    korisnikmail=k.getMail();
                    textView.setText(korisnikmail);
                    korisnikteren=k.getTeren();
                    korisniksport=k.getSport();
                    korsniknaterenu=k.getNaterenu();
                    navUsername.setText(korisnikuser);
                    check = navigationView.getMenu().findItem(R.id.check);

                    if (k.getNaterenu().equals("jeste")) {
                        check.setTitle("Odjavi se sa terena!");
                    }
                    else
                        check.setTitle("Prijavi se na teren!");


                }



                @Override

                public void onCancelled(DatabaseError databaseError) {


                }

            });


            mojabaza1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Teren t =dataSnapshot.getValue(Teren.class);
                    if(t.getIme().equals(korisnikteren))
                        tereNaKomeJeKorisnik=t;


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    Teren t =dataSnapshot.getValue(Teren.class);
                    if(HomeActivity.sharedPref.getStringSet("Sub", null)!=null)
                        HomeActivity.set.addAll(HomeActivity.sharedPref.getStringSet("Sub", null));
                    if(set.contains(t.getIme())) {
                        Toast.makeText(HomeActivity.this, "Promenilo se stanje na terenu "+t.getIme()+"!",
                                Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent(getApplicationContext(), InfoterActivity.class);

                        PendingIntent resultPendingIntent =
                                PendingIntent.getActivity(
                                        getApplicationContext(),
                                        0,
                                        resultIntent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);

                        MapsActivity.prenos=t.getIme();
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        final NotificationCompat.Builder Builder =
                                new NotificationCompat.Builder(HomeActivity.this)
                                        .setSmallIcon(R.drawable.terenislika)
                                        .setContentTitle("Dodji Na Teren")
                                        .setContentIntent(resultPendingIntent)
                                        .setAutoCancel(true)
                                        .setSound(alarmSound)
                                        .setContentText("Promeilo se stanje na terenu "+t.getIme()+"!");
                        NotificationManager.notify(001, Builder.build());



                    }
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
/*
        if(korsniknaterenu=="jeste"&&tereNaKomeJeKorisnik!=null)
            if (Math.abs(HomeActivity.userLocation.getLatitude() - tereNaKomeJeKorisnik.getLat()) <= 0.001 && Math.abs(HomeActivity.userLocation.getLongitude() - tereNaKomeJeKorisnik.getLng()) <= 0.001)
                Toast.makeText(this,"Ne zaboravi da se odjavis sa terena kada odes!",Toast.LENGTH_LONG).show();

*/


            listView = (ListView) findViewById(R.id.listaobjava);
            arrayList = new ArrayList<Objava>();
            adapter = new ObjavaAdapter(this, arrayList,storageReference);
            listView.setAdapter(adapter);


            final ChildEventListener childEventListener=mojabaza2.orderByChild("minutiSort").addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    ucitava=true;

                    Objava o = dataSnapshot.getValue(Objava.class);
                    if(HomeActivity.preKolikoMin(o.getMinutiSort())>24*60*60 && !o.getId().equals("pocetna"))
                    {
                        mojabaza2.child(o.getId()).removeValue();
                    }
                    else {
                        arrayList.add(o);
                        adapter.notifyDataSetChanged();
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

        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                arrayList.clear();
                listView.setAdapter(null);
                listView.setAdapter(adapter);

                mojabaza2.orderByChild("minutiSort").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        Objava o = dataSnapshot.getValue(Objava.class);
                        if(HomeActivity.preKolikoMin(o.getMinutiSort())>24*60*60 && !o.getId().equals("pocetna"))
                        {
                            mojabaza2.child(o.getId()).removeValue();
                        }
                        else {
                            arrayList.add(o);
                            adapter.notifyDataSetChanged();
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            Uri uri = data.getData();
            Log.d("SLIKA", "onActivityResult: " + uri);
            final ImageView circle = (ImageView) findViewById(R.id.profileImage);


//            byte[] slikaBytes = null;
//            try {
//                Bitmap bitmap = Picasso.with(HomeActivity.this).load(uri).fit().centerCrop().resize(200, 200).onlyScaleDown().get();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                slikaBytes = baos.toByteArray();
//
//            }
//            catch(Exception e) {
//                Log.d("SLIKA", "Greska", e);
//            }

            Log.d("SLIKA", "onActivityResult: 2" );
            StorageReference filepath = mStorage.child("Photos").child(user.getUid());

//            filepath.putBytes(slikaBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Log.d("SLIKA 2", "onActivityResult: " + downloadUri);
                    Picasso.with(HomeActivity.this).load(downloadUri).fit().centerCrop().into(circle);

                }


            });

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Pritisnite Back opet ako zelite da izadjete!",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tereni) {
            if(trazenjeLokacie)
            {
                Toast.makeText(this,"Trazenje lokacije...",Toast.LENGTH_SHORT).show();
            }
            else {

                Intent intent = new Intent(HomeActivity.this, TereniActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.prijatelji) {

         Intent intent=new Intent(HomeActivity.this,KorisniciActivity.class);
         startActivity(intent);
        } else if (id == R.id.mapa) {
            if(trazenjeLokacie)
            {
                Toast.makeText(this,"Trazenje lokacije...",Toast.LENGTH_SHORT).show();
            }
            else {


                prijava = false;
                MapsActivity.prenos = "";
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            }
        }
        else if(id==R.id.pozovi)
        {
            Intent i=new Intent(HomeActivity.this,Main3Activity.class);
            startActivity(i);
        }
        else if(id==R.id.sportovi)
        {
            if(trazenjeLokacie)
            {
                Toast.makeText(this,"Trazenje lokacije...",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, SportoviActivity.class);
                startActivity(intent);
            }
        }
        else if(id==R.id.logout)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
        else if(id==R.id.podesavanja)
        {
            startActivity(new Intent(HomeActivity.this,PodesavanjaActivity.class));
        }
        else if(id==R.id.check)
        {
            if(trazenjeLokacie)
            {
                Toast.makeText(this,"Trazenje lokacije...",Toast.LENGTH_SHORT).show();
            }
            else {
                prijava = true;
                MapsActivity.prenos = "";
                if (check.getTitle().equals("Odjavi se sa terena!")) {

                    startActivity(new Intent(HomeActivity.this, IzaberioActivity.class));
                } else {
                    startActivity(new Intent(this, MapsActivity.class));
                }
            }
        }
        else
        {
            if(trazenjeLokacie)
            {
                Toast.makeText(this,"Trazenje lokacije...",Toast.LENGTH_SHORT).show();
            }
            else {

                startActivity(new Intent(this, DodajterenActivity.class));
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static int brojdanaugod(int g)
    {
        if(g%400==0 || (g%4==0 && g%100!=0))
            return 366;
        return 365;
    }
    static public int preKolikoMin(int brMin)
    {
        HomeActivity.generisiDatum();
        int minutiukupno1=0;

        int i;

        minutiukupno1=minutiukupno1+(HomeActivity.dan-1)*60*24;
        minutiukupno1=minutiukupno1+HomeActivity.sat*60+HomeActivity.minut;

        for(i=2018;i<HomeActivity.godina;i++)
        {
            minutiukupno1=minutiukupno1+brojdanaugod(i)*24*60;
        }
        return minutiukupno1*60+Calendar.getInstance().get(Calendar.SECOND)+brMin;

    }
    static int brMin(){
        HomeActivity.generisiDatum();
        int mi2=HomeActivity.minut;
        int s2=HomeActivity.sat;
        int d2=HomeActivity.dan;
        int g2=HomeActivity.godina;

        int minutiukupno2=0;
        int i;

        for(i=2018;i<HomeActivity.godina;i++)
        {
            minutiukupno2=minutiukupno2+brojdanaugod(i)*24*60;
        }

        minutiukupno2=minutiukupno2+(d2-1)*60*24;
        minutiukupno2=minutiukupno2+s2*60+mi2;
        minutiukupno2=minutiukupno2*60+Calendar.getInstance().get(Calendar.SECOND);

        return -minutiukupno2;
    }

    @Override
    protected void onPause() {
        homeVisible=false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        homeVisible = true;
        super.onResume();
    }
}
