package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "app";//database adı

    private static final String TABLE_NAME = "contacts";
    private static String CONTACT_ID = "id";
    private static String CONTACT_PHONE_NUMBER = "contact_phone_number";
    private static String CONTACT_FULLNAME = "contact_fullname";
    private static String CONTACT_GROUP_ID = "contact_group_id";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTACT_FULLNAME + " TEXT,"
                + CONTACT_PHONE_NUMBER + " TEXT,"
                + CONTACT_GROUP_ID + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }
    public void insertPhoneNumber(String contact_fullname, String contact_phone_number,String contact_group_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACT_FULLNAME, contact_fullname);
        values.put(CONTACT_PHONE_NUMBER, contact_phone_number);
        values.put(CONTACT_GROUP_ID, contact_group_id);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public ArrayList<HashMap<String, String>> getPhoneNumbers(String id){
        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + CONTACT_GROUP_ID + " = '"+id+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> phoneNumbers = new ArrayList<HashMap<String, String>>();
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                phoneNumbers.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return phoneNumbers;
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}
