package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class kisilerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<kisiler> kisilerArrayList;
    public  kisilerAdapter(Activity activity, ArrayList<kisiler> kisilerArrayList){
        this.mInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//ListView satırlarını düzenleme işlemi.
        this.kisilerArrayList=kisilerArrayList;
    }
    @Override
    public  int getCount(){
        return kisilerArrayList.size(); } //Kişiler listesinin boyutunu sayacak.
    @Override
    public  Object getItem(int position){
        return kisilerArrayList.get(position); } //Kişiler listesine getirilecek verilerin yerini belirleyecek
    @Override
    public long getItemId(int position){
        return position; }//Kişiler listesine getirilecek idlerin yerini belirleyecek
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView=mInflater.inflate(R.layout.kisi,null); //Kisi görünümünü convertView nesnesine atar.
        TextView kisiad=(TextView) convertView.findViewById(R.id.adsoyad); //Rehberde bulunan ad soyadı kisiad nesnesine atar.
        TextView kisino=(TextView) convertView.findViewById(R.id.telefonno);//Rehberde bulunan numarayı kisiad nesnesine atar.
        ImageView kisiresim=(ImageView) convertView.findViewById(R.id.resim);//Rehberde bulunan resmi kisiad nesnesine atar.

        kisiler Kisi= kisilerArrayList.get(position); //kisilerArrayList'in pozisyonunu alarak kisiler tipindeki Kisi nesnesine atar
        kisiad.setText(Kisi.get_Adsoyad());//Adsoyadı kisi nesnesi sayesinde çeker
        kisino.setText(Kisi.get_numara());
        if(Kisi.get_resim()!=null){
            kisiresim.setImageBitmap(Kisi.get_resim());
        }
        else
            kisiresim.setImageResource(R.drawable.ic_launcher_background);
        convertView.setTag(Kisi.get_Adsoyad()); //nesneyi görünüm ile ilişkilendirir.
        return  convertView;
    }
}
