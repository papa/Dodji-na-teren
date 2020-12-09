package com.example.android.dodjinateren;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class InfoterActivity extends AppCompatActivity {

    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<Korisnik> arrayList2;
    ArrayAdapter<String> adapter;
    NaterenuAdapter adapter2;
    ListView listView;
    ListView listView2;
    DatabaseReference mojabaza;
    TextView textView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    DatabaseReference mojabaza1;
    TextView textView2;
    TextView textView3;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    ImageView notOn;
    ImageView notOff;
    String id;
    DatabaseReference mojabaza2;
    boolean sub=false;
    static boolean lociraj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        textView2=(TextView)findViewById(R.id.broj);
        arrayList2=new ArrayList<Korisnik>();
        listView2=(ListView)findViewById(R.id.listak);
        adapter2=new NaterenuAdapter(this,arrayList2);
        listView2.setAdapter(adapter2);
        lociraj=false;
        textView=(TextView)findViewById(R.id.imete);
        textView.setText(MapsActivity.prenos);
        mojabaza= FirebaseDatabase.getInstance().getReference("Korisnici");
        mojabaza1=FirebaseDatabase.getInstance().getReference("tereni");
        mojabaza2=FirebaseDatabase.getInstance().getReference("Objave");


        imageView1=(ImageView)findViewById(R.id.Kosarka);
        imageView2=(ImageView)findViewById(R.id.Fudbal);
        imageView3=(ImageView)findViewById(R.id.Tenis);
        imageView4=(ImageView)findViewById(R.id.Plivanje);
        notOn=(ImageView)findViewById(R.id.noton);
        notOff=(ImageView)findViewById(R.id.notoff);

        floatingActionButton=(FloatingActionButton)findViewById(R.id.obrisi);
        imageView=(ImageView) findViewById(R.id.lociraj);
        if(MapsActivity.usoIzMape)
            imageView.setVisibility(View.GONE);
        else
            imageView.setVisibility(View.VISIBLE);

        mojabaza1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Teren t=dataSnapshot.getValue(Teren.class);
                if(t.getIme().equals(MapsActivity.prenos))
                {
                    if(t.getKosarka().equals("nema"))
                    {
                        imageView1.setVisibility(View.GONE);
                    }
                    if(t.getFudbal().equals("nema"))
                    {
                        imageView2.setVisibility(View.GONE);
                    }
                    if(t.getTenis().equals("nema"))
                    {
                        imageView3.setVisibility(View.GONE);
                    }
                    if(t.getPlivanje().equals("nema"))
                    {
                        imageView4.setVisibility(View.GONE);
                    }
                    if(!t.getAutor().equals(HomeActivity.korisnikuser))
                    {
                        floatingActionButton.setVisibility(View.GONE);
                    }
                    id=t.getid();
                    textView2.setText(String.valueOf(t.getKorisnici()));
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

        ChildEventListener childEventListener = mojabaza.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Korisnik k = dataSnapshot.getValue(Korisnik.class);
                if (k.getTeren().equals(MapsActivity.prenos)) {
                    arrayList2.add(k);
                    adapter2.notifyDataSetChanged();
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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(InfoterActivity.this)
                        .setIcon(R.drawable.terenislika)
                        .setTitle("Brisanje terena!")
                        .setMessage("Da li ste sigurni da zelite da izbrisete teren "+MapsActivity.prenos+"?")
                        .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mojabaza1.child(id).removeValue();

                                String id2=mojabaza2.push().getKey();
                                int sati= Calendar.getInstance().getTime().getHours();
                                int minuti=Calendar.getInstance().getTime().getMinutes();

                                mojabaza2.child(id2).setValue(new Objava(id2,HomeActivity.korisnikUid,MapsActivity.prenos,HomeActivity.korisnikuser,"",sati,minuti,"Teren je upravo obrisan!",HomeActivity.brMin()));

                                mojabaza.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        Korisnik korisnik=dataSnapshot.getValue(Korisnik.class);
                                        if(korisnik.getTeren().equals(MapsActivity.prenos))
                                        {
                                            mojabaza.child(korisnik.getId()).child("naterenu").setValue("nije");
                                            mojabaza.child(korisnik.getId()).child("teren").setValue("");
                                            mojabaza.child(korisnik.getId()).child("sport").setValue("");
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


                                Toast.makeText(InfoterActivity.this, "Teren je uspesno obrisan!",Toast.LENGTH_SHORT).show();
                                Intent PokreniActivity=(new Intent(InfoterActivity.this, HomeActivity.class));
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(PokreniActivity);

                            }
                        })
                        .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lociraj=true;
                MapsActivity.prenos= (String) textView.getText();
                MapsActivity.usoIzMape=false;
                startActivity(new Intent(InfoterActivity.this,MapsActivity.class));
            }
        });
        if(HomeActivity.sharedPref.getStringSet("Sub", null)!=null)
            HomeActivity.set.addAll(HomeActivity.sharedPref.getStringSet("Sub", null));
        if(HomeActivity.set.contains(MapsActivity.prenos))
        {
            sub=true;
            notOn.setVisibility(View.VISIBLE);
        }
        else
        {
            sub=false;
            notOff.setVisibility(View.VISIBLE);
        }
        notOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new AlertDialog.Builder(InfoterActivity.this)
                            .setIcon(R.drawable.terenislika)
                            .setTitle("Obavestenja")
                            .setMessage("Da li zelite da iskljucite obavestenja za teren "+MapsActivity.prenos+"?" )
                            .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(HomeActivity.sharedPref.getStringSet("Sub", null)!=null)
                                        HomeActivity.set.addAll(HomeActivity.sharedPref.getStringSet("Sub", null));
                                    HomeActivity.set.remove(MapsActivity.prenos);
                                    HomeActivity.editor.putStringSet("Sub",HomeActivity.set);
                                    HomeActivity.editor.commit();
                                    notOn.setVisibility(View.GONE);
                                    sub=false;
                                    notOff.setVisibility(View.VISIBLE);

                                }
                            })
                            .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

            });
        notOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(InfoterActivity.this)
                        .setIcon(R.drawable.terenislika)
                        .setTitle("Obavestenja")
                        .setMessage("Da li zelite da ukljucite obavestenja za teren " + MapsActivity.prenos + "?")
                        .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (HomeActivity.sharedPref.getStringSet("Sub", null) != null)
                                    HomeActivity.set.addAll(HomeActivity.sharedPref.getStringSet("Sub", null));
                                HomeActivity.set.add(MapsActivity.prenos);
                                HomeActivity.editor.putStringSet("Sub", HomeActivity.set);
                                HomeActivity.editor.commit();
                                notOff.setVisibility(View.GONE);
                                sub=true;
                                notOn.setVisibility(View.VISIBLE);

                            }
                        })
                        .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
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
