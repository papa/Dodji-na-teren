package com.example.android.dodjinateren;

import java.util.ArrayList;

/**
 * Created by jane on 12/5/2017.
 */

public class Teren {
    private String id;
    private String ime;
    private String kosarka;
    private String fudbal;
    private String tenis;
    private String plivanje;
    private double lat;
    private double lng;
    private int korisnici;
    private String autor;
    Teren()
    {

    }
    Teren(String ide,String i,String k,String f,String t,String p,double lat,double lng,int kor,String aut)
    {
        this.ime=i;
        this.id=ide;
        this.kosarka=k;
        this.fudbal=f;
        this.plivanje=p;
        this.tenis=t;
        this.lat=lat;
        this.lng=lng;
        this.korisnici=kor;
        this.autor=aut;
    }
    public String getIme()
    {
        return ime;
    }
    public double getLat()
    {
        return lat;
    }
    public double getLng(){
        return lng;
    }
    public String getKosarka()
    {
        return kosarka;
    }
    public String getFudbal()
    {
        return fudbal;
    }
    public String getTenis()
    {
        return tenis;
    }
    public String getPlivanje()
    {
        return plivanje;
    }
    public int getKorisnici()
    {
        return  korisnici;
    }
    public String getAutor(){
        return autor;
    }
    public String getid()
    {
        return id;
    }
}
