package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
public class Rehber extends AppCompatActivity {

    public static final int REQUEST_READ_CONTACTS=1; //REQUEST_READ_CONTACTS=1 ifadesi uygulmaya verileri okuma iznini verir.
    ListView rehber_list;
    Button btnsmsekrani;
    RadioButton akraba, arkadas,is;
    ArrayList<kisiler> Kisiler;
    kisilerAdapter KisilerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehber);

        btnsmsekrani=(Button) findViewById(R.id.btn_sms_ekrani);
        rehber_list=findViewById(R.id.rehber_list);

        akraba=(RadioButton) findViewById(R.id.radioButton2);
        arkadas=(RadioButton) findViewById(R.id.radioButton3);
        is=(RadioButton) findViewById(R.id.radioButton4);

        ArrayList<kisiler> Kisiler=new ArrayList<kisiler>();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            Cursor rehber= getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null ,null,null,null);
            while (rehber.moveToNext())
            {
                //rehberden Çağırılma işlemleri
                @SuppressLint("Range") String adsoyad=rehber.getString(rehber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String numara=rehber.getString(rehber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                @SuppressLint("Range") String contactID=rehber.getString(rehber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                kisiler r_nesnesi=new kisiler();

                r_nesnesi.set_adsoyad(adsoyad); //r_nesnesi'ne adsoyadı atamak.
                r_nesnesi.set_numara(numara);
                r_nesnesi.set_resim(ContactPhoto(contactID));
                Kisiler.add(r_nesnesi);
            }
            rehber.close();

            kisilerAdapter KisilerAdapter = new kisilerAdapter(this,Kisiler);
            if(rehber_list!=null){
                rehber_list.setAdapter(KisilerAdapter);
            }
        }
        else
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);

        btnsmsekrani.setOnClickListener(new View.OnClickListener() { //Butona basıldığında bir diğer sayfa olan sms sayfasına geçiş yaplması.
            @Override
            public void onClick(View v) {
              Intent i= new Intent(Rehber.this,Sms.class);
                startActivity(i);
            }
        });
        rehber_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //listView uzun basılma durumu

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("asd",(KisilerAdapter.getItem(position)).toString());
                if (akraba.isChecked()==true){
                    SharedPreferences sharedPreferences = getSharedPreferences("Akraba", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    Object[] array = Kisiler.toArray();
                    String json = gson.toJson(array[position]);
                    editor.putString("Akraba",json);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Seçim Gruba Başarılı Bir Şekilde Eklendi.", Toast.LENGTH_LONG).show();
                }
                else if (arkadas.isChecked()==true){
                    SharedPreferences sharedPreferences = getSharedPreferences("Arkadas", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    Object[] array = Kisiler.toArray();
                    String json = gson.toJson(array[position]);
                    editor.putString("Arkadas",json);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Seçim Gruba Başarılı Bir Şekilde Eklendi", Toast.LENGTH_LONG).show();
                }
                else if (is.isChecked()==true){
                    SharedPreferences sharedPreferences = getSharedPreferences("Is", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    Object[] array = Kisiler.toArray();
                    String json = gson.toJson(array[position]);
                    editor.putString("Is",json);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Seçim Başarılı Bir Şekilde Eklendi", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Lütfen Önce Bir Grup Seçiniz.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
    public Bitmap ContactPhoto(String contactID){
        //Fotoğrafın rehberden çekilme işlemi.
        Uri contactUri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.valueOf(contactID));
        Uri PhotoUri=Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(PhotoUri,new String[]{ContactsContract.Contacts.Photo.PHOTO},null,null,null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToNext();
            byte[] data =cursor.getBlob(0); //blob metodu ile veriyi sütuna kaydeder.
            if (data!=null){ //eğer rehberde fotoğraf varsa fotoğrafı çekecek.
                return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            }
            else
                return null;
        }
        cursor.close();
        return null;
    }
}