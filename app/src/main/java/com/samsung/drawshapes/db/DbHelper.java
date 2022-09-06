package com.samsung.drawshapes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.samsung.drawshapes.Logic.DbInfo;

public class DbHelper  extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, DbInfo._DB_NAME, null, DbInfo._DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + DbInfo._DB_TABLE_NAME + " (" +
                DbInfo._COLOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbInfo._COLOR_NAME+ " TEXT, "+
                DbInfo._CURRENT_COLOR+ " INT, "+
                DbInfo._COLOR_VALUE+ " INT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbInfo._DB_TABLE_NAME );
        onCreate(sqLiteDatabase);
    }
}
