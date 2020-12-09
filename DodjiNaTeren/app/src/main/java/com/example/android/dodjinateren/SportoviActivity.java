package com.example.android.dodjinateren;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SportoviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        final ArrayList<Sport> sportovi=new ArrayList<Sport>();
        sportovi.add(new Sport("Kosarka"));
        sportovi.add(new Sport("Fudbal"));
        sportovi.add(new Sport("Tenis"));
        sportovi.add(new Sport("Plivanje"));
        SportAdapter sportAdapter=new SportAdapter(this,sportovi);
        final ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(sportAdapter);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapsActivity.prenos=sportovi.get(position).getSport();
                startActivity(new Intent(SportoviActivity.this,InfosportActivity.class));
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
