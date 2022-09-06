package com.samsung.drawshapes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.drawshapes.Logic.DbInfo;

import java.util.ArrayList;

public class DbOps {

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    public DbOps(Context context) {
        mDbHelper = new DbHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }


    public long insert (String colorName, int colorValue){

        ContentValues cv = new ContentValues();
        cv.put(DbInfo._COLOR_NAME,colorName);
        cv.put(DbInfo._COLOR_VALUE,colorValue);


        return mDb.insert(DbInfo._DB_TABLE_NAME,null,cv);

    }



    public void delete(long id) {
        mDb.delete(DbInfo._DB_TABLE_NAME, DbInfo._COLOR_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public int getColorIdByValue(int value){

        Cursor mCursor = mDb.query(DbInfo._DB_TABLE_NAME, new String[] { DbInfo._COLOR_ID },
                "value = " + "'"+value+"'", null, null, null, null);
        int id = 0;
        System.out.println("selected Value" + value);

            mCursor.moveToFirst();
            id = mCursor.getInt(0);
            mCursor.close();

        return  id;
    }
    public int getColorById(int  id){

        Cursor mCursor = mDb.query(DbInfo._DB_TABLE_NAME, new String[] { DbInfo._COLOR_NAME },
                "id like " + "'"+id+"'", null, null, null, null);
        mCursor.moveToFirst();
        int colorCode = mCursor.getInt(3);
        mCursor.close();
        return  colorCode;
    }

/*    public String getCurrentPlayerName(){
        Cursor mCursor = mDb.query(Constants.DB_TABLE_NAME, new String[] { Constants.PLAYER_NAME },
                Constants.IS_CURRENT_PAYER+" = 1", null, null, null, null);
        mCursor.moveToFirst();
        String name = mCursor.getString(0);
        mCursor.close();
        return  name;

    }*/

    public ArrayList <MyColor> getColors(){
        Cursor mCursor = mDb.query(DbInfo._DB_TABLE_NAME, null, null,
                null, null, null, null);
        ArrayList<MyColor> colorsArray = new ArrayList<MyColor>();
        mCursor.moveToFirst();
        if(!mCursor.isAfterLast()){

            do{
                int id = mCursor.getInt(0);
                String name = mCursor.getString(1);
                int cucrentColor  = mCursor.getInt(2);
                int colorValue = mCursor.getInt(3);
                System.out.println("colorVal" + colorValue);
                colorsArray.add(new MyColor(id,name,colorValue,cucrentColor));
            }
            while (mCursor.moveToNext());


        }
        mCursor.close();
        return  colorsArray;



    }



}
