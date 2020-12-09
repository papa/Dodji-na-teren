package com.example.android.dodjinateren;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Calendar;
import java.util.Map;

import static com.example.android.dodjinateren.HomeActivity.iden;
import static com.example.android.dodjinateren.HomeActivity.korisniksport;
import static com.example.android.dodjinateren.HomeActivity.korisnikteren;
import static com.example.android.dodjinateren.HomeActivity.korisnikuser;
import static com.example.android.dodjinateren.HomeActivity.mojabaza;
import static com.example.android.dodjinateren.HomeActivity.mojabaza1;
import static com.example.android.dodjinateren.HomeActivity.mojabaza2;


public class IzaberioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izaberio);
        new AlertDialog.Builder(IzaberioActivity.this)
                .setIcon(R.drawable.terenislika)
                .setTitle("Odjava!")
                .setMessage("Da li zelite da se odjavite sa terena "+HomeActivity.korisnikteren +"? ")
                .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int sati = Calendar.getInstance().getTime().getHours();
                        int minuti=Calendar.getInstance().getTime().getMinutes();
                        iden = mojabaza2.push().getKey();
                        mojabaza2.child(iden).setValue(new Objava(iden,HomeActivity.korisnikUid,korisnikteren, korisnikuser,korisniksport,sati,minuti,"Korisnik se odjavio sa terena!",HomeActivity.brMin()));
                        final String naKomTerenu=korisnikteren;
                        HomeActivity.mojabaza1.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Teren teren=dataSnapshot.getValue(Teren.class);
                                if(teren.getIme().equals(naKomTerenu))
                                {

                                    mojabaza1.child(teren.getid()).child("korisnici").setValue(teren.getKorisnici()-1);
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
                        mojabaza.child(HomeActivity.user.getUid()).child("naterenu").setValue("nije");
                        mojabaza.child(HomeActivity.user.getUid()).child("teren").setValue("");
                        mojabaza.child(HomeActivity.user.getUid()).child("sport").setValue("");
                        Toast.makeText(IzaberioActivity.this, "Uspesno ste se odjavili sa terena " +korisnikteren+"!", Toast.LENGTH_SHORT).show();

                        HomeActivity.korsniknaterenu="nije";
                        HomeActivity.korisnikteren="";
                        HomeActivity.korisniksport="";
                        Intent PokreniActivity=(new Intent(IzaberioActivity.this, HomeActivity.class));
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(PokreniActivity);
                    }
                })
                .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent PokreniActivity=(new Intent(IzaberioActivity.this, HomeActivity.class));
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(PokreniActivity);
                    }
                }).show();
    }
}
