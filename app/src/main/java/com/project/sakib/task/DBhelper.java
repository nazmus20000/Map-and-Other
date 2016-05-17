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
public class DBhelper extends SQLiteOpenHelper {

    public static final String DB_NAME="profile";
    public static final int version=1;

    public static final String TABLE_NAME="profile";
    final static String ITEM_FNAME_COLUMN="fname";
    final static String ITEM_LNAME_COLUMN="lname";
    final static String ITEM_PHONE_COLUMN="phone";
    final static String ITEM_INTEREST_COLUMN="interest";
    final static String ITEM_IMAGE_COLUMN="image";

    public static final String CREATE_TABLE=
            "CREATE TABLE "+TABLE_NAME
                    +" ("+ITEM_FNAME_COLUMN+" TEXT, "+ITEM_LNAME_COLUMN+" TEXT, "+ITEM_PHONE_COLUMN+" TEXT, "+
                    ITEM_INTEREST_COLUMN+" TEXT, "+ITEM_IMAGE_COLUMN+" TEXT)";

    public DBhelper(Context context) {
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

    public long InsertPerson(person p){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues C=new ContentValues();
        C.put(ITEM_FNAME_COLUMN, p.getFname());
        C.put(ITEM_LNAME_COLUMN, p.getLname());
        C.put(ITEM_PHONE_COLUMN, p.getPhone());
        C.put(ITEM_INTEREST_COLUMN, p.getInterests());
        C.put(ITEM_IMAGE_COLUMN, p.getImage());
        long val=db.insert(TABLE_NAME, null, C);
        db.close();
        return val;
    }

    public ArrayList<person> getSelectedPerson(String S1){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor C;
        C=db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<person>P=new ArrayList<person>();

        if(C!=null && C.getCount()>0){
            C.moveToFirst();
            for(int i=0;i<C.getCount();i++){
                String fname =C.getString(C.getColumnIndex(ITEM_FNAME_COLUMN));
                String lname =C.getString(C.getColumnIndex(ITEM_LNAME_COLUMN));
                String phone=C.getString(C.getColumnIndex(ITEM_PHONE_COLUMN));
                String inte =C.getString(C.getColumnIndex(ITEM_INTEREST_COLUMN));
                String image=C.getString(C.getColumnIndex(ITEM_IMAGE_COLUMN));
                person p=new person(fname,lname,phone,inte,image);
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
