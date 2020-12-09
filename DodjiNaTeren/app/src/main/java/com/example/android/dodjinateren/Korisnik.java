package com.example.android.dodjinateren;

/**
 * Created by jane on 12/7/2017.
 */

public class Korisnik {

    private String id;
    private String mail;
    private String username;
    private String naterenu;
    private String teren;
    private String sport;
    Korisnik()
    {

    }
    Korisnik(String id,String e,String us,String nat,String t,String sp)
    {
        this.id=id;
        this.mail=e;
        this.username=us;
        this.naterenu=nat;
        this.teren=t;
        this.sport=sp;
    }
    public String getMail(){
        return mail;
    }
    public String getUsername()
    {
        return username;
    }
    public String getId(){
        return id;
    }
    public String getNaterenu(){
        return naterenu;
    }
    public String getTeren(){
        return teren;
    }
    public String getSport()
    {
        return sport;
    }
}
