package com.example.android.dodjinateren;

import android.location.Location;

import com.example.android.dodjinateren.Teren;
import com.example.android.dodjinateren.TereniActivity;

import java.util.Comparator;

/**
 * Created by Aleksandar on 1/6/2018.
 */

public class KomparatorTeren implements Comparator<Teren> {

    @Override
    public int compare(Teren t1,Teren t2) {
        Location prvi = new Location("");
        prvi.setLatitude(t1.getLat());
        prvi.setLongitude(t1.getLng());
        int razdaljinaPrvi =(int) HomeActivity.userLocation.distanceTo(prvi);

        Location drugi = new Location("");
        drugi.setLatitude(t2.getLat());
        drugi.setLongitude(t2.getLng());
        int razdaljinaDrugi =(int) HomeActivity.userLocation.distanceTo(drugi);


        if(razdaljinaDrugi<razdaljinaPrvi){
            return 1;
        } else {
            return -1;
        }
    }
}