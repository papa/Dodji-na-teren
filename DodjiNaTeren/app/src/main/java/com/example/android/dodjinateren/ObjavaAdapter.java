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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jane on 12/26/2017.
 */
public class ObjavaAdapter extends ArrayAdapter<Objava> {

    StorageReference storageReference;
    public ObjavaAdapter (Activity context, ArrayList<Objava> objave,StorageReference storageReference)
    {
        super(context,0,objave);
        this.storageReference = storageReference;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.objava_item,parent,false);

        }
        Objava objava=getItem(position);
        TextView spt=(TextView) view.findViewById(R.id.ouser);
        spt.setText(objava.getKorisnik());
        TextView d=(TextView)view.findViewById(R.id.ovreme);
        d.setText(objava.getSat()+":"+objava.getMinut());
        TextView ter=(TextView)view.findViewById(R.id.oteren);
        ter.setText(objava.getTeren());
        TextView st=(TextView)view.findViewById(R.id.ostatus);
        st.setText(objava.getStatus());

        final CircleImageView i=(CircleImageView) view.findViewById(R.id.slikao);

        if (objava.getKorisnikUid() == null)
            i.setImageResource(R.drawable.ic_person);
        else {
            StorageReference mStorage = storageReference.child("Photos").child(objava.getKorisnikUid());
            StorageReference filepath = mStorage.child("Photos").child(objava.getKorisnikUid());
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

        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.l3);
        if(objava.getSport().equals(""))
        {
            linearLayout.setVisibility(View.GONE);
        }
        else
        {
            TextView sport=(TextView)view.findViewById(R.id.osport);
            sport.setText(objava.getSport());
            linearLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
