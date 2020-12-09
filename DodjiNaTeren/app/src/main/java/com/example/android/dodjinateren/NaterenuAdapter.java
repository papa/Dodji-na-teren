package com.example.android.dodjinateren;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jane on 1/2/2018.
 */

public class NaterenuAdapter extends ArrayAdapter<Korisnik> {

    public NaterenuAdapter(Activity context, ArrayList<Korisnik> korisnici)
    {
        super(context,0,korisnici);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.naterenu_item,parent,false);
        }
        Korisnik korisnik=getItem(position);
        TextView textView=(TextView)view.findViewById(R.id.usernamenat);
        textView.setText(korisnik.getUsername());
        ImageView imageView=(ImageView)view.findViewById(R.id.slikanat);
        imageView.setImageResource(R.drawable.prijatelji);
        ImageView imageView1=(ImageView)view.findViewById(R.id.sportnat);
       switch (korisnik.getSport())
       {
           case "Kosarka":
               imageView1.setImageResource(R.drawable.kosarka);
               break;
           case "Fudbal":
               imageView1.setImageResource(R.drawable.fudbal);
               break;
           case "Tenis":
               imageView1.setImageResource(R.drawable.tenis);
               break;
           default:
               imageView1.setImageResource(R.drawable.plivanje);

       }
        return view;
    }
}
