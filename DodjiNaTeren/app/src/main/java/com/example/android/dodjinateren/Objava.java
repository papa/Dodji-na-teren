package com.example.android.dodjinateren;

import java.sql.Date;
import java.sql.Time;
/**
 * Created by jane on 12/13/2017.
 */

public class Objava {

    private String id;
    private String teren;
    private String korisnik;
    private String korisnikUid;
    private String sport;
    private int minutiSort;
    private int sat;
    private int minut;
    private String status;

    Objava(String id,String uid,String s, String korisnikuser, String s1,  int sati, int minuti, String s2,int minutiSort)
    {
        this.id=id;
        this.korisnikUid=uid;
        this.teren=s;
        this.korisnik=korisnikuser;
        this.sport=s1;
        this.sat=sati;
        this.minut=minuti;
        this.status=s2;
        this.minutiSort=minutiSort;
    }
    Objava ()
    {

    }
    public String getTeren()
    {
        return  teren;
    }
    public String getKorisnik()
    {
        return korisnik;
    }
    public String getKorisnikUid()
    {
        return korisnikUid;
    }
    public int getSat()
    {
        return sat;
    }
    public int getMinut()
    {
        return minut;
    }
    public String getSport()
    {
        return sport;
    }
    public String getStatus()
    {
        return status;
    }
    public int getMinutiSort()
    {
        return minutiSort;
    }

    public String getId()
    {
        return id;
    }
}
