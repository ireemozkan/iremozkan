package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Açılan pencerenin 2 saniye sonra kapanıp bir sonraki ekrana geçmesini sağlar.
        Thread redirect = new Thread(){
            @Override
            public  void run(){
                try {
                    sleep(2000);
                    Intent i = new Intent(getApplicationContext(),Rehber.class);
                    startActivity(i);
                    finish();
                    super.run();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        redirect.start();
    }
}