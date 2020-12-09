package com.example.android.dodjinateren;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.android.dodjinateren.HomeActivity.user;

/**
 * Created by jane on 12/29/2017.
 */

public class KorisnikAdapter extends ArrayAdapter<Korisnik>{

    StorageReference storageReference;
    public KorisnikAdapter (Activity context, ArrayList<Korisnik> korisnici,StorageReference storageReference)
    {
       super(context,0,korisnici);
       this.storageReference=storageReference;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.korisnik_item,parent,false);
        }
       Korisnik korisnik=getItem(position);
       TextView textView=(TextView)view.findViewById(R.id.kkorisnik);
       textView.setText(korisnik.getUsername());

        final CircleImageView i=(CircleImageView) view.findViewById(R.id.kslika);

        if (korisnik.getId() == null)
            i.setImageResource(R.drawable.ic_person);
        else {
            StorageReference mStorage = storageReference.child("Photos").child(korisnik.getId());
            StorageReference filepath = mStorage.child("Photos").child(korisnik.getId());
            filepath.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.with(getContext()).load(uri).fit().centerCrop().into(i);

                            // Got the download URL for 'users/me/profile.png'
                        }})
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {
//                                                       Handle any errors
                        }
                    });

        }

       ImageView imageView1=(ImageView)view.findViewById(R.id.naT);
       switch (korisnik.getNaterenu())
       {
           case "jeste":
               imageView1.setImageResource(R.drawable.jeste);
               break;
               default:
                   imageView1.setImageResource(R.drawable.nije);
       }
        return view;
    }

}
