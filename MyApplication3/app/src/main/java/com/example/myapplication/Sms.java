package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Sms extends AppCompatActivity {
    private EditText smstext,smsno;
    private RadioButton getSms_akraba,getSms_arkadas,getSms_is;
    Button btn;
    RadioButton sms_akraba, sms_arkadas, sms_is;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        smsno=findViewById(R.id.smsNumara);
        smstext=findViewById(R.id.smsMsg);
        btn=findViewById(R.id.smsGonder);
        addListenerOnButton();

        sms_akraba=findViewById(R.id.akraba_sms);
        sms_arkadas=findViewById(R.id.arkadas_sms);
        sms_is=findViewById(R.id.is_sms);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                        sendSMS();
                    }
                    else{
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                    }
                }
            }
        });
    }
    public void addListenerOnButton() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getPhoneNumbers(checkedId);
            }
        });
    }
    public void getPhoneNumbers(Integer id)
    {
        String willSend = "";
        Database db = new Database(getApplicationContext());
        ArrayList<HashMap<String, String>> phoneNumbers = db.getPhoneNumbers(String.valueOf(id));
        for (HashMap<String, String> hashMap : phoneNumbers) {
            for (String key : hashMap.keySet()) {
                if(key.equals("contact_phone_number")){
                    willSend += hashMap.get(key)+",";
                }
            }
        }
        if(!willSend.equals(""))
        {
            willSend = willSend.substring(0, (willSend.length()-1));
        }
        smsno.setText(willSend);

        /*if(id == 2131296594){ // akraba
            smsno.setText(String.valueOf(id));
        }
        else if(id == 2131296595){ // arkadaş
            smsno.setText(String.valueOf(id));
        }
        else if(id == 2131296748){ // iş
            smsno.setText(String.valueOf(id));
        }*/
    }
    private void sendSMS(){
        String SMS = smstext.getText().toString().trim();
        String phoneNo = "";

        if (sms_akraba.isChecked()==true){
            //SharedPreferences türünde sharedPreferences nesnesi oluşturarak bir akraba grubu oluşturdu.
            SharedPreferences sharedPreferences = getSharedPreferences("Akraba", MODE_PRIVATE);
            String json = sharedPreferences.getString("Akraba","");
            Object[] numaraSplit = json.split("\""); //split metodu oluşturulur.
            phoneNo = numaraSplit[7].toString(); //gelen telefon numarası split metodu ile parçalanabilir.
        }
        if (sms_arkadas.isChecked()==true){
            SharedPreferences sharedPreferences = getSharedPreferences("Arkadas", MODE_PRIVATE);
            String json = sharedPreferences.getString("Arkadas","");
            Object[] numaraSplit = json.split("\"");
            phoneNo = numaraSplit[7].toString();
        }
        if (sms_is.isChecked()==true){
            SharedPreferences sharedPreferences = getSharedPreferences("Is", MODE_PRIVATE);
            String json = sharedPreferences.getString("Is","");
            Object[] numaraSplit = json.split("\"");
            phoneNo = numaraSplit[7].toString();
        }
             String[] separated = SMS.split(",");
        try {
            //sms gönderme işlemi
            SmsManager smsManager=SmsManager.getDefault();
            for(int i = 0;i<separated.length;i++){
                smsManager.sendTextMessage(phoneNo,null,separated[i],null,null);
            }
        Toast.makeText(this,"Mesaj Basarili bir sekilde teslim edildi.",Toast.LENGTH_SHORT).show();
       }
       catch (Exception e){
           e.printStackTrace();
           Toast.makeText(this, "Mesaj Gonderme Islemi Basarisiz.", Toast.LENGTH_SHORT).show(); //Ekrana bildirim düşmesi
       }
    }
}
