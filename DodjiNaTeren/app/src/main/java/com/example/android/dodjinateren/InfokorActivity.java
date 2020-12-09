package com.example.android.dodjinateren;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InfokorActivity extends AppCompatActivity {


    ImageView imageView1;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageView imageView2;
    DatabaseReference mojabaza;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infokor);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       textView1=(TextView)findViewById(R.id.uname);
        textView2=(TextView)findViewById(R.id.gm);
        textView3=(TextView)findViewById(R.id.terr);
        textView4=(TextView)findViewById(R.id.sportt);
        imageView1=(ImageView)findViewById(R.id.slikakor);
        imageView2=(ImageView)findViewById(R.id.slikasp);
        mojabaza= FirebaseDatabase.getInstance().getReference("Korisnici");

        mojabaza.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Korisnik k=dataSnapshot.getValue(Korisnik.class);
                if(k.getUsername().equals(KorisniciActivity.prenos))
                {
                    textView1.setText(k.getUsername());
                    textView2.setText(k.getMail());
                    if(k.getNaterenu().equals("jeste"))
                    {
                        textView3.setText(k.getTeren());
                        textView4.setText(k.getSport());
                        switch (k.getSport())
                        {
                            case "Kosarka":
                                imageView2.setImageResource(R.drawable.kosarka);
                                break;
                            case "Fudbal":
                                imageView2.setImageResource(R.drawable.fudbal);
                                break;
                            case "Tenis":
                                imageView2.setImageResource(R.drawable.tenis);
                                break;
                            default: imageView2.setImageResource(R.drawable.plivanje);
                        }
                    }
                    else
                    {
                        textView3.setText("Korisnik nije prijavljen ni na jedan teren !");
                        textView4.setText("-");
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
