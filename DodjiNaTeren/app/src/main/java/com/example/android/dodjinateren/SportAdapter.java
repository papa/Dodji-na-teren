package com.example.android.dodjinateren;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jane on 11/28/2017.
 */

public class SportAdapter extends ArrayAdapter<Sport> {

    public SportAdapter(Activity context, ArrayList<Sport> sports)
    {
    super(context,0,sports);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.lise_item,parent,false);
        }
        Sport currentsport=getItem(position);
        TextView spt=(TextView) view.findViewById(R.id.sport);
        spt.setText(currentsport.getSport());
        ImageView im=(ImageView)view.findViewById(R.id.slika);
        switch (currentsport.getSport())
        {
            case "Kosarka":
                im.setImageResource(R.drawable.kosarka);
                break;
            case "Fudbal":
                im.setImageResource(R.drawable.fudbal);
                break;
            case "Tenis":
                im.setImageResource(R.drawable.tenis);
                break;
            default:
                im.setImageResource(R.drawable.plivanje);
        }
        return view;
    }
}
