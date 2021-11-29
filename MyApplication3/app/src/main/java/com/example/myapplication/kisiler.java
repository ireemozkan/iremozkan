package com.example.myapplication;

import android.graphics.Bitmap;

public class kisiler {
    private String isim;
    private String numara;
    private Bitmap resim=null;

    public  void set_adsoyad(String isim){this.isim=isim;}
    public  String get_Adsoyad(){return isim;}

    public  void set_numara(String numara){this.numara=numara;}
    public  String get_numara(){return numara;}

    public void set_resim(Bitmap resim){this.resim=resim;}
    public  Bitmap get_resim(){return resim;}

}
