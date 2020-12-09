package com.example.android.dodjinateren;

/**
 * Created by jane on 11/28/2017.
 */

public class Sport {
    private String sport;
    private int brojslike;
    Sport(String s)
    {
        this.sport=s;
        this.brojslike=R.drawable.ic_launcher_background;
    }
    public String getSport()
    {
        return sport;
    }
    public int getbroj()
    {
        return brojslike;
    }
}
