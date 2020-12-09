package com.example.android.dodjinateren;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PodesavanjaActivity extends AppCompatActivity {


    EditText editus;
    EditText edite;
    EditText editp;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ImageView update;
    DatabaseReference korisnici;
    DatabaseReference objave;
    DatabaseReference tereni;
    String trenutni;
    CircleImageView circleImageView;
    private static final int GALLERY_INTENT = 2;
    StorageReference mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podesavanja);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        editus=(EditText)findViewById(R.id.editusername);
        edite=(EditText)findViewById(R.id.editmail);
        editp=(EditText)findViewById(R.id.editpass);
        update=(ImageView)findViewById(R.id.update);

        editus.setText(HomeActivity.korisnikuser);
        trenutni=editus.getText().toString();
        edite.setText(HomeActivity.korisnikmail);

        korisnici= FirebaseDatabase.getInstance().getReference("Korisnici").child(user.getUid());
        objave=FirebaseDatabase.getInstance().getReference("Objave");
        tereni=FirebaseDatabase.getInstance().getReference("tereni");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = storageReference.child("Photos").child(user.getUid());


        final CircleImageView circleImageView=(CircleImageView)findViewById(R.id.slikakoris);

//            StorageReference storageRef =
//                    FirebaseStorage.getInstance().getReference();
        StorageReference filepath = mStorage.child("Photos").child(user.getUid());
        filepath.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.with(PodesavanjaActivity.this).load(uri).fit().centerCrop().into(circleImageView);

                        // Got the download URL for 'users/me/profile.png'
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
//                                                       Handle any errors
                    }
                });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editus.getText().toString().isEmpty() && !edite.getText().toString().isEmpty())
                {

                    korisnici.child("username").setValue(editus.getText().toString());
                    korisnici.child("mail").setValue(edite.getText().toString());

                    HomeActivity.korisnikuser=editus.getText().toString();
                    HomeActivity.korisnikmail=edite.getText().toString();
                    user.updateEmail(edite.getText().toString());

                    tereni.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                          Teren t=dataSnapshot.getValue(Teren.class);
                          if(t.getAutor().equals(trenutni))
                          {
                              tereni.child(t.getid()).child("autor").setValue(HomeActivity.korisnikuser);
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

                    objave.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Objava o=dataSnapshot.getValue(Objava.class);
                            if(o.getKorisnik().equals(trenutni))
                            {
                                objave.child(o.getId()).child("korisnik").setValue(HomeActivity.korisnikuser);
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

                    if(!editp.getText().toString().isEmpty()) {
                            user.updatePassword(editp.getText().toString());
                    }
                    Toast.makeText(PodesavanjaActivity.this,"Vas profil je uspesno podesen!",Toast.LENGTH_LONG).show();


                    Intent PokreniActivity=(new Intent(PodesavanjaActivity.this, HomeActivity.class));
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(PokreniActivity);
                }
                else
                {
                    if(editus.getText().toString().isEmpty())
                    {
                        Toast.makeText(PodesavanjaActivity.this,"Morate popuniti polje za username",Toast.LENGTH_LONG).show();
                    }
                    if(edite.getText().toString().isEmpty())
                    {
                        Toast.makeText(PodesavanjaActivity.this,"Morate popuniti polje za e-mail",Toast.LENGTH_LONG).show();
                    }
                }

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
