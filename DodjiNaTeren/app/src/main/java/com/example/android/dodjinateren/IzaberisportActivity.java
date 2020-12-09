package com.example.android.dodjinateren;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class IzaberisportActivity extends AppCompatActivity {

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    DatabaseReference mojabaza1;
    DatabaseReference mojabaza2;
    DatabaseReference mojabaza3;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ArrayList<String> arrayList;
    String sport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izaberisport);
        imageView1=(ImageView)findViewById(R.id.sport1);
        imageView2=(ImageView)findViewById(R.id.sport2);
        imageView3=(ImageView)findViewById(R.id.sport3);
        imageView4=(ImageView)findViewById(R.id.sport4);
        arrayList=new ArrayList<>();
        arrayList.clear();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        mojabaza1= FirebaseDatabase.getInstance().getReference("tereni");
        mojabaza3=FirebaseDatabase.getInstance().getReference("Objave");
        mojabaza2=FirebaseDatabase.getInstance().getReference("Korisnici");

        mojabaza1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Teren t=dataSnapshot.getValue(Teren.class);
                if(t.getIme().equals(MapsActivity.prenos2))
                {
                    if(t.getKosarka().equals("ima"))
                    {
                        arrayList.add("Kosarka");
                    }
                    else
                    {
                        imageView1.setVisibility(View.GONE);
                    }
                    if(t.getFudbal().equals("ima"))
                    {
                        arrayList.add("Fudbal");
                    }
                    else
                    {
                        imageView2.setVisibility(View.GONE);
                    }
                    if(t.getTenis().equals("ima"))
                    {
                        arrayList.add("Tenis");
                    }
                    else
                    {
                        imageView3.setVisibility(View.GONE);
                    }
                    if(t.getPlivanje().equals("ima"))
                    {
                        arrayList.add("Plivanje");
                    }
                    else
                    {
                        imageView4.setVisibility(View.GONE);
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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



       imageView1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Prijavi("Kosarka");


           }
       });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Prijavi("Fudbal");
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Prijavi("Tenis");
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Prijavi("Plivanje");
            }
        });

    }

    public void Prijavi(String sport){



        mojabaza3.child(MapsActivity.id).setValue(new Objava(MapsActivity.id,HomeActivity.korisnikUid,MapsActivity.prenos2, HomeActivity.korisnikuser,sport,MapsActivity.sati,MapsActivity.minuti,"Korisnik se prijavio na teren!",HomeActivity.brMin()));
        mojabaza2.child(user.getUid()).child("naterenu").setValue("jeste");
        mojabaza2.child(user.getUid()).child("teren").setValue(MapsActivity.prenos2);
        mojabaza2.child(user.getUid()).child("sport").setValue(sport);
        HomeActivity.korsniknaterenu="jeste";
        HomeActivity.korisnikteren=MapsActivity.prenos2;
        HomeActivity.korisniksport=sport;
        Toast.makeText(IzaberisportActivity.this, "Uspesno ste se prijavili", Toast.LENGTH_SHORT).show();
        Intent PokreniActivity=(new Intent(IzaberisportActivity.this, HomeActivity.class));
        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(PokreniActivity);
        mojabaza1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Teren t=dataSnapshot.getValue(Teren.class);
                if(t.getIme().equals(MapsActivity.prenos2))
                {
                    mojabaza1.child(t.getid()).child("korisnici").setValue(t.getKorisnici()+1);
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
