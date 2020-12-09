package com.example.android.dodjinateren;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class KorisniciActivity extends AppCompatActivity {
    //OPTIMIZOVANO !!!!
    private DatabaseReference mojabaza;
    ArrayList<Korisnik> arrayList=new ArrayList<>();
    KorisnikAdapter adapter;
    ListView listView;
    ArrayList<String> arrayList2;
    static String prenos;
    SwipeRefreshLayout swipeRefreshLayout;
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.rifreskor);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storage = storageReference.child("Photos").child(HomeActivity.user.getUid());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayList2=new ArrayList<>();
        mojabaza= FirebaseDatabase.getInstance().getReference("Korisnici");
        listView=(ListView)findViewById(R.id.listak);
        adapter=new KorisnikAdapter(this,arrayList,storageReference);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prenos=arrayList.get(position).getUsername();
                startActivity(new Intent(KorisniciActivity.this,InfokorActivity.class));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                listView.setAdapter(null);
                listView.setAdapter(adapter);
                mojabaza.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        try{
                            Korisnik k1 = dataSnapshot.getValue(Korisnik.class);
                            arrayList.add(k1);
                            adapter.notifyDataSetChanged();
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        arrayList.clear();
        mojabaza.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try{
                    Korisnik k1 = dataSnapshot.getValue(Korisnik.class);
                    arrayList.add(k1);
                    adapter.notifyDataSetChanged();
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
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
}
