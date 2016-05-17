package com.project.sakib.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by acer on 2/13/2016.
 */
public class DBhelper2 extends SQLiteOpenHelper {

    public static final String DB_NAME="map";
    public static final int version=1;

    public static final String TABLE_NAME="map";
    final static String ITEM_LATITUDE_COLUMN="lat";
    final static String ITEM_LONGITUDE_COLUMN="long";
    final static String ITEM_TIME_COLUMN="time";
    final static String ITEM_PHONE_COLUMN="phone";

    public static final String CREATE_TABLE=
            "CREATE TABLE "+TABLE_NAME
                    +" ("+ITEM_PHONE_COLUMN+" TEXT, "+ITEM_LATITUDE_COLUMN+" TEXT, "+
                    ITEM_LONGITUDE_COLUMN+" TEXT, "+ITEM_TIME_COLUMN+" TEXT)";

    public DBhelper2(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public long InsertPosition(position p){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues C=new ContentValues();
        C.put(ITEM_PHONE_COLUMN, p.getPhone());
        C.put(ITEM_LATITUDE_COLUMN, p.getLatitude());
        C.put(ITEM_LONGITUDE_COLUMN, p.getLongitude());
        C.put(ITEM_TIME_COLUMN, p.getTime());
        long val=db.insert(TABLE_NAME, null, C);
        db.close();
        return val;
    }

    public ArrayList<position> getAllPos(String S1){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor C;
        C=db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<position>P=new ArrayList<position>();

        if(C!=null && C.getCount()>0){
            C.moveToFirst();
            for(int i=0;i<C.getCount();i++){
                String phone=C.getString(C.getColumnIndex(ITEM_PHONE_COLUMN));
                String lat =C.getString(C.getColumnIndex(ITEM_LATITUDE_COLUMN));
                String lng =C.getString(C.getColumnIndex(ITEM_LONGITUDE_COLUMN));
                String time=C.getString(C.getColumnIndex(ITEM_TIME_COLUMN));
                position p=new position(phone,lat,lng,time);
                if(phone.equals(S1))P.add(p);
                C.moveToNext();
            }
        }
        else{
            return null;
        }
        return P;
    }
}
